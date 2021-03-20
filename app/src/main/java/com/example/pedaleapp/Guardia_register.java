package com.example.pedaleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pedaleapp.function.Functions;
import com.example.pedaleapp.model.Guardia;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Guardia_register extends AppCompatActivity {
    // --- Elementos del Layout ---
    private EditText txt_rut, txt_nombre, txt_apellido, txt_mail, txt_password1, txt_password2;
    // --- ---

    // --- Elementos de Firebase ---
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ProgressDialog progressDialog;
    // --- ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardia_register);

        // --- Inicializar los controles ---
        txt_rut = findViewById(R.id.txt_guardia_rut); //Va a buscar el valor al objeto según ID.
        txt_nombre = findViewById(R.id.txt_guardia_nombre); //Va a buscar el valor al objeto según ID.
        txt_apellido = findViewById(R.id.txt_guardia_apellido); //Va a buscar el valor al objeto según ID.
        txt_mail = findViewById(R.id.txt_guardia_mail); //Va a buscar el valor al objeto según ID.
        txt_password1 = findViewById(R.id.txt_guardia_password1); //Va a buscar el valor al objeto según ID.
        txt_password2 = findViewById(R.id.txt_guardia_password2); //Va a buscar el valor al objeto según ID.
        // --- ---

        // --- Inicializar Elementos de Firebase ---
        firebaseAuth = FirebaseAuth.getInstance(); //Inicializa la Autenticación de Firebase.
        database = FirebaseDatabase.getInstance(); //Inicializa la Database Realtime de Firebase.
        reference = database.getReference("Guardia");

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        // --- ---

    }

    public void registrarGuardia(View view) {
        // --- Extraer valores para validaciones ---
        final String rut = txt_rut.getText().toString().toLowerCase().trim(); //Extrae valor desde el elemento del Layout.
        final String nombre = txt_nombre.getText().toString(); //Extrae valor desde el elemento del Layout.
        final String apellido = txt_apellido.getText().toString(); //Extrae valor desde el elemento del Layout.
        final String mail = txt_mail.getText().toString().toLowerCase().trim(); //Extrae valor desde el elemento del Layout.
        final String password1 = txt_password1.getText().toString(); //Extrae valor desde el elemento del Layout.
        String password2 = txt_password2.getText().toString(); //Extrae valor desde el elemento del Layout.
        // --- ---

        // --- Inicio de validaciones ---
        // --- Validar Campos vacíos ---
        if (rut.isEmpty()) {
            Toast.makeText(this, "Debe ingresar su RUT.", Toast.LENGTH_SHORT).show();
        } else if (nombre.isEmpty()) {
            Toast.makeText(this, "Debe ingresar su Nombre.", Toast.LENGTH_SHORT).show();
        } else if (apellido.isEmpty()) {
            Toast.makeText(this, "Debe ingresar su Apellido.", Toast.LENGTH_SHORT).show();
        } else if (mail.isEmpty()) {
            Toast.makeText(this, "Debe ingresar su Correo Electrónico INACAP.", Toast.LENGTH_SHORT).show();
        } else if (password1.isEmpty()) {
            Toast.makeText(this, "Debe ingresar su Contraseña.", Toast.LENGTH_SHORT).show();
        } else if (password2.isEmpty()) {
            Toast.makeText(this, "Debe ingresar su Contraseña confirmada.", Toast.LENGTH_SHORT).show();
        }
        // --- ---

        // --- Validar Campos Erróneos ---
        else {
            Functions f = new Functions(); //Importa la clase donde se guardan funciones.
            boolean validacionRut = f.validarRut(rut); //Llama a la función "validarRut" y guarda su resultado.

            if (validacionRut == false) { //Si el RUT es inválido.
                Toast.makeText(this, "El RUT ingresado no es correcto.", Toast.LENGTH_SHORT).show();
            } else if (!rut.contains("-")) {
                Toast.makeText(this, "El RUT ingresado debe tener el guión '-'", Toast.LENGTH_SHORT).show();
            } else if (!mail.contains("inacap")) {//Si el mail es corporativo de INACAP.
                Toast.makeText(this, "El Correo Electrónico debe ser corporativo de INACAP y/o debe ser tuyo (nombre.apellido@inacap...).", Toast.LENGTH_SHORT).show();
            } else if (!password1.equals(password2)) { //Si las contraseñas no coinciden.
                Toast.makeText(this, "Las Contraseñas no coinciden, por favor intente nuevamente.", Toast.LENGTH_SHORT).show();
            } else if (password1.length() < 6){
                Toast.makeText(this,"La Contraseña debe tener mínimo 6 caractéres.", Toast.LENGTH_SHORT).show();
            }
            // --- ---
            // --- Fin de Validaciones ---

            // --- Proceso de Registro de Usuario ---
            else {
                progressDialog.setMessage("Registrando al Usuario ..."); //Mensaje de la Barra de Progreso.
                progressDialog.show(); //Mostrar Barra de Progreso.

                firebaseAuth.createUserWithEmailAndPassword(mail, password1)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                boolean registroCorrecto = false;
                                if(task.isSuccessful()){ //Si la tarea fue realizada. Se realiza el registro.
                                    Guardia guardia = new Guardia(rut, nombre, apellido, mail, password1, "NULL");
                                    reference.child(rut).setValue(guardia); //Crea un nuevo "child" con el RUT como ID, en la "tabla" Alumno.
                                    Toast.makeText(getApplicationContext(),"Te has registrado correctamente.", Toast.LENGTH_SHORT).show();
                                    registroCorrecto = true;
                                }else{ //Si ocurre un error.
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException){ //Si el Usuario ya está registado.
                                        Toast.makeText(getApplicationContext(),"El Guardia ya existe", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Error en el Registro.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                progressDialog.dismiss(); //Cerrar Barra de Progreso.

                                // --- Si el Usuario se registró, se devolverá a la Pantalla de Login ---
                                if (registroCorrecto == true){
                                    onBackPressed();
                                    finish();
                                }
                                // --- ---
                            }
                        });

            }
            // --- Fin Proceso de Registro de Usuario ---
        }
    } // Fin función registrarGuardia().
}