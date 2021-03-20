package com.example.pedaleapp.model;

import androidx.annotation.Nullable;

public class Alumno {

    private String rut;
    private String nombre;
    private String apellido;
    private String correo;
    private String carrera;
    private String password;
    private String url_userPhoto;
    private String marca;
    private String modelo;
    private String color;
    private String aro;
    private String url_bicyclePhoto;

    //Para generar código (Alt + Insert)

    public Alumno(){

    }

    public Alumno(String rut, String nombre, String apellido, String correo, String carrera, String password, String url_userPhoto, String marca, String modelo, String color, String aro, String url_bicyclePhoto) {
        this.rut = rut;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.carrera = carrera;
        this.password = password;
        this.url_userPhoto = url_userPhoto;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.aro = aro;
        this.url_bicyclePhoto = url_bicyclePhoto;
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

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
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

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAro() {
        return aro;
    }

    public void setAro(String aro) {
        this.aro = aro;
    }

    public String getUrl_bicyclePhoto() {
        return url_bicyclePhoto;
    }

    public void setUrl_bicyclePhoto(String url_bicyclePhoto) {
        this.url_bicyclePhoto = url_bicyclePhoto;
    }

    @Override //Se sobreescribe método para poder comparar objetos por atributos, no por posición de memoria
    public boolean equals(@Nullable Object obj) {
        return super.equals(((Alumno)obj).rut);
    }
}