package com.example.dglozano.meetapp.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dglozano on 01/02/18.
 */

public class Participante {

    public static Integer cantidadParticipantes = 0;
    private static Participante participanteSinAsignar = new Participante();

    private int id;
    private String nombreApellido;
    private int pictureId;

    public Participante(String nombreApellido, int pictureId) {
        this.pictureId = pictureId;
        this.id = ++Participante.cantidadParticipantes;
        this.nombreApellido = nombreApellido;
    }

    public Participante(){
        this.id = 0;
        this.nombreApellido = "<Sin Asignar>";
        this.pictureId = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean matches(String query){
        return this.nombreApellido.toUpperCase().contains(query.toUpperCase());
    }

    public static List<Participante> getParticipantesMock() {
        List<Participante> listaParticipantesMock = new ArrayList<>();

        Participante participante = new Participante("Diego Garcia Lozano", 1);
        listaParticipantesMock.add(participante);

        participante = new Participante("Esteban Rebechi", 2);
        listaParticipantesMock.add(participante);

        participante = new Participante("Andres Martinez", 2);
        listaParticipantesMock.add(participante);

        participante = new Participante("Ariel Kohan", 0);
        listaParticipantesMock.add(participante);

        participante = new Participante("Augusto Tibalt", 1);
        listaParticipantesMock.add(participante);

        return listaParticipantesMock;
    }

    public static Participante getParticipanteSinAsignar(){
        return Participante.participanteSinAsignar;
    }
}
