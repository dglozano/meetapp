package com.example.dglozano.meetapp.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tarea {

    private Integer id;
    private String titulo;
    private Participante personaAsignada;
    private EstadoTarea estadoTarea;
    private String descripcion;
    private Double gasto;

    public Tarea() {
        gasto = 0.0;
    }

    public Tarea(Integer id, String titulo, Participante personaAsignada, EstadoTarea estadoTarea, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.personaAsignada = personaAsignada;
        this.estadoTarea = estadoTarea;
        this.descripcion = descripcion;
        this.gasto = 0.0;
    }

    public Tarea(Integer id, String titulo, Participante personaAsignada, EstadoTarea estadoTarea, String descripcion, Double gasto) {
        this.id = id;
        this.titulo = titulo;
        this.personaAsignada = personaAsignada;
        this.estadoTarea = estadoTarea;
        this.descripcion = descripcion;
        this.gasto = gasto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Double getGasto() {
        return gasto;
    }

    public void setGasto(Double gasto) {
        this.gasto = gasto;
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

        Tarea tarea = new Tarea(1, "Comprar 5kg Asado", Participante.getParticipanteSinAsignar(), EstadoTarea.SIN_ASIGNAR, "Esto es una descripción");
        listaTareasMock.add(tarea);

        tarea = new Tarea(2, "Comprar 2 cajones", Participante.getParticipanteSinAsignar(), EstadoTarea.SIN_ASIGNAR, "Esto es una descripción");
        listaTareasMock.add(tarea);

        tarea = new Tarea(3, "Comprar carbon", Participante.getParticipanteSinAsignar(), EstadoTarea.SIN_ASIGNAR, "Esto es una descripción");
        listaTareasMock.add(tarea);

        Participante p = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        tarea = new Tarea(4, "Reservar salon", p, EstadoTarea.EN_PROGRESO, "Esto es una descripción", 500.0);
        listaTareasMock.add(tarea);

        p = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        tarea = new Tarea(5, "Conseguir parlante", p, EstadoTarea.EN_PROGRESO, "Esto es una descripción", 200.0);
        listaTareasMock.add(tarea);

        p = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        tarea = new Tarea(6, "Comprar torta", p, EstadoTarea.EN_PROGRESO, "Esto es una descripción");
        listaTareasMock.add(tarea);

        p = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        tarea = new Tarea(7, "Difundir fiesta", p, EstadoTarea.FINALIZADA, "Esto es una descripción", 200.0);
        listaTareasMock.add(tarea);

        p = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        tarea = new Tarea(8, "Comprar hielo", p, EstadoTarea.FINALIZADA, "Esto es una descripción", 1.0);
        listaTareasMock.add(tarea);

        p = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        tarea = new Tarea(9, "Contratar DJ", p, EstadoTarea.FINALIZADA, "Esto es una descripción", 15.0);
        listaTareasMock.add(tarea);

        p = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        tarea = new Tarea(10, "Contratar iluminacion", p, EstadoTarea.FINALIZADA, "Esto es una descripción", 1000.0);
        listaTareasMock.add(tarea);

        p = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        tarea = new Tarea(11, "Comprar servilletas", p, EstadoTarea.FINALIZADA, "Esto es una descripción", 500.0);
        listaTareasMock.add(tarea);

        return listaTareasMock;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Tarea tarea = (Tarea) o;

        return id != null ? id.equals(tarea.id) : tarea.id == null;
    }
}
