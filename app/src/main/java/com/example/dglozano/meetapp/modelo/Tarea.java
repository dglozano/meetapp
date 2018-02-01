package com.example.dglozano.meetapp.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dglozano on 01/02/18.
 */

public class Tarea {

    private String titulo;
    private Participante personaAsignada;
    private EstadoTarea estadoTarea;

    public Tarea(){
    }

    public Tarea(String titulo, Participante personaAsignada, EstadoTarea estadoTarea){
        this.titulo = titulo;
        this.personaAsignada = personaAsignada;
        this.estadoTarea = estadoTarea;
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
