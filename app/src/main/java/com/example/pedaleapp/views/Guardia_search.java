package com.example.pedaleapp.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pedaleapp.Guardia_principal;
import com.example.pedaleapp.R;
import com.example.pedaleapp.model.Alumno;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class Guardia_search extends Fragment {

    public Guardia_search() {
        // Required empty public constructor
    }

    // --- Elementos del Layout ---
    private EditText txt_rut;
    private Button btn_search;
    private ImageView photo_bicycle, photo_alum;
    private TextView txt_rut_search, txt_marca_search, txt_modelo_search, txt_color_search, txt_aro_search;
    private TextView txt_nombre_search, txt_apellido_search, txt_correo_search, txt_carrera_search;
    // --- ---

    // --- Extraer valores ---
    final Guardia_principal activity = (Guardia_principal) getActivity();
    // --- ---

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guardia_search, container, false);;

        // --- Inicializar los controles ---
        btn_search = view.findViewById(R.id.btn_guard_search);
        txt_rut = view.findViewById(R.id.txt_rut_alumn_guard_search);

        photo_bicycle = view.findViewById(R.id.photo_bicycle_guard_search);
        txt_rut_search = view.findViewById(R.id.txt_rut_guard_search);
        txt_marca_search = view.findViewById(R.id.txt_marca_guard_search);
        txt_modelo_search = view.findViewById(R.id.txt_modelo_guard_search);
        txt_color_search = view.findViewById(R.id.txt_color_guard_search);
        txt_aro_search = view.findViewById(R.id.txt_aro_guard_search);
        photo_alum = view.findViewById(R.id.photo_alum_guard_search);
        txt_nombre_search = view.findViewById(R.id.txt_nombre_guard_search);
        txt_apellido_search = view.findViewById(R.id.txt_apellido_guard_search);
        txt_correo_search = view.findViewById(R.id.txt_correo_guard_search);
        txt_carrera_search = view.findViewById(R.id.txt_carrera_guard_search);
        // --- ---

        // --- Botón Buscar por Rut ---
        btn_search.setOnClickListener(new View.OnClickListener() { //Método para accionar el botón.
            @Override
            public void onClick(View v) {
                String rut = txt_rut.getText().toString().trim(); //Captura el valor ingresado.
                if (rut.isEmpty()){
                    Toast.makeText(activity,"Debe ingresar un RUT.", Toast.LENGTH_SHORT).show();
                } else {
                    busquedaRut(rut);
                }
            }
        });
        // --- ---

        // Inflate the layout for this fragment
        return view;
    }

    // --- Función que realiza la búsqueda en la BD y muestra resultado en el Fragment ---
    private void busquedaRut (String rut){
        // --- Parámetros de Conexión a la Base de Datos ---
        FirebaseDatabase database = FirebaseDatabase.getInstance(); //Inicializa la Database Realtime de Firebase.
        DatabaseReference reference = database.getReference("Alumno"); //Raíz de la BD, es como definir Tabla.
        Query query = reference.orderByChild("rut").equalTo(rut); //Consulta a la Base de Datos en base a la Referencia, Tabla y un Atributo a comparar.
        // --- ---

        // --- Búsqueda en la Base de Datos ---
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            Alumno alumno = new Alumno ("NO ENCONTRADO", "NO ENCONTRADO", "NO ENCONTRADO", "NO ENCONTRADO", "NO ENCONTRADO", "NO ENCONTRADO", "NO ENCONTRADO", "NO ENCONTRADO", "NO ENCONTRADO", "NO ENCONTRADO", "NO ENCONTRADO", "NO ENCONTRADO");

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    alumno = ds.getValue(Alumno.class);
                }

                // --- Arrojar resultados en el Fragment ---
                txt_rut_search.setText(alumno.getRut());
                txt_marca_search.setText(alumno.getMarca());
                txt_modelo_search.setText(alumno.getModelo());
                txt_color_search.setText(alumno.getColor());
                txt_aro_search.setText(alumno.getAro());
                txt_nombre_search.setText(alumno.getNombre());
                txt_apellido_search.setText(alumno.getApellido());
                txt_correo_search.setText(alumno.getCorreo());
                txt_carrera_search.setText(alumno.getCarrera());

                if (!alumno.getUrl_userPhoto().contains("NULL")){
                    Glide.with(getActivity()).load(alumno.getUrl_userPhoto()).into(photo_alum);
                }

                if (!alumno.getUrl_bicyclePhoto().contains("NULL")){
                    Glide.with(getActivity()).load(alumno.getUrl_bicyclePhoto()).into(photo_bicycle);
                }
                // --- ---

                Toast.makeText(getActivity(),"Búsqueda terminada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Listener -> onCancelled", "---------- No se encontró Usuario o Error de BD ----------");
            }
        });
        // --- Fin Búsquda en la Base de Datos ---
    } // --- ---
}