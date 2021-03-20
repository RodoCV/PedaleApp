package com.example.pedaleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pedaleapp.function.Functions;
import com.example.pedaleapp.model.Alumno;
import com.example.pedaleapp.model.Guardia;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    // --- Elementos del Layout ---
    private Spinner spinner_typeUser;
    private EditText txt_id, txt_password;
    private CheckBox checkbox_sesion;
    private SharedPreferences usuarioBuscado; //Guarda información de la búsqueda de usuario, solo en el proceso de Login.
    // --- ---

    // --- Elementos de Firebase ---
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Query query; //Consulta a la Base de Datos en base a la Referencia, Tabla y un Atributo a comparar.
    // --- ---

    // --- Contenido del spinner_typeUser ---
    private String[] typeUserArray = {
            "Seleccione su Tipo de Cuenta",
            "Alumno",
            "Guardia",
    };
    // --- ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // --- Inicializar los controles ---
        spinner_typeUser = findViewById(R.id.spinner_typeUser); //Va a buscar el valor al objeto según ID.
        txt_id = findViewById(R.id.txt_login_id); //Va a buscar el valor al objeto según ID.
        txt_password = findViewById(R.id.txt_login_password); //Va a buscar el valor al objeto según ID.
        checkbox_sesion = findViewById(R.id.checkbox_sesion); //Va a buscar el valor al objeto según ID.
        // --- ---

        // --- Trabajando con SharedPreferences, para extraer valores de la Consulta a la Base de Datos ---
        usuarioBuscado = getSharedPreferences("Login", Context.MODE_PRIVATE); //Nombre de la "BD" y el modo de trabajo.
        SharedPreferences.Editor editor = usuarioBuscado.edit(); //Mediante este objeto, se edita el SharedPreferences.
        editor.clear(); //Limpia el SharedPreferences.
        editor.commit();
        // --- ---

        // --- Inicializar Elementos de Firebase ---
        firebaseAuth = FirebaseAuth.getInstance(); //Inicializa la Autenticación de Firebase.
        database = FirebaseDatabase.getInstance(); //Inicializa la Database Realtime de Firebase.
        // --- ---

        // --- Objetos donde se guardará lo extraído de Firebase BD.
        final Alumno alumnoLog = new Alumno();
        final Guardia guardiaLog = new Guardia();
        // --- ---

        // --- Rellenar Spinner Principal (spinner_typeUser) ---
        ArrayAdapter<String> typeUserAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, typeUserArray); //Adapter para relacionar Vector con Spinner.
        spinner_typeUser.setAdapter(typeUserAdapter); //Spinner + ArrayAdapter = Vector asociado.
        // --- ---
    }

    public void login(View view) { //Evento onClick() del Botón "Iniciar Sesión"
        final Functions functions = new Functions();

        // --- Extraer valores para validaciones ---
        int index_spinner = spinner_typeUser.getSelectedItemPosition();
        final String id = txt_id.getText().toString().trim(); //Extrae valor desde el elemento del Layout.
        final String password = txt_password.getText().toString(); //Extrae valor desde el elemento del Layout.
        Boolean checkbox = checkbox_sesion.isChecked(); //Extrae valor desde el elemento del Layout.
        // --- ---

        // --- Objetos donde se guardará lo extraído de Firebase BD.
        final Alumno alumnoLog = new Alumno();
        final Guardia guardiaLog = new Guardia();
        // --- ---

        // --- Validar campos vacíos y determinar Tipo de Usuario a consultar en la Base de Datos ---

        // --- Validar campos vacíos ----
        if (index_spinner == 0) {
            Toast.makeText(this, "Debe seleccionar su Tipo de Cuenta.", Toast.LENGTH_SHORT).show();
        } else if (id.isEmpty()) {
            Toast.makeText(this, "Debe ingresar su RUT o Correo Electrónico Institucional.", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Debe ingresar su Contraseña.", Toast.LENGTH_SHORT).show();
        }
        // --- Fin Validar campos vaciós ----

        // --- Proceso de Login ---
        else {
            // --- Tipo de Usuario Alumno ---
            if (index_spinner == 1) {
                // --- Completando parámetros de Conexion a la Base de Datos ---
                reference = database.getReference(spinner_typeUser.getSelectedItem().toString());
                query = functions.armarQuery(id, reference);
                // --- ---

                // --- Búsqueda en la Base de Datos ---
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    Alumno alumno;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        SharedPreferences.Editor editor = usuarioBuscado.edit(); //Mediante este objeto, se edita el SharedPreferences.

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            alumno = ds.getValue(Alumno.class);
                            editor.putString("Rut_Alumno", alumno.getRut());
                            editor.putString("Nombre_Alumno", alumno.getNombre());
                            editor.putString("Apellido_Alumno", alumno.getApellido());
                            editor.putString("Correo_Alumno", alumno.getCorreo());
                            editor.putString("Carrera_Alumno", alumno.getCarrera());
                            editor.putString("Password_Alumno", alumno.getPassword());
                            editor.putString("Foto_Alumno", alumno.getUrl_userPhoto());
                            editor.putString("Marca_Bicicleta", alumno.getMarca());
                            editor.putString("Modelo_Bicicleta", alumno.getModelo());
                            editor.putString("Color_Bicicleta", alumno.getColor());
                            editor.putString("Aro_Bicicleta", alumno.getAro());
                            editor.putString("Foto_Bicicleta", alumno.getUrl_bicyclePhoto());
                            editor.commit();
                        }

                        // --- Proceso de Login ---
                        alumnoLog.setRut(usuarioBuscado.getString("Rut_Alumno", "NULL")); // Busca el valor según el índice, sino encuentra nada, setea el valor.
                        alumnoLog.setNombre(usuarioBuscado.getString("Nombre_Alumno", "NULL")); // Busca el valor según el índice, sino encuentra nada, setea el valor.
                        alumnoLog.setApellido(usuarioBuscado.getString("Apellido_Alumno", "NULL")); // Busca el valor según el índice, sino encuentra nada, setea el valor.
                        alumnoLog.setCorreo(usuarioBuscado.getString("Correo_Alumno", "NULL")); // Busca el valor según el índice, sino encuentra nada, setea el valor.
                        alumnoLog.setCarrera(usuarioBuscado.getString("Carrera_Alumno", "NULL")); // Busca el valor según el índice, sino encuentra nada, setea el valor.
                        alumnoLog.setPassword(usuarioBuscado.getString("Password_Alumno", "NULL")); // Busca el valor según el índice, sino encuentra nada, setea el valor.
                        alumnoLog.setUrl_userPhoto(usuarioBuscado.getString("Foto_Alumno", "NULL")); // Busca el valor según el índice, sino encuentra nada, setea el valor.
                        alumnoLog.setMarca(usuarioBuscado.getString("Marca_Bicicleta", "NULL")); // Busca el valor según el índice, sino encuentra nada, setea el valor.
                        alumnoLog.setModelo(usuarioBuscado.getString("Modelo_Bicicleta", "NULL")); // Busca el valor según el índice, sino encuentra nada, setea el valor.
                        alumnoLog.setColor(usuarioBuscado.getString("Color_Bicicleta", "NULL")); // Busca el valor según el índice, sino encuentra nada, setea el valor.
                        alumnoLog.setAro(usuarioBuscado.getString("Aro_Bicicleta", "NULL")); // Busca el valor según el índice, sino encuentra nada, setea el valor.
                        alumnoLog.setUrl_bicyclePhoto(usuarioBuscado.getString("Foto_Bicicleta", "NULL")); // Busca el valor según el índice, sino encuentra nada, setea el valor.

                        boolean loginCorrecto = functions.permisoLogin(id, password, alumnoLog.getRut(), alumnoLog.getCorreo(), alumnoLog.getPassword()); //Determina si los datos son correctos para Login.

                        if (loginCorrecto == false) { //Si los Datos de Login son incorrectos.
                            Toast.makeText(LoginActivity.this, "Los datos ingresados son inválidos", Toast.LENGTH_SHORT).show();
                            editor = usuarioBuscado.edit(); //Mediante este objeto, se edita el SharedPreferences.
                            editor.clear(); //Limpia el SharedPreferences.
                            editor.commit();
                        } else { //Si el Usuario puede hacer Login.
                            firebaseAuth.signInWithEmailAndPassword(alumnoLog.getCorreo(), password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()){
                                                Intent i = new Intent(LoginActivity.this, Alumno_principal.class); //Crea la Pantalla de Tipo de Registro.
                                                i.putExtra("Rut", alumnoLog.getRut());
                                                i.putExtra("Nombre", alumnoLog.getNombre());
                                                i.putExtra("Apellido", alumnoLog.getApellido());
                                                i.putExtra("Correo", alumnoLog.getCorreo());
                                                i.putExtra("Carrera", alumnoLog.getCarrera());
                                                i.putExtra("Password", alumnoLog.getPassword());
                                                i.putExtra("Foto_Usuario", alumnoLog.getUrl_userPhoto());
                                                i.putExtra("Marca", alumnoLog.getMarca());
                                                i.putExtra("Modelo", alumnoLog.getModelo());
                                                i.putExtra("Color", alumnoLog.getColor());
                                                i.putExtra("Aro", alumnoLog.getAro());
                                                i.putExtra("Foto_Bicicleta", alumnoLog.getUrl_bicyclePhoto());
                                                SharedPreferences.Editor editor = usuarioBuscado.edit(); //Mediante este objeto, se edita el SharedPreferences.
                                                editor = usuarioBuscado.edit(); //Mediante este objeto, se edita el SharedPreferences.
                                                editor.clear(); //Limpia el SharedPreferences.
                                                editor.commit();
                                                startActivity(i); //Muestra la pantalla creada.
                                                finish(); //Cierra la Pantalla actual.
                                            }
                                        }
                                    });
                        }
                        // --- Fin Proceso de Login ---
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Listener -> onCancelled", "---------- No se encontró Usuario o Error de BD ----------");
                    }
                });
                // --- Fin Búsqueda en la Base de Datos---

                // --- Fin Tipo de Usuario Alumno ---

                // --- Tipo de Usuario Guardia ---
            } else if (index_spinner == 2) {
                // --- Completando parámetros de Conexion a la Base de Datos ---
                reference = database.getReference(spinner_typeUser.getSelectedItem().toString());
                query = functions.armarQuery(id, reference);
                // --- ---

                // --- Búsqueda en la Base de Datos ---
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    Guardia guardia;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        SharedPreferences.Editor editor = usuarioBuscado.edit(); //Mediante este objeto, se edita el SharedPreferences.

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            guardia = ds.getValue(Guardia.class);
                            editor.putString("Rut_Guardia", guardia.getRut());
                            editor.putString("Nombre_Guardia", guardia.getNombre());
                            editor.putString("Apellido_Guardia", guardia.getApellido());
                            editor.putString("Correo_Guardia", guardia.getCorreo());
                            editor.putString("Password_Guardia", guardia.getPassword());
                            editor.putString("Foto_Guardia", guardia.getUrl_userPhoto());
                            editor.commit();
                        }

                        // --- Proceso de Login ---
                        guardiaLog.setRut(usuarioBuscado.getString("Rut_Guardia", "NULL")); // Busca el valor según el índice, sino encuentra nada, setea el valor.
                        guardiaLog.setNombre(usuarioBuscado.getString("Nombre_Guardia", "NULL")); // Busca el valor según el índice, sino encuentra nada, setea el valor.
                        guardiaLog.setApellido(usuarioBuscado.getString("Apellido_Guardia", "NULL")); // Busca el valor según el índice, sino encuentra nada, setea el valor.
                        guardiaLog.setCorreo(usuarioBuscado.getString("Correo_Guardia", "NULL")); // Busca el valor según el índice, sino encuentra nada, setea el valor.
                        guardiaLog.setPassword(usuarioBuscado.getString("Password_Guardia", "NULL")); // Busca el valor según el índice, sino encuentra nada, setea el valor.
                        guardiaLog.setUrl_userPhoto(usuarioBuscado.getString("Foto_Guardia", "NULL")); // Busca el valor según el índice, sino encuentra nada, setea el valor.

                        boolean loginCorrecto = functions.permisoLogin(id, password, guardiaLog.getRut(), guardiaLog.getCorreo(), guardiaLog.getPassword()); //Determina si los datos son correctos para Login.

                        if (loginCorrecto == false) { //Si los Datos de Login son incorrectos.
                            Toast.makeText(LoginActivity.this, "Los datos ingresados son inválidos", Toast.LENGTH_SHORT).show();
                            editor = usuarioBuscado.edit(); //Mediante este objeto, se edita el SharedPreferences.
                            editor.clear(); //Limpia el SharedPreferences.
                            editor.commit();
                        } else { //Si el Usuario puede hacer Login.
                            firebaseAuth.signInWithEmailAndPassword(guardiaLog.getCorreo(), password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()){
                                                Intent i = new Intent(LoginActivity.this, Guardia_principal.class); //Crea la Pantalla de Tipo de Registro.
                                                i.putExtra("Rut", guardiaLog.getRut());
                                                i.putExtra("Nombre", guardiaLog.getNombre());
                                                i.putExtra("Apellido", guardiaLog.getApellido());
                                                i.putExtra("Correo", guardiaLog.getCorreo());
                                                i.putExtra("Password", guardiaLog.getPassword());
                                                i.putExtra("Foto_Usuario", guardiaLog.getUrl_userPhoto());
                                                SharedPreferences.Editor editor = usuarioBuscado.edit(); //Mediante este objeto, se edita el SharedPreferences.
                                                editor = usuarioBuscado.edit(); //Mediante este objeto, se edita el SharedPreferences.
                                                editor.clear(); //Limpia el SharedPreferences.
                                                editor.commit();
                                                startActivity(i); //Muestra la pantalla creada.
                                                finish(); //Cierra la Pantalla actual.
                                            }
                                        }
                                    });
                        }
                        // --- Fin Proceso de Login ---
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Listener -> onCancelled", "---------- No se encontró Usuario o Error de BD ----------");
                    }
                });
                // --- Fin Búsqueda en la Base de Datos---
            }
            // --- Fin Tipo de Usuario Guardia---
        }
        // --- Fin Validar campos vacíos y determinar Tipo de Usuario a consultar en la Base de Datos ---
    }

    public void goAbout(View view) {
        Intent i = new Intent(this, About_PedaleApp.class); //Crea la Pantalla de Tipo de Registro.
        startActivity(i); //Muestra la pantalla creada.
        finish();
    }

    public void goRegister(View view) { //Ir a la Pantalla de Registro
        Intent i = new Intent(this, Register_type.class); //Crea la Pantalla de Tipo de Registro.
        startActivity(i); //Muestra la pantalla creada.
    }
}