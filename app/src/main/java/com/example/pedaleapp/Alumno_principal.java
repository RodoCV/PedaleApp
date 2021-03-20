package com.example.pedaleapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.pedaleapp.lectorQR.IntentIntegrator;
import com.example.pedaleapp.model.Alumno;
import com.example.pedaleapp.views.Alumno_bicycle;
import com.example.pedaleapp.views.Alumno_profile;
import com.example.pedaleapp.views.Map_inacap;
import com.google.android.material.tabs.TabLayout;

import static com.example.pedaleapp.Guardia_principal.CODE_CAMERA;
import static com.example.pedaleapp.Guardia_principal.CODE_GALLERY;

public class Alumno_principal extends AppCompatActivity {
    // --- Elementos del Layout ---
    private Toolbar toolBar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AdapterPagerAlumno adapterPager;
    private Alumno alumno; //Objeto para compartir los datos del Usuario con los Fragments.
    // --- ---

    // --- Fragments del Layout ---
    private Alumno_profile alumno_profile;
    private Alumno_bicycle alumno_bicycle;
    // --- ---

    // --- Elementos de los Fragments ---
    private String rut, nombre, apellido, correo, carrera, password, user_photo, marca, modelo, color, aro, bicycle_photo;
    // --- ---

    // --- Constantes de acción ---
    public static final int CODE_CAMERA_ALUMNO = 1;
    public static final int CODE_GALLERY_ALUMNO = 2;
    public static final int CODE_CAMERA_BICICLETA = 3;
    public static final int CODE_GALLERY_BICICLETA = 4;
    // ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_principal);

        // --- Inicializar los controles ---
        toolBar = findViewById(R.id.toolbar_alumno);
        tabLayout = findViewById(R.id.tabs_alumno);
        viewPager = findViewById(R.id.viewpager_alumno);

        setSupportActionBar(toolBar);
        adapterPager = new AdapterPagerAlumno(getSupportFragmentManager());
        viewPager.setAdapter(adapterPager);
        tabLayout.setupWithViewPager(viewPager);

        rut = getIntent().getStringExtra("Rut"); //Rescatando el atributo extra, pasado en LoginActivity.
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

        setTitle("Hola "+nombre+" "+apellido); //Coloca título.
        // --- ---

        // --- Objeto para compartir los datos del Usuario con los Fragments ---
        alumno = new Alumno (rut, nombre, apellido, correo, carrera, password, user_photo, marca, modelo, color, aro, bicycle_photo);
        // --- ---
    }

    // --- Clase que Genera cada Fragment ---
    public class AdapterPagerAlumno extends FragmentPagerAdapter {

        public AdapterPagerAlumno(FragmentManager fm) { //Constructor del AdapterPager
            super(fm);
        }

        @Override
        public Fragment getItem(int position) { //Determina el Fragment a mostrar.
            switch (position){ //(0, 1, 2).
                case 0:
                    Alumno_profile alumno_profile = new Alumno_profile();

                    return alumno_profile;
                case 1:
                    Alumno_bicycle alumno_bicycle = new Alumno_bicycle();
                    return alumno_bicycle;
                case 2:
                    Map_inacap map_inacap = new Map_inacap();
                    return map_inacap;
            }
            return null;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) { //Determina el Título de los Fragments
            switch (position){ //(0, 1, 2).
                case 0:
                    return "Tu perfil";
                case 1:
                    return "Tu Bicicleta";
                case 2:
                    return "Mapa";
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3; //Determina la cantidad de Fragments a crear.
        }
    }
    // --- Fin Clase que Genera cada Fragment ---

    // --- Método para que los Fragments obtengan el Objeto compartido ---
    public Alumno obtenerAlumno (){
        return alumno;
    }

    // --- Método para entregar la información a los Fragments que cargan Foto de Perfil ---
    // *** Fragments al cargar Foto de Perfil pierden el foco, por eso se requiere éste método acá ***
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case CODE_CAMERA_ALUMNO: //Caso Acceder a la Cámara.
                alumno_profile.onActivityResult(requestCode, resultCode, data);
                break;

            case CODE_GALLERY_ALUMNO: //Caso Acceder a la Galería.
                alumno_profile.onActivityResult(requestCode, resultCode, data);
                break;

            case CODE_CAMERA_BICICLETA: //Caso Acceder a la Galería.
                alumno_bicycle.onActivityResult(requestCode, resultCode, data);
                break;

            case CODE_GALLERY_BICICLETA: //Caso Acceder a la Galería.
                alumno_bicycle.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
    // --- ---
}