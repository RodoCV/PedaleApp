package com.example.pedaleapp.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pedaleapp.Guardia_principal;
import com.example.pedaleapp.LoginActivity;
import com.example.pedaleapp.R;
import com.example.pedaleapp.model.Guardia;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class Guardia_profile extends Fragment {

    public Guardia_profile() {
        // Required empty public constructor
    }

    // --- Elementos del Fragment ---
    private ImageView photo_profile;
    private TextView txt_rut, txt_nombre, txt_apellido, txt_correo;
    private Button btn_camera, btn_gallery, btn_logout;
    private Uri uri_profilePhoto;
    // --- ---

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guardia_profile, container, false);

        // --- Inicializar los controles ---
        photo_profile = view.findViewById(R.id.photo_guard_profile);
        txt_rut = view.findViewById(R.id.txt_rut_guard_profile);
        txt_nombre = view.findViewById(R.id.txt_nombre_guard_profile);
        txt_apellido = view.findViewById(R.id.txt_apellido_guard_profile);
        txt_correo = view.findViewById(R.id.txt_correo_guard_profile);
        btn_camera = view.findViewById(R.id.btn_camera_guard_profile);
        btn_gallery = view.findViewById(R.id.btn_gallery_guard_profile);
        btn_logout = view.findViewById(R.id.btn_logout_guard_profile);
        // ---

        // --- Extraer valores ---
        final Guardia_principal activity = (Guardia_principal) getActivity();
        Guardia guardia = activity.obtenerGuardia();
        if (!guardia.getUrl_userPhoto().contains("NULL")){
            Glide.with(getActivity()).load(guardia.getUrl_userPhoto()).into(photo_profile);
        }
        // --- ---

        // --- Usar valores ---
        txt_rut.setText(guardia.getRut());
        txt_nombre.setText(guardia.getNombre());
        txt_apellido.setText(guardia.getApellido());
        txt_correo.setText(guardia.getCorreo());
        // --- ---

        // --- Botón Abrir Cámara ---
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //Intent que permite trabajar con la cámara.
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //Permiso para trabajar con la URI de la imagen.
                File foto = new File(getActivity().getExternalFilesDir(null), "/test.jpg"); //Crea un archivo Java para guardar la foto, temporal.
                uri_profilePhoto = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", foto); //Obtiene la URI de la foto, mediante la actividad concatenada con el "provider" del Manifest.
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri_profilePhoto);
                startActivityForResult(intent, Guardia_principal.CODE_CAMERA); //Desde Guardia_Principal, obtengo el código que determina usar Cámara.
            }
        });
        // --- ---

        // --- Botón Abrir Galería ---
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //Intent que permite trabajar con elementos externos en base a URI.
                startActivityForResult(intent, Guardia_principal.CODE_GALLERY); //Desde Guardia_Principal, obtengo el código que determina usar Galería.
            }
        });
        // --- ---

        // --- Botón Cerrar Sesión ---
        btn_logout.setOnClickListener(new View.OnClickListener() { //Método para accionar el botón.
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), LoginActivity.class); //Crea la Pantalla del Login.
                startActivity(i); //Muestra la pantalla creada.
                activity.finish(); //Cierra la Pantalla actual.
            }
        });
        // ---

        // Inflate the layout for this fragment
        return view;
    }

    // --- Método que recibirá la información desde Guardia_principal al cargar una foto ---
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // --- Extraer valores ---
        Guardia_principal activity = (Guardia_principal) getActivity();
        final Guardia guardia = activity.obtenerGuardia();
        // --- ---

        // --- Parámetros de Conexión al Storage ---
        StorageReference storage = FirebaseStorage.getInstance().getReference(); //Inicializa el Storage de Firebase.
        StorageReference folder = storage.child("Guardia_Profile"); //Crea carpeta donde se guardará la imagen.
        StorageReference photo = folder.child(guardia.getRut() + "_User_Photo"); //Crea el nombre de la imagen.
        // --- ---

        // --- Parámetros de Conexión a la Base de Datos
        FirebaseDatabase database = FirebaseDatabase.getInstance(); //Inicializa la Database Realtime de Firebase.
        final DatabaseReference reference = database.getReference("Guardia"); //Raíz de la BD, es como definir Tabla.
        // --- ---

        switch (requestCode) {
            // --- Caso Usuario accede a la Cámara ---
            case Guardia_principal.CODE_CAMERA:
                Bitmap bitmapCamera = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/test.jpg");
                photo_profile.setImageBitmap(bitmapCamera);

                // --- Proceso de Insert de la imagen ---
                photo.putFile(uri_profilePhoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        Uri downloadUri = uriTask.getResult();

                        guardia.setUrl_userPhoto(downloadUri.toString()); //Ingresa la URL de la Foto al Guardia.
                        reference.child(guardia.getRut()).setValue(guardia); //Actualiza el "child" con el RUT como ID, en la "tabla" Guardia.
                    }
                });
                // --- Fin Proceso de Insert de la imagen ---
                break;
            // --- Fin Caso Usuario accede a la Cámara ---

            // --- Caso Usuario accede a la Galería ---
            case Guardia_principal.CODE_GALLERY:
                if (data != null) {
                    uri_profilePhoto = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri_profilePhoto);
                        photo_profile.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // --- Proceso de Insert de la imagen ---
                    photo.putFile(uri_profilePhoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri downloadUri = uriTask.getResult();

                            guardia.setUrl_userPhoto(downloadUri.toString()); //Ingresa la URL de la Foto al Guardia.
                            reference.child(guardia.getRut()).setValue(guardia); //Actualiza el "child" con el RUT como ID, en la "tabla" Guardia.
                        }
                    });
                    // --- Fin Proceso de Insert de la imagen ---
                }
                break;
            // --- Fin Caso Usuario accede a la Galería ---
        }
    }
    // --- ---
}