package com.example.pedaleapp.model;

import androidx.annotation.Nullable;

public class Guardia {

    private String rut;
    private String nombre;
    private String apellido;
    private String correo;
    private String password;
    private String url_userPhoto;

    //Para generar código (Alt + Insert)

    public Guardia() {

    }

    public Guardia(String rut, String nombre, String apellido, String correo, String password, String url_userPhoto) {
        this.rut = rut;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.password = password;
        this.url_userPhoto = url_userPhoto;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl_userPhoto() {
        return url_userPhoto;
    }

    public void setUrl_userPhoto(String url_userPhoto) {
        this.url_userPhoto = url_userPhoto;
    }

    @Override //Se sobreescribe método para poder comparar objetos por atributos, no por posición de memoria
    public boolean equals(@Nullable Object obj) {
        return super.equals(((Guardia)obj).rut);
    }
}