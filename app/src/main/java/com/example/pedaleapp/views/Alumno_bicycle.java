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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pedaleapp.Bicicleta_edit;
import com.example.pedaleapp.Alumno_principal;
import com.example.pedaleapp.R;
import com.example.pedaleapp.model.Alumno;
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
public class Alumno_bicycle extends Fragment {

    public Alumno_bicycle() {
        // Required empty public constructor
    }

    // --- Elementos del Fragment ---
    private ImageView photo_bicycle, qr_bicycle;
    private TextView txt_rut, txt_marca, txt_modelo, txt_color, txt_aro;
    private Button btn_camera, btn_gallery, btn_edit;
    private Uri uri_profilePhoto;
    // --- ---

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alumno_bycicle, container, false);

        // --- Inicializar los controles ---
        photo_bicycle = view.findViewById(R.id.photo_bicycle_profile);
        btn_camera = view.findViewById(R.id.btn_camera_bicycle_profile);
        btn_gallery = view.findViewById(R.id.btn_gallery_bicycle_profile);
        txt_rut = view.findViewById(R.id.txt_rut_bicycle_profile);
        txt_marca = view.findViewById(R.id.txt_marca_bicycle_profile);
        txt_modelo = view.findViewById(R.id.txt_modelo_bicycle_profile);
        txt_color = view.findViewById(R.id.txt_color_bicycle_profile);
        txt_aro = view.findViewById(R.id.txt_aro_bicycle_profile);
        qr_bicycle = view.findViewById(R.id.qr_bicycle_profile);
        btn_edit = view.findViewById(R.id. btn_bicycle_profile);
        // ---

        // --- Extraer valores ---
        final Alumno_principal activity = (Alumno_principal) getActivity();
        final Alumno alumno = activity.obtenerAlumno();
        if (!alumno.getUrl_bicyclePhoto().contains("NULL")){
            Glide.with(getActivity()).load(alumno.getUrl_bicyclePhoto()).into(photo_bicycle);
        }
        // --- ---

        // --- Usar valores ---
        txt_rut.setText(alumno.getRut());
        txt_marca.setText(alumno.getMarca());
        txt_modelo.setText(alumno.getModelo());
        txt_color.setText(alumno.getColor());
        txt_aro.setText(alumno.getAro());
        // --- ---

        // --- Botón Abrir Cámara ---
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alumno.getMarca().equals("Registra tu bicicleta") || alumno.getMarca().isEmpty() || alumno.getModelo().equals("Registra tu bicicleta") || alumno.getModelo().isEmpty()
                        || alumno.getColor().equals("Registra tu bicicleta") || alumno.getColor().isEmpty() || alumno.getAro().equals("Registra tu bicicleta") || alumno.getAro().isEmpty() ){
                    Toast.makeText(getActivity(),"Debes registrar tu bicicleta primero", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //Intent que permite trabajar con la cámara.
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //Permiso para trabajar con la URI de la imagen.
                    File foto = new File(getActivity().getExternalFilesDir(null), "/test.jpg"); //Crea un archivo Java para guardar la foto, temporal.
                    uri_profilePhoto = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", foto); //Obtiene la URI de la foto, mediante la actividad concatenada con el "provider" del Manifest.
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri_profilePhoto);
                    startActivityForResult(intent, Alumno_principal.CODE_CAMERA_BICICLETA); //Desde Alumno_Principal, obtengo el código que determina usar Cámara.
                }
            }
        });
        // --- ---

        // --- Botón Abrir Galería ---
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alumno.getMarca().equals("Registra tu bicicleta") || alumno.getMarca().isEmpty() || alumno.getModelo().equals("Registra tu bicicleta") || alumno.getModelo().isEmpty()
                        || alumno.getColor().equals("Registra tu bicicleta") || alumno.getColor().isEmpty() || alumno.getAro().equals("Registra tu bicicleta") || alumno.getAro().isEmpty() ){
                    Toast.makeText(getActivity(),"Debes registrar tu bicicleta primero", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //Intent que permite trabajar con elementos externos en base a URI.
                    startActivityForResult(intent, Alumno_principal.CODE_GALLERY_BICICLETA); //Desde Alumno_Principal, obtengo el código que determina usar Galería.
                }
            }
        });
        // --- ---

        // --- Botón Gestionar Bicicleta ---
        btn_edit.setOnClickListener(new View.OnClickListener() { //Método para accionar el botón.
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), Bicicleta_edit.class); //Crea la pantalla de Editar Alumno.
                i.putExtra("Rut", alumno.getRut());
                i.putExtra("Nombre", alumno.getNombre());
                i.putExtra("Apellido", alumno.getApellido());
                i.putExtra("Correo", alumno.getCorreo());
                i.putExtra("Carrera", alumno.getCarrera());
                i.putExtra("Password", alumno.getPassword());
                i.putExtra("Foto_Usuario", alumno.getUrl_userPhoto());
                i.putExtra("Marca", alumno.getMarca());
                i.putExtra("Modelo", alumno.getModelo());
                i.putExtra("Color", alumno.getColor());
                i.putExtra("Aro", alumno.getAro());
                i.putExtra("Foto_Bicicleta", alumno.getUrl_bicyclePhoto());
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
        Alumno_principal activity = (Alumno_principal) getActivity();
        final Alumno alumno = activity.obtenerAlumno();
        // --- ---

        // --- Parámetros de Conexión al Storage ---
        StorageReference storage = FirebaseStorage.getInstance().getReference(); //Inicializa el Storage de Firebase.
        StorageReference folder = storage.child("Alumno_Profile"); //Crea carpeta donde se guardará la imagen.
        StorageReference photo = folder.child(alumno.getRut() + "_Bicycle_Photo"); //Crea el nombre de la imagen.
        // --- ---

        // --- Parámetros de Conexión a la Base de Datos
        FirebaseDatabase database = FirebaseDatabase.getInstance(); //Inicializa la Database Realtime de Firebase.
        final DatabaseReference reference = database.getReference("Alumno"); //Raíz de la BD, es como definir Tabla.
        // --- ---

        switch (requestCode) {
            // --- Caso Usuario accede a la Cámara ---
            case Alumno_principal.CODE_CAMERA_BICICLETA:
                Bitmap bitmapCamera = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/test.jpg");
                photo_bicycle.setImageBitmap(bitmapCamera);

                // --- Proceso de Insert de la imagen ---
                photo.putFile(uri_profilePhoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        Uri downloadUri = uriTask.getResult();

                        alumno.setUrl_bicyclePhoto(downloadUri.toString()); //Ingresa la URL de la Foto al Alumno.
                        reference.child(alumno.getRut()).setValue(alumno); //Actualiza el "child" con el RUT como ID, en la "tabla" Alumno.
                    }
                });
                // --- Fin Proceso de Insert de la imagen ---
                break;
            // --- Fin Caso Usuario accede a la Cámara ---

            // --- Caso Usuario accede a la Galería ---
            case Alumno_principal.CODE_GALLERY_BICICLETA:
                if (data != null) {
                    uri_profilePhoto = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri_profilePhoto);
                        photo_bicycle.setImageBitmap(bitmap);
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

                            alumno.setUrl_bicyclePhoto(downloadUri.toString()); //Ingresa la URL de la Foto al Alumno.
                            reference.child(alumno.getRut()).setValue(alumno); //Actualiza el "child" con el RUT como ID, en la "tabla" Alumno.
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