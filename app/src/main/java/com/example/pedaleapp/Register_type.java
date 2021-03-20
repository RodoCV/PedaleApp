package com.example.pedaleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Register_type extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_type);
    }

    public void alumno(View view) {
        Intent i = new Intent(this, Alumno_register.class); //Crea la Pantalla de Registro de Alumno.
        startActivity(i); //Muestra la pantalla creada.
        finish(); //Cierra la Pantalla actual.
    }

    public void guardia(View view) {
        Intent i = new Intent(this, Guardia_register.class); //Crea la Pantalla de Registro de Guardia.
        startActivity(i); //Muestra la pantalla creada.
        finish(); //Cierra la Pantalla actual.
    }

    public void volver(View view) {
        onBackPressed(); //Vuelve a la Pantalla anterior (En éste caso a la de Login, pues no fue cerrada con un finish()).
        finish(); //Cierra la Pantalla actual.
    }
}