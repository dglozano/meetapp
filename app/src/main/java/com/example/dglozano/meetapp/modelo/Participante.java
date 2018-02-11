package com.example.dglozano.meetapp.modelo;

import java.util.ArrayList;
import java.util.List;

public class Participante {
    private static Participante participanteSinAsignar = new Participante();

    private Integer id;
    private String nombreApellido;
    private int pictureId;
    private String numeroTel;

    public Participante(Integer id, String nombreApellido, int pictureId) {
        this.id = id;
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

    public static List<Participante> getParticipantesMock() {
        List<Participante> listaParticipantesMock = new ArrayList<>();

        Participante participante = new Participante(1, "Diego Garcia Lozano", 1);
        listaParticipantesMock.add(participante);

        participante = new Participante(2, "Esteban Rebechi", 2);
        listaParticipantesMock.add(participante);

        participante = new Participante(3, "Andres Martinez", 2);
        listaParticipantesMock.add(participante);

        participante = new Participante(4, "Ariel Kohan", 0);
        listaParticipantesMock.add(participante);

        participante = new Participante(5, "Augusto Tibalt", 1);
        listaParticipantesMock.add(participante);

        return listaParticipantesMock;
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
}
