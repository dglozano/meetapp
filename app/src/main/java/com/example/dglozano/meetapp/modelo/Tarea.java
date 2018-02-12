package com.example.dglozano.meetapp.modelo;

import java.util.ArrayList;
import java.util.List;

public class Tarea {

    private Integer id;
    private String titulo;
    private Participante personaAsignada;
    private EstadoTarea estadoTarea;
    private String descripcion;
    private Double gasto;

    public Tarea() {
        this.gasto = 0.0;
    }

    public Tarea(String titulo, Participante personaAsignada, EstadoTarea estadoTarea, String descripcion) {
        this.titulo = titulo;
        this.personaAsignada = personaAsignada;
        this.estadoTarea = estadoTarea;
        this.descripcion = descripcion;
        this.gasto = 0.0;
    }

    public Tarea(String titulo, String descripcion) {
        this.titulo = titulo;
        this.personaAsignada = Participante.getParticipanteSinAsignar();
        this.estadoTarea = EstadoTarea.SIN_ASIGNAR;
        this.descripcion = descripcion;
        this.gasto = 0.0;
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

    public Double getGasto() {
        return gasto;
    }

    public void setGasto(Double gasto) {
        this.gasto = gasto;
    }

    @Override
    public String toString() {
        return this.personaAsignada + " tiene que " + this.titulo;
    }

    public boolean estaFinalizada() {
        return this.estadoTarea == EstadoTarea.FINALIZADA;
    }

    public static List<Tarea> getTareasMock() {
        List<Tarea> listaTareasMock = new ArrayList<>();

        listaTareasMock.add(new Tarea("Comprar 5kg Asado", "Somos 10 personas que vamos asi que comprar 5kg mas achuras"));
        listaTareasMock.add(new Tarea("Comprar 2 cajones", "Que no sean Quilmes por favor!"));
        listaTareasMock.add(new Tarea("Comprar carbon", "Si conseguis leña mejor"));
        listaTareasMock.add(new Tarea("Reservar salon", "Que tenga aire acondicionado. Si no, no!"));
        listaTareasMock.add(new Tarea("Conseguir parlante", "Inalambrico bluetooth. Trae auxiliar tambien."));
        listaTareasMock.add(new Tarea("Comprar torta", "Chocotorta, torta oreo o lemon Pie"));
        listaTareasMock.add(new Tarea("Difundir fiesta", "Difundir por Instagram, FB y WhatsApp pasando la dirección por privado"));
        listaTareasMock.add(new Tarea("Comprar hielo", "5 bolsas de 10kg"));
        listaTareasMock.add(new Tarea("Contratar DJ", "Cualquiera menos el boludo de Chento"));
        listaTareasMock.add(new Tarea("Contratar iluminacion", "Una bola de disco tambien si puede ser"));
        listaTareasMock.add(new Tarea("Comprar servilletas", "Cualquiera menos esas plastificados que no limpian nada"));
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
