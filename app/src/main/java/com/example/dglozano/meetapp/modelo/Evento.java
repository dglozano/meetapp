package com.example.dglozano.meetapp.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Evento implements Parcelable {
    private String nombre;
    private LatLng lugar;
    private String fecha;

    protected Evento(Parcel in) {
        nombre = in.readString();
        lugar = in.readParcelable(LatLng.class.getClassLoader());
        fecha = in.readString();
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

    public Evento() {
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
}
