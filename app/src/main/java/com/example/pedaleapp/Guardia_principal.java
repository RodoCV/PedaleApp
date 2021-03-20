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
import com.example.pedaleapp.model.Guardia;
import com.example.pedaleapp.views.Guardia_lector;
import com.example.pedaleapp.views.Guardia_profile;
import com.example.pedaleapp.views.Guardia_search;
import com.google.android.material.tabs.TabLayout;

public class Guardia_principal extends AppCompatActivity {
    // --- Elementos del Layout ---
    private Toolbar toolBar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AdapterPagerGuardia adapterPager;
    private Guardia guardia; //Objeto para compartir los datos del Usuario con los Fragments.
    // --- ---

    // --- Fragments del Layout ---
    private Guardia_profile guardia_profile;
    private Guardia_lector guardia_lector;
    private Guardia_search guardia_search;
    // --- ---

    // --- Elementos de los Fragments ---
    String rut, nombre, apellido, correo, password, user_photo;
    // --- ---

    // --- Constantes de acción ---
    public static final int CODE_CAMERA = 21;
    public static final int CODE_GALLERY = 22;
    // ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardia_principal);

        // --- Inicializar los controles ---
        toolBar = findViewById(R.id.toolbar_guardia);
        tabLayout = findViewById(R.id.tabs_guardia);
        viewPager = findViewById(R.id.viewpager_guardia);

        guardia_profile = new Guardia_profile();
        guardia_lector = new Guardia_lector();
        guardia_search = new Guardia_search();

        setSupportActionBar(toolBar);
        adapterPager = new AdapterPagerGuardia(getSupportFragmentManager());
        viewPager.setAdapter(adapterPager);
        tabLayout.setupWithViewPager(viewPager);

        rut = getIntent().getStringExtra("Rut"); //Rescatando el atributo extra, pasado en LoginActivity.
        nombre = getIntent().getStringExtra("Nombre"); //Rescatando el atributo extra, pasado en LoginActivity.
        apellido = getIntent().getStringExtra("Apellido"); //Rescatando el atributo extra, pasado en LoginActivity.
        correo = getIntent().getStringExtra("Correo"); //Rescatando el atributo extra, pasado en LoginActivity.
        password = getIntent().getStringExtra("Password"); //Rescatando el atributo extra, pasado en LoginActivity.
        user_photo = getIntent().getStringExtra("Foto_Usuario"); //Rescatando el atributo extra, pasado en LoginActivity.

        setTitle("Hola "+nombre+" "+apellido); //Coloca título.
        // --- ---

        // --- Llenado del Objeto a compartir con los Fragments ---
        guardia = new Guardia (rut, nombre, apellido, correo, password, user_photo);
        // --- ---
    }

    // --- Clase que Genera cada Fragment ---
    public class AdapterPagerGuardia extends FragmentPagerAdapter {

        public AdapterPagerGuardia(FragmentManager fm) { //Constructor del AdapterPager
            super(fm);
        }

        @Override
        public Fragment getItem(int position) { //Determina el Fragment a mostrar.
            switch (position){ //(0, 1, 2).
                case 0:
                    return guardia_profile;
                case 1:
                    ;
                    return guardia_lector;
                case 2:
                    ;
                    return guardia_search;
            }
            return null;
        }

        public CharSequence getPageTitle(int position) { //Determina el Título de los Fragments
            switch (position){ //(0, 1, 2).
                case 0:
                    return "Tu perfil";
                case 1:
                    return "Lector QR";
                case 2:
                    return "Buscador RUT";
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
    public Guardia obtenerGuardia (){
        return guardia;
    }
    // --- ---

    // --- Método para entregar la información al Fragment guardia_search, que escanea código QR ---
    // *** Fragment Guardia_search al escanear código QR pierde el foco, por eso se requiere éste método acá ***
    // *** Fragment Guardia_profile al cargar Foto de Perfil pierde el foco, por eso se requiere éste método acá ***
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case IntentIntegrator.REQUEST_CODE: //Caso Escaneo Código QR.
                guardia_lector.onActivityResult(requestCode, resultCode, data);
                break;

            case CODE_GALLERY: //Caso Acceder a la Galería.
                guardia_profile.onActivityResult(requestCode, resultCode, data);
                break;

            case CODE_CAMERA: //Caso Acceder a la Cámara.
                guardia_profile.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
    // --- ---
}