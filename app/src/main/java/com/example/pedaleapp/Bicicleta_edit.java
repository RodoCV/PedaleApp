package com.example.pedaleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pedaleapp.model.Alumno;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Bicicleta_edit extends AppCompatActivity {
    // --- Elementos del Layout ---
    private ImageView photo_alumno, photo_bicycle;
    private EditText txt_marca, txt_modelo, txt_color, txt_aro;
    // --- ---

    // --- Elementos de los Fragments ---
    String rut, nombre, apellido, correo, carrera, password, user_photo, marca, modelo, color, aro, bicycle_photo;
    // --- ---

    // --- Objetos donde se guardará lo extraído de Firebase BD.
    private Alumno alumnoEdit;
    // --- ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bicicleta_edit);

        // --- Inicializar los controles ---
        txt_marca = findViewById(R.id.txt_marca_alumno_edit);
        txt_modelo = findViewById(R.id.txt_modelo_alumno_edit);
        txt_color = findViewById(R.id.txt_color_alumno_edit);
        txt_aro = findViewById(R.id.txt_aro_alumno_edit);

        rut = getIntent().getStringExtra("Rut");
        nombre = getIntent().getStringExtra("Nombre"); //Rescatando el atributo extra, pasado en LoginActivity.
        apellido = getIntent().getStringExtra("Apellido"); //Rescatando el atributo extra, pasado en LoginActivity.
        correo = getIntent().getStringExtra("Correo"); //Rescatando el atributo extra, pasado en LoginActivity.
        carrera = getIntent().getStringExtra("Carrera"); //Rescatando el atributo extra, pasado en LoginActivity.
        password = getIntent().getStringExtra("Password"); //Rescatando el atributo extra, pasado en LoginActivity.
        user_photo = getIntent().getStringExtra("Foto_Usuario"); //Rescatando el atributo extra, pasado en LoginActivity.
        marca = getIntent().getStringExtra("Marca"); //Rescatando el atributo extra, pasado en LoginActivity.
        modelo = getIntent().getStringExtra("Modelo"); //Rescatando el atributo extra, pasado en LoginActivity.
        color = getIntent().getStringExtra("Color"); //Rescatando el atributo extra, pasado en LoginActivity.
        aro = getIntent().getStringExtra("Aro"); //Rescatando el atributo extra, pasado en LoginActivity.
        bicycle_photo = getIntent().getStringExtra("Foto_Bicicleta"); //Rescatando el atributo extra, pasado en LoginActivity.


        alumnoEdit = new Alumno(rut, nombre, apellido, correo, carrera, password, user_photo, marca, modelo, color, aro, bicycle_photo); //Objeto cargado para mostrar información.
        if (!marca.contains("Registra tu bicicleta") && modelo.contains("Registra tu bicicleta") && color.contains("Registra tu bicicleta") && aro.contains("Registra tu bicicleta")){
            txt_marca.setText(marca);
            txt_modelo.setText(modelo);
            txt_color.setText(color);
            txt_aro.setText(aro);
        }
        // --- ---
    }

    public void editAlumno(View view) {
        // --- Extraer valores para validaciones ---
        String marcaEdit = txt_marca.getText().toString();
        String modeloEdit = txt_modelo.getText().toString();
        String colorEdit = txt_color.getText().toString();
        String aroEdit = txt_aro.getText().toString();
        // --- ---

        // --- Inicio de validaciones ---
        if (marcaEdit.equals("Registra tu bicicleta") || marcaEdit.isEmpty()){
            Toast.makeText(this,"Debe ingresar la Marca de su Bicicleta", Toast.LENGTH_SHORT).show();
        } else if (modeloEdit.equals("Registra tu bicicleta") || modeloEdit.isEmpty()){
            Toast.makeText(this,"Debe ingresar el Modelo de su Bicicleta", Toast.LENGTH_SHORT).show();
        } else if (colorEdit.equals("Registra tu bicicleta") || colorEdit.isEmpty()){
            Toast.makeText(this,"Debe ingresar el Color de su Bicicleta", Toast.LENGTH_SHORT).show();
        } else if (aroEdit.equals("Registra tu bicicleta") || aroEdit.isEmpty()){
            Toast.makeText(this,"Debe ingresar el Aro de su Bicicleta", Toast.LENGTH_SHORT).show();
        }
        // --- ---

        // --- Proceso de Edición ---
        else {
            // --- Parámetros de Conexión a la Base de Datos ---
            FirebaseDatabase database = FirebaseDatabase.getInstance(); //Inicializa la Database Realtime de Firebase.
            DatabaseReference reference = database.getReference("Alumno"); //Raíz de la BD, es como definir Tabla.
            Query query = reference.orderByChild("rut").equalTo(rut); //Consulta a la Base de Datos en base a la Referencia, Tabla y un Atributo a comparar.
            // --- ---

            // --- Armado de objeto editado ---
            alumnoEdit.setMarca(marcaEdit);
            alumnoEdit.setModelo(modeloEdit);
            alumnoEdit.setColor(colorEdit);
            alumnoEdit.setAro(aroEdit);
            // --- ---

            reference.child(alumnoEdit.getRut()).setValue(alumnoEdit); //Crea un nuevo "child" con el RUT como ID, en la "tabla" Alumno.

            // --- Mensaje de Confirmación y vuelta a Pantalla Principal ---
            Toast.makeText(this,"Tu Bicicleta se registró correctamente.", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, Alumno_principal.class);
            i.putExtra("Rut", alumnoEdit.getRut());
            i.putExtra("Nombre", alumnoEdit.getNombre());
            i.putExtra("Apellido", alumnoEdit.getApellido());
            i.putExtra("Correo", alumnoEdit.getCorreo());
            i.putExtra("Carrera", alumnoEdit.getCarrera());
            i.putExtra("Password", alumnoEdit.getPassword());
            i.putExtra("Foto_Usuario", alumnoEdit.getUrl_userPhoto());
            i.putExtra("Marca", alumnoEdit.getMarca());
            i.putExtra("Modelo", alumnoEdit.getModelo());
            i.putExtra("Color", alumnoEdit.getColor());
            i.putExtra("Aro", alumnoEdit.getAro());
            i.putExtra("Foto_Bicicleta", alumnoEdit.getUrl_bicyclePhoto());
            startActivity(i);
            finish();
        }
    }
}