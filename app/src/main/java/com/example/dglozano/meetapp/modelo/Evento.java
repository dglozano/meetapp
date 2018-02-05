package com.example.dglozano.meetapp.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Evento implements Parcelable {
    private String nombre;
    private LatLng lugar;
    private String fecha;

    protected Evento(Parcel in) {
        nombre = in.readString();
        lugar = in.readParcelable(LatLng.class.getClassLoader());
        fecha = in.readString();
    }

    public Evento(String nombre, String fecha) {
        this.nombre = nombre;
        this.fecha = fecha;
    }

    public static final Creator<Evento> CREATOR = new Creator<Evento>() {
        @Override
        public Evento createFromParcel(Parcel in) {
            return new Evento(in);
        }

        @Override
        public Evento[] newArray(int size) {
            return new Evento[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nombre);
        parcel.writeParcelable(lugar, i);
        parcel.writeString(fecha);
    }

    public boolean matches (String query){
        boolean matches = false;
        if(this.nombre.toUpperCase().contains(query.toUpperCase())){
            matches = true;
        }
        return matches;
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
