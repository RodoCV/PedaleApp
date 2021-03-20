package com.example.pedaleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pedaleapp.function.Functions;
import com.example.pedaleapp.model.Alumno;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Alumno_register extends AppCompatActivity {
    // --- Elementos del Layout ---
    private EditText txt_rut, txt_nombre, txt_apellido, txt_mail, txt_password1, txt_password2;
    private Spinner spinner_area, spinner_carrera;
    // --- ---

    // --- Elementos de Firebase ---
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ProgressDialog progressDialog;
    // --- ---

    // --- Contenido del spinner_area ---
    private String [] areas = {
            "Selecciona Área de Carrera", //Case 0
            "Administración y Negocios", //Case 1
            "Agropecuaria y Agroindustrial", //Case 2
            "Construcción", //Case 3
            "Diseño & Comunicación", //Case 4
            "Electricidad y Electrónica", //Case 5
            "Hotelería, Turismo y Gastronomía", //Case 6
            "Humanidades y Educación", //Case 7
            "Informática y Telecomunicaciones", //Case 8
            "Mecánica", //Case 9
            "Minería y Metalurgia", //Case 10
            "Procesos Industriales", //Case 11
            "Salud" //Case 12
    };
    // --- ---

    // --- Posible contenido del spinner_carrera ---
    private String [] carrera = {"Seleccione una Carrera"}; //Case 0

    private String [] admin = { //Case 1
            "Seleccione una Carrera",
            "Téc. en Administración de Empresas",
            "Téc. en Comercio Exterior",
            "Contabilidad General",
            "Ing. en Administración de Empresas",
            "Ing. en Comercio Exterior",
            "Contador Auditor"
    };

    private String [] agro = { //Case 2
            "Seleccione una Carrera",
            "Téc. en Tecnología Agrícola",
            "Téc. en Análisis Químico",
            "Téc. en Producción Ganadera",
            "Ingeniería Agrícola",
            "Ing. en Química Industrial",
            "Ing. en Producción Ganadera"
    };

    private String [] constru = { //Case 3
            "Seleccione una Carrera",
            "Téc. en Edificación",
            "Téc. en Fabricación y Montaje Industrial",
            "Téc. en Topografía",
            "Construcción Civil",
            "Ing. en Geomesura"
    };

    private String [] diseno = { //Case 4
            "Seleccione una Carrera",
            "Téc. en Diseño y Producción de Moda",
            "Téc. en Diseño Web y Mobile",
            "Téc. en Diseño y Producción Gráfica",
            "Diseño Gráfico Profesional",
            "Diseño de Moda"
    };

    private String [] electr = { //Case 5
            "Seleccione una Carrera",
            "Téc. en Automatización y Control Industrial",
            "Téc. en Electricidad Industrial (Instalaciones Eléctricas)",
            "Téc. en Electromecánica",
            "Téc. en Electrónica Industrial",
            "Téc. en Sonido",
            "Ing. en Automatización y Control Industrial",
            "Ingeniería Electríca",
            "Ing. en Electrónica y Sistemas Inteligentes",
            "Ing. en Sonido"
    };

    private String [] hotel = { //Case 6
            "Seleccione una Carrera",
            "Téc. en Gastronomía Internacional",
            "Téc. en Hotelería y Servicios",
            "Téc. en Turismo",
            "Administrador Gastronómico Internacional",
            "Administración en Hotelería y Servicio",
            "Administración Turística Internacional"
    };

    private String [] human = { //Case 7
            "Seleccione una Carrera",
            "Pedagogía de Ed. Media (Artes Musicales)",
            "Psicopedagogía",
            "Trabajo Social"
    };

    private String [] inform = { //Case 8
            "Seleccione una Carrera",
            "Téc. Analista Programador",
            "Téc. en Telecomunicaciones, Conectividad y Redes",
            "Ingeniería Informática",
            "Ing. en Telecomunicaciones, Conectividad y Redes"
    };

    private String [] mecanic = { //Case 9
            "Seleccione una Carrera",
            "Téc. en Climatización",
            "Téc. en Refrigeración",
            "Téc. en Mantención Industrial",
            "Téc. en Mantenimiento Mecánico de Plantas Mineras",
            "Téc. Mecánico Automotriz en Maquinaria Pesada",
            "Téc. Mecánico Automotriz en Sistemas Electrónicos",
            "Téc. Mecánico en Producción Industrial",
            "Ing. en Climatización",
            "Ing. en Refrigeración",
            "Ing. Mecánica en Mantenimiento Industrial",
            "Ing. Mecánica en Producción Industrial",
            "Ing. en Maquinaria Pesada y Vehículos Automotrices",
            "Ing. en Maquinaria, Vehículos Automotrices y Sistemas Electrónicos"
    };

    private String [] mineria = { //Case 10
            "Seleccione una Carrera",
            "Téc. en Metalurgia Extractiva",
            "Téc. en Minería",
            "Ing. en Metalurgia",
            "Ing. en Minas"
    };

    private String [] industr = { //Case 11
            "Seleccione una Carrera",
            "Téc. en Logística y Operaciones Industriales",
            "Téc. en Prevención de Riesgos",
            "Ing. en Prevención de Riesgos, Calidad y Ambiente",
            "Ingeniería Industrial"
    };

    private String [] salud = { //Case 12
            "Seleccione una Carrera",
            "Téc. en Lab. Clínico, Banco de Sangre e Imagenología",
            "Téc. en Enfermería",
            "Téc. en Farmacia",
            "Téc. en Odontología",
            "Enfermería",
            "Kinesiología",
            "Nutrición y Dietética"
    };
    // --- ---

    // --- Objetos donde se guardará lo extraído de Firebase BD.
    private Alumno alumnoReg = new Alumno();
    // --- ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_register);

        // --- Inicializar los controles ---
        txt_rut = findViewById(R.id.txt_alumno_rut); //Va a buscar el valor al objeto según ID.
        txt_nombre = findViewById(R.id.txt_alumno_nombre); //Va a buscar el valor al objeto según ID.
        txt_apellido = findViewById(R.id.txt_alumno_apellido); //Va a buscar el valor al objeto según ID.
        txt_mail = findViewById(R.id.txt_alumno_mail); //Va a buscar el valor al objeto según ID.
        txt_password1 = findViewById(R.id.txt_alumno_password1); //Va a buscar el valor al objeto según ID.
        txt_password2 = findViewById(R.id.txt_alumno_password2); //Va a buscar el valor al objeto según ID.
        spinner_area = findViewById(R.id.spinner_area); //Va a buscar el valor al objeto según ID.
        spinner_carrera = findViewById(R.id.spinner_carrera); //Va a buscar el valor al objeto según ID.
        spinner_carrera.setEnabled(false);
        // --- ---

        // --- Inicializar Elementos de Firebase ---
        firebaseAuth = FirebaseAuth.getInstance(); //Inicializa la Autenticación de Firebase.
        database = FirebaseDatabase.getInstance(); //Inicializa la Database Realtime de Firebase.
        reference = database.getReference("Alumno");

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        // --- ---

        // --- Rellenar Spinner Principal (spinner_area) ---
        ArrayAdapter <String> areasAdapter = new ArrayAdapter <String>(this, R.layout.support_simple_spinner_dropdown_item, areas); //Adapter para relacionar Vector con Spinner.
        spinner_area.setAdapter(areasAdapter); //Spinner + ArrayAdapter = Vector asociado.
        // --- ---

        // --- Posibles ArrayAdapter del Spinner Cambiante (spinner_carrera). Adapters para relacionar Vector con Spinner ---
        final ArrayAdapter <String> carreraAdapter = new ArrayAdapter <String>(this, R.layout.support_simple_spinner_dropdown_item, carrera); //Adapter para relacionar Vector con Spinner.
        final ArrayAdapter <String> adminAdapter = new ArrayAdapter <String>(this, R.layout.support_simple_spinner_dropdown_item, admin); //Adapter para relacionar Vector con Spinner.
        final ArrayAdapter <String> agroAdapter = new ArrayAdapter <String>(this, R.layout.support_simple_spinner_dropdown_item, agro); //Adapter para relacionar Vector con Spinner.
        final ArrayAdapter <String> construAdapter = new ArrayAdapter <String>(this, R.layout.support_simple_spinner_dropdown_item, constru); //Adapter para relacionar Vector con Spinner.
        final ArrayAdapter <String> disenoAdapter = new ArrayAdapter <String>(this, R.layout.support_simple_spinner_dropdown_item, diseno); //Adapter para relacionar Vector con Spinner.
        final ArrayAdapter <String> electrAdapter = new ArrayAdapter <String>(this, R.layout.support_simple_spinner_dropdown_item, electr); //Adapter para relacionar Vector con Spinner.
        final ArrayAdapter <String> hotelAdapter = new ArrayAdapter <String>(this, R.layout.support_simple_spinner_dropdown_item, hotel); //Adapter para relacionar Vector con Spinner.
        final ArrayAdapter <String> humanAdapter = new ArrayAdapter <String>(this, R.layout.support_simple_spinner_dropdown_item, human); //Adapter para relacionar Vector con Spinner.
        final ArrayAdapter <String> informAdapter = new ArrayAdapter <String>(this, R.layout.support_simple_spinner_dropdown_item, inform); //Adapter para relacionar Vector con Spinner.
        final ArrayAdapter <String> mecanicAdapter = new ArrayAdapter <String>(this, R.layout.support_simple_spinner_dropdown_item, mecanic); //Adapter para relacionar Vector con Spinner.
        final ArrayAdapter <String> mineriaAdapter = new ArrayAdapter <String>(this, R.layout.support_simple_spinner_dropdown_item, mineria); //Adapter para relacionar Vector con Spinner.
        final ArrayAdapter <String> industrAdapter = new ArrayAdapter <String>(this, R.layout.support_simple_spinner_dropdown_item, industr); //Adapter para relacionar Vector con Spinner.
        final ArrayAdapter <String> saludAdapter = new ArrayAdapter <String>(this, R.layout.support_simple_spinner_dropdown_item, salud); //Adapter para relacionar Vector con Spinner.
        // --- ---

        // --- Rellenar Spinner Cambiante (spinner_carrera) ---
        spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //Función que está pendiente de la opción seleccionada en spinner_area.

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int areaSelected = spinner_area.getSelectedItemPosition(); //Obtener la posición de spinner_area.

                switch (areaSelected){ //En base a la posición seleccionada, rellenar spinner_carrera.
                    case 0:
                        spinner_carrera.setEnabled(false); //Se deshabilita spinner_carrera.
                        spinner_carrera.setAdapter(carreraAdapter); //Spinner + ArrayAdapter = Vector asociado.
                        break;

                    case 1:
                        spinner_carrera.setEnabled(true); //Se habilita spinner_carrera.
                        spinner_carrera.setAdapter(adminAdapter); //Spinner + ArrayAdapter = Vector asociado.
                        break;

                    case 2:
                        spinner_carrera.setEnabled(true); //Se habilita spinner_carrera.
                        spinner_carrera.setAdapter(agroAdapter); //Spinner + ArrayAdapter = Vector asociado.
                        break;

                    case 3:
                        spinner_carrera.setEnabled(true); //Se habilita spinner_carrera.
                        spinner_carrera.setAdapter(construAdapter); //Spinner + ArrayAdapter = Vector asociado.
                        break;

                    case 4:
                        spinner_carrera.setEnabled(true); //Se habilita spinner_carrera.
                        spinner_carrera.setAdapter(disenoAdapter); //Spinner + ArrayAdapter = Vector asociado.
                        break;

                    case 5:
                        spinner_carrera.setEnabled(true); //Se habilita spinner_carrera.
                        spinner_carrera.setAdapter(electrAdapter); //Spinner + ArrayAdapter = Vector asociado.
                        break;

                    case 6:
                        spinner_carrera.setEnabled(true); //Se habilita spinner_carrera.
                        spinner_carrera.setAdapter(hotelAdapter); //Spinner + ArrayAdapter = Vector asociado.
                        break;

                    case 7:
                        spinner_carrera.setEnabled(true); //Se habilita spinner_carrera.
                        spinner_carrera.setAdapter(humanAdapter); //Spinner + ArrayAdapter = Vector asociado.
                        break;

                    case 8:
                        spinner_carrera.setEnabled(true); //Se habilita spinner_carrera.
                        spinner_carrera.setAdapter(informAdapter); //Spinner + ArrayAdapter = Vector asociado.
                        break;

                    case 9:
                        spinner_carrera.setEnabled(true); //Se habilita spinner_carrera.
                        spinner_carrera.setAdapter(mecanicAdapter); //Spinner + ArrayAdapter = Vector asociado.
                        break;

                    case 10:
                        spinner_carrera.setEnabled(true); //Se habilita spinner_carrera.
                        spinner_carrera.setAdapter(mineriaAdapter); //Spinner + ArrayAdapter = Vector asociado.
                        break;

                    case 11:
                        spinner_carrera.setEnabled(true); //Se habilita spinner_carrera.
                        spinner_carrera.setAdapter(industrAdapter); //Spinner + ArrayAdapter = Vector asociado.
                        break;

                    case 12:
                        spinner_carrera.setEnabled(true); //Se habilita spinner_carrera.
                        spinner_carrera.setAdapter(saludAdapter); //Spinner + ArrayAdapter = Vector asociado.
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Función requerida por el ItemSelectedListener.
            }
        });
        // --- ---
    }

    public void registrarAlumno(View view) {
        // --- Extraer valores para validaciones ---
        final String rut = txt_rut.getText().toString().toLowerCase().trim(); //Extrae valor desde el elemento del Layout.
        final String nombre = txt_nombre.getText().toString(); //Extrae valor desde el elemento del Layout.
        final String apellido = txt_apellido.getText().toString(); //Extrae valor desde el elemento del Layout.
        final String mail = txt_mail.getText().toString().toLowerCase().trim(); //Extrae valor desde el elemento del Layout.
        final String password1 = txt_password1.getText().toString(); //Extrae valor desde el elemento del Layout.
        String password2 = txt_password2.getText().toString(); //Extrae valor desde el elemento del Layout.
        final int carreraSelected = spinner_carrera.getSelectedItemPosition(); //Extrae la posición seleccionada del spinner_carrera.
        // --- ---

        // --- Inicio de validaciones ---
            // --- Validar Campos Vacíos ---
        if (rut.isEmpty()){
            Toast.makeText(this,"Debe ingresar su RUT", Toast.LENGTH_SHORT).show();
        }
        else if (nombre.isEmpty()){
            Toast.makeText(this,"Debe ingresar su Nombre.", Toast.LENGTH_SHORT).show();
        }
        else if (apellido.isEmpty()){
            Toast.makeText(this,"Debe ingresar su Apellido.", Toast.LENGTH_SHORT).show();
        }
        else if (mail.isEmpty()){
            Toast.makeText(this,"Debe ingresar su Correo Electrónico INACAP.", Toast.LENGTH_SHORT).show();
        }
        else if (password1.isEmpty()){
            Toast.makeText(this,"Debe ingresar su Contraseña.", Toast.LENGTH_SHORT).show();
        }
        else if (password2.isEmpty()){
            Toast.makeText(this,"Debe ingresar su Contraseña confirmada.", Toast.LENGTH_SHORT).show();
        }
        else if (carreraSelected == 0){
            Toast.makeText(this,"Debe seleccionar una Carrera.", Toast.LENGTH_SHORT).show();
        }
            // --- ---

            // --- Validar Campos Erróneos ---
        else {
            Functions f = new Functions(); //Importa la clase donde se guardan funciones.
            boolean validacionRut = f.validarRut(rut); //Llama a la función "validarRut" y guarda su resultado.

            if (validacionRut == false){ //Si el RUT es inválido.
                Toast.makeText(this,"El RUT ingresado no es correcto.", Toast.LENGTH_SHORT).show();
            } else if (!rut.contains("-")) {
                Toast.makeText(this,"El RUT ingresado debe tener el guión '-'", Toast.LENGTH_SHORT).show();
            } else if(!mail.contains("inacap")){//Si el mail es corporativo de INACAP.
                Toast.makeText(this,"El Correo Electrónico debe ser corporativo de INACAP y/o debe ser tuyo  (nombre.apellido@inacap...).", Toast.LENGTH_SHORT).show();
            } else if (!password1.equals(password2)) { //Si las contraseñas no coinciden.
                Toast.makeText(this,"Las Contraseñas no coinciden, por favor intente nuevamente.", Toast.LENGTH_SHORT).show();
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
                                    Alumno alumno = new Alumno(rut, nombre, apellido, mail, spinner_carrera.getSelectedItem().toString(), password1, "NULL", "Registra tu bicicleta", "Registra tu bicicleta", "Registra tu bicicleta", "Registra tu bicicleta", "NULL");
                                    reference.child(rut).setValue(alumno); //Crea un nuevo "child" con el RUT como ID, en la "tabla" Alumno.
                                    Toast.makeText(getApplicationContext(),"Te has registrado correctamente.", Toast.LENGTH_SHORT).show();
                                    registroCorrecto = true;
                                }else{ //Si ocurre un error.
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException){ //Si el Usuario ya está registado.
                                        Toast.makeText(getApplicationContext(),"El Alumno ya existe", Toast.LENGTH_SHORT).show();
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
    } // Fin función registrarAlumno().
}