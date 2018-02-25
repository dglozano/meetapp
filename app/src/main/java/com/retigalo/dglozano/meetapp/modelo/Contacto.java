package com.retigalo.dglozano.meetapp.modelo;

import android.support.annotation.NonNull;

/**
 * Created by dglozano on 14/02/18.
 */

public class Contacto implements Comparable<Contacto> {

    private String nombre;
    private String numero;
    private boolean isChecked;

    public Contacto(String nombre, String numero) {
        this.nombre = nombre;
        this.numero = numero;
        this.isChecked = false;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNumero() {
        return numero;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int compareTo(@NonNull Contacto contacto) {
        return this.getNombre().compareToIgnoreCase(contacto.getNombre());
    }
}
