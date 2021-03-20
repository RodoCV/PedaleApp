package com.example.pedaleapp.views;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pedaleapp.About_PedaleApp;
import com.example.pedaleapp.LoginActivity;
import com.example.pedaleapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class About_rcampos extends Fragment {

    private Button btn_rcampos;

    public About_rcampos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_rcampos, container, false);

        // --- Inicializar los controles ---
        btn_rcampos = view.findViewById(R.id.btn_about_rcampos);
        final About_PedaleApp activity = (About_PedaleApp) getActivity();
        // --- ---

        // --- Botón Volver ---
        btn_rcampos.setOnClickListener(new View.OnClickListener() { //Método para accionar el botón.
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

}
