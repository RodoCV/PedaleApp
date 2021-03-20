package com.example.pedaleapp.function;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class Functions {

    public boolean validarRut (String rutValidar){
        boolean validacion = false;
        String rut = rutValidar;

        try{
            rut = rut.replace(".","");
            rut = rut.replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));
            char dv = rut.charAt(rut.length() - 1);

            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validacion = true;
            }

        } catch (java.lang.NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return validacion;
    }

    public Query armarQuery (String id, DatabaseReference reference){
        Query query = null;
        if (id.contains("@")){ //Correo
            query = reference.orderByChild("correo").equalTo(id);
        } else { //RUT
            query = reference.orderByChild("rut").equalTo(id);
        }
        return query;
    }

    public boolean permisoLogin (String id, String passwordIngresada, String rut, String correo, String password){
        boolean permiso = false;
        if (id.contains("@")){ //Si el ID es Correo.
            if (id.equals(correo) && passwordIngresada.equals(password)){
                permiso = true;
            }
        }
        else { //Si el ID es RUT.
            if (id.equals(rut) && passwordIngresada.equals(password)){
                permiso = true;
            }
        }
        return permiso;
    }
}