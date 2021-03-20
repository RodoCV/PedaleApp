package com.example.pedaleapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.pedaleapp.views.About_fdiaz;
import com.example.pedaleapp.views.About_lzabalaga;
import com.example.pedaleapp.views.About_rcampos;
import com.google.android.material.tabs.TabLayout;

public class About_PedaleApp extends AppCompatActivity {
    // --- Elementos del Layout ---
    private Toolbar toolBar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AdapterPagerAbout adapterPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_pedaleapp);

        // --- Inicializar los controles ---
        toolBar = findViewById(R.id.toolbar_about);
        tabLayout = findViewById(R.id.tabs_about);
        viewPager = findViewById(R.id.viewpager_about);

        setSupportActionBar(toolBar);
        adapterPager = new AdapterPagerAbout(getSupportFragmentManager());
        viewPager.setAdapter(adapterPager);
        tabLayout.setupWithViewPager(viewPager);

        setTitle("Acerca de los Creadores de PedaleaApp"); //Coloca título.
    }

    // --- Clase que Genera cada Fragment ---
    public class AdapterPagerAbout extends FragmentPagerAdapter {

        public AdapterPagerAbout(FragmentManager fm) { //Constructor del AdapterPager
            super(fm);
        }

        @Override
        public Fragment getItem(int position) { //Determina el Fragment a mostrar.
            switch (position){ //(0, 1, 2).
                case 0:
                    About_rcampos about_rcampos = new About_rcampos();
                    return about_rcampos;
                case 1:
                    About_fdiaz about_fdiaz = new About_fdiaz();
                    return about_fdiaz;
                case 2:
                    About_lzabalaga about_lzabalaga = new About_lzabalaga();
                    return about_lzabalaga;
            }
            return null;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) { //Determina el Título de los Fragments
            switch (position){ //(0, 1, 2).
                case 0:
                    return "R. Campos";
                case 1:
                    return "F. Díaz";
                case 2:
                    return "L. Zabalaga";
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3; //Determina la cantidad de Fragments a crear.
        }
    }
    // --- Fin Clase que Genera cada Fragment ---
}