package com.example.dglozano.meetapp.modelo;

import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Evento {

    private Integer id;
    private String nombre;
    private LatLng lugar;
    private Date fecha;

    public Evento(String nombre, LatLng lugar, Date fecha) {
        this.nombre = nombre;
        this.lugar = lugar;
        this.fecha = fecha;
    }

    public boolean matches(String query) {
        boolean matches = false;
        if(this.nombre.toUpperCase().contains(query.toUpperCase())) {
            matches = true;
        }
        return matches;
    }

    public Evento() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public static List<Evento> getEventosMock() {
        List<Evento> listaEventosMock = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String nombre = "Fiesta Universitaria";
        String fechaString = "30/03/2018";
        LatLng lugar = new LatLng(-31.652743, -60.721669);
        Date fecha = null;
        try {
            fecha = sdf.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Evento evento = new Evento(nombre, lugar, fecha);
        listaEventosMock.add(evento);

        nombre = "Peña ISI";
        fechaString = "30/05/2018";
        lugar = new LatLng(-31.617035, -60.675274);
        fecha = null;
        try {
            fecha = sdf.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        evento = new Evento(nombre, lugar, fecha);
        listaEventosMock.add(evento);

        nombre = "CONAIISI 2018";
        fechaString = "20/11/2018";
        lugar = new LatLng(-31.617035, -60.675274);
        fecha = null;
        try {
            fecha = sdf.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        evento = new Evento(nombre, lugar, fecha);
        listaEventosMock.add(evento);

        nombre = "JUR 2018";
        fechaString = "03/10/2018";
        lugar = new LatLng(-31.627316, -60.713997);
        fecha = null;
        try {
            fecha = sdf.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        evento = new Evento(nombre, lugar, fecha);
        listaEventosMock.add(evento);

        nombre = "Fiesta Fin de Año";
        fechaString = "31/12/2018";
        lugar = new LatLng(-31.639732, -60.706646);
        fecha = null;
        try {
            fecha = sdf.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        evento = new Evento(nombre, lugar, fecha);
        listaEventosMock.add(evento);

        return listaEventosMock;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Evento evento = (Evento) o;

        return id != null ? id.equals(evento.id) : evento.id == null;
    }
}
