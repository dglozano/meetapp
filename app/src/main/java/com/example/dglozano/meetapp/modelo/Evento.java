package com.example.dglozano.meetapp.modelo;

import com.example.dglozano.meetapp.dao.SQLiteDaoParticipante;
import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
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
    private List<Participante> participantes;
    private List<Tarea> tareas;
    private List<Pago> pagos;

    public Evento(String nombre, LatLng lugar, Date fecha) {
        this.nombre = nombre;
        this.lugar = lugar;
        this.fecha = fecha;
        this.participantes = new ArrayList<>();
        this.tareas = new ArrayList<>();
        this.pagos = new ArrayList<>();
    }

    public boolean matches(String query) {
        boolean matches = false;
        if(this.nombre.toUpperCase().contains(query.toUpperCase())) {
            matches = true;
        }
        return matches;
    }

    public Evento() {
        this.participantes = new ArrayList<>();
        this.tareas = new ArrayList<>();
        this.pagos = new ArrayList<>();
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

    public void addParticipante(Participante p){
        this.participantes.add(p);
    }

    public void addAllParticipantes(List<Participante> participantes){
        this.participantes.addAll(participantes);
    }

    public void eliminarParticipante(Participante pEliminar){
        for(Participante p: participantes){
            if(p.getId() == pEliminar.getId()){
                participantes.remove(p);
                //TODO ON DELETE CASCADE
            }
        }
    }

    public List<Participante> getParticipantes(){
        return this.participantes;
    }

    public void addTarea(Tarea t){
        this.tareas.add(t);
    }

    public void addAllTareas(List<Tarea> tareas){
        this.tareas.addAll(tareas);
    }

    public void eliminarTarea(Tarea tEliminar){
        for(Tarea t: tareas){
            if(t.getId() == tEliminar.getId()){
                tareas.remove(t);
                //TODO ON DELETE CASCADE
            }
        }
    }

    public List<Pago> getPagos(){
        return this.pagos;
    }

    public void addPago(Pago p){
        this.pagos.add(p);
    }

    public void addAllPagos(List<Pago> pagos){
        this.pagos.addAll(pagos);
    }

    public void eliminarPago(Pago pEliminar){
        for(Pago p: pagos){
            if(p.getId() == pEliminar.getId()){
                tareas.remove(p);
                //TODO ON DELETE CASCADE
            }
        }
    }

    public List<Tarea> getTareas(){
        return this.tareas;
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
