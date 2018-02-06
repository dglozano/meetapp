package com.example.dglozano.meetapp.modelo;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Evento {

    private int id;
    private String nombre;
    private LatLng lugar;
    private String fecha;

    public Evento(String nombre, String fecha) {
        this.nombre = nombre;
        this.fecha = fecha;
    }

    public boolean matches (String query) {
        boolean matches = false;
        if(this.nombre.toUpperCase().contains(query.toUpperCase())) {
            matches = true;
        }
        return matches;
    }

    public Evento() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LatLng getLugar() {
        return lugar;
    }

    public void setLugar(LatLng lugar) {
        this.lugar = lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public static List<Evento> getEventosMock() {
        List<Evento> listaEventosMock = new ArrayList<>();

        String nombre = "Fiesta Universitaria";
        String fecha = "2018-03-30";
        Evento evento = new Evento(nombre, fecha);
        listaEventosMock.add(evento);

        nombre = "Peña ISI";
        fecha = "2018-05-30";
        evento = new Evento(nombre, fecha);
        listaEventosMock.add(evento);

        nombre = "CONAIISI 2018";
        fecha = "2018-11-20";
        evento = new Evento(nombre, fecha);
        listaEventosMock.add(evento);

        nombre = "JUR 2018";
        fecha = "2018-11-03";
        evento = new Evento(nombre, fecha);
        listaEventosMock.add(evento);

        nombre = "Fiesta Fin de Año";
        fecha = "2018-12-31";
        evento = new Evento(nombre, fecha);
        listaEventosMock.add(evento);


        return listaEventosMock;
    }
}
