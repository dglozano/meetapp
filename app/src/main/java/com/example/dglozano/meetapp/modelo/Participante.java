package com.example.dglozano.meetapp.modelo;

import java.util.ArrayList;
import java.util.List;

public class Participante {
    private static Participante participanteSinAsignar = new Participante();

    private Integer id;
    private String nombreApellido;
    private int pictureId;
    private String numeroTel;

    public Participante(String nombreApellido, int pictureId) {
        this.pictureId = pictureId;
        this.nombreApellido = nombreApellido;
    }

    public Participante() {
        this.id = null;
        this.nombreApellido = "<Sin Asignar>";
        this.pictureId = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroTel() {
        return numeroTel;
    }

    public void setNumero(String numero) {
        this.numeroTel = numero;
    }

    public String getNombreApellido() {
        return nombreApellido;
    }

    public void setNombreApellido(String nombreApellido) {
        this.nombreApellido = nombreApellido;
    }

    @Override
    public String toString() {
        return nombreApellido;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public boolean matches(String query) {
        return this.nombreApellido.toUpperCase().contains(query.toUpperCase());
    }

    public static Participante getParticipanteSinAsignar() {
        return Participante.participanteSinAsignar;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Participante that = (Participante) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    public static List<Participante> getParticipantesMock(){
        List<Participante> participantes = new ArrayList<>();
        participantes.add(new Participante("Juan Perez", 2));
        participantes.add(new Participante("Maria Garcia", 0));
        participantes.add(new Participante("Jose Lopez", 1));
        participantes.add(new Participante("Diego Garcia Lozano", 1));
        participantes.add(new Participante("Esteban Rebechi", 2));
        participantes.add(new Participante("Andres Martinez", 2));
        participantes.add(new Participante("Ariel Kohan", 0));
        participantes.add(new Participante("Augusto Tibalt", 1));
        participantes.add(new Participante("Lucas Gamba", 0));
        participantes.add(new Participante("Franco Soldano", 1));
        participantes.add(new Participante("Mauro Pitton", 0));
        participantes.add(new Participante("Bruno Pitton", 1));
        participantes.add(new Participante("Nelson Acevedo", 2));
        participantes.add(new Participante("Nereo Fernandez", 2));
        participantes.add(new Participante("Yeimar Gomez Andrade", 0));
        participantes.add(new Participante("Rodrigo Gomez", 1));
        participantes.add(new Participante("Agustin Pane", 2));
        participantes.add(new Participante("Fede Madoery", 1));
        participantes.add(new Participante("Flaminia Castanio", 1));
        participantes.add(new Participante("Juliana Rossi", 0));
        participantes.add(new Participante("Gonzalo Etcheveksadfgay", 2));
        participantes.add(new Participante("Kevin Raud", 1));
        participantes.add(new Participante("El Colo", 0));
        participantes.add(new Participante("Juan De Garay", 1));
        participantes.add(new Participante("Lisandro Lopez", 0));
        participantes.add(new Participante("General Paz", 2));
        participantes.add(new Participante("Jose de San Martin", 1));
        participantes.add(new Participante("Domingo Faustino Sarmiento", 0));
        return participantes;
    }
}
