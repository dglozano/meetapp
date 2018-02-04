package com.example.dglozano.meetapp.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tarea {

    private int id;
    private String titulo;
    private Participante personaAsignada;
    private EstadoTarea estadoTarea;
    private String descripcion;

    public Tarea() {
    }

    public Tarea(String titulo, Participante personaAsignada, EstadoTarea estadoTarea) {
        this.titulo = titulo;
        this.personaAsignada = personaAsignada;
        this.estadoTarea = estadoTarea;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Participante getPersonaAsignada() {
        return personaAsignada;
    }

    public void setPersonaAsignada(Participante personaAsignada) {
        this.personaAsignada = personaAsignada;
    }

    public EstadoTarea getEstadoTarea() {
        return estadoTarea;
    }

    public void setEstadoTarea(EstadoTarea estadoTarea) {
        this.estadoTarea = estadoTarea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean matches(String query) {
        boolean matches = false;
        if(this.titulo.toUpperCase().contains(query.toUpperCase())) {
            matches = true;
        }
        if(this.getPersonaAsignada().matches(query)) {
            matches = true;
        }
        return matches;
    }

    @Override
    public String toString() {
        return this.personaAsignada + " tiene que " + this.titulo;
    }

    public static List<Tarea> getTareasMock() {
        List<Tarea> listaTareasMock = new ArrayList<>();

        List<Participante> listaParticipantesMock = Participante.getParticipantesMock();

        Tarea tarea = new Tarea("Comprar 5kg Asado", Participante.getParticipanteSinAsignar(), EstadoTarea.SIN_ASIGNAR);
        listaTareasMock.add(tarea);

        tarea = new Tarea("Comprar 2 cajones", Participante.getParticipanteSinAsignar(), EstadoTarea.SIN_ASIGNAR);
        listaTareasMock.add(tarea);

        tarea = new Tarea("Comprar carbon", Participante.getParticipanteSinAsignar(), EstadoTarea.SIN_ASIGNAR);
        listaTareasMock.add(tarea);

        Participante p = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        tarea = new Tarea("Reservar salon", p, EstadoTarea.EN_PROGRESO);
        listaTareasMock.add(tarea);

        p = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        tarea = new Tarea("Conseguir parlante", p, EstadoTarea.EN_PROGRESO);
        listaTareasMock.add(tarea);

        p = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        tarea = new Tarea("Comprar torta", p, EstadoTarea.EN_PROGRESO);
        listaTareasMock.add(tarea);

        p = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        tarea = new Tarea("Difundir fiesta", p, EstadoTarea.FINALIZADA);
        listaTareasMock.add(tarea);

        p = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        tarea = new Tarea("Comprar hielo", p, EstadoTarea.FINALIZADA);
        listaTareasMock.add(tarea);

        p = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        tarea = new Tarea("Contratar DJ", p, EstadoTarea.FINALIZADA);
        listaTareasMock.add(tarea);

        p = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        tarea = new Tarea("Contratar iluminacion", p, EstadoTarea.FINALIZADA);
        listaTareasMock.add(tarea);

        p = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        tarea = new Tarea("Comprar servilletas", p, EstadoTarea.FINALIZADA);
        listaTareasMock.add(tarea);

        return listaTareasMock;
    }
}
