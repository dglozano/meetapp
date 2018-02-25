package com.example.dglozano.meetapp.modelo;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Participante implements Comparable<Participante> {
    private static final String NOMBRE_CREADOR_EVENTO = "Yo";
    private static final String NOMBRE_SIN_ASIGNAR = "<Sin Asignar>";

    private Integer id;
    private String nombreApellido;
    private String numeroTel;
    private boolean esCreador;
    private boolean esSinAsignar;

    public Participante(String nombreApellido, String numeroTel) {
        this.nombreApellido = nombreApellido;
        this.numeroTel = numeroTel;
        this.esCreador = false;
        this.esSinAsignar = false;
    }

    public Participante() {
        this.esCreador = false;
        this.esSinAsignar = false;
    }

    public static Participante participanteCreadorEvento() {
        Participante creador = new Participante();
        creador.nombreApellido = NOMBRE_CREADOR_EVENTO;
        creador.numeroTel = "";
        creador.esCreador = true;
        creador.esSinAsignar = false;
        return creador;
    }

    public static Participante getParticipanteSinAsignar() {
        Participante sinAsignar = new Participante();
        sinAsignar.nombreApellido = NOMBRE_SIN_ASIGNAR;
        sinAsignar.numeroTel = "";
        sinAsignar.esCreador = false;
        sinAsignar.esSinAsignar = true;
        return sinAsignar;
    }

    public boolean esCreadorEvento() {
        return this.esCreador;
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

    public boolean matches(String query) {
        return this.nombreApellido.toUpperCase().contains(query.toUpperCase());
    }

    public boolean esSinAsignar() {
        return this.esSinAsignar;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participante that = (Participante) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    public static List<Participante> getParticipantesMock() {
        List<Participante> participantes = new ArrayList<>();
        int numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        String numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Juan Perez", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Maria Garcia", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Jose Lopez", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Diego Alejandro Garcia Lozano", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Esteban Rebechi", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Andres Martinez", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Ariel Kohan", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Augusto Tibalt", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Lucas Jesus Dios Salvador Gamba", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Franco Soldano", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Mauro Pitton", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Bruno Pitton", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Nelson Acevedo", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Nereo Fernandez", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Yeimar El Negro de Whatsapp Gomez Andrade", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Rodrigo Gomez", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Agustin Pane", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Fede Madoery", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Flaminia Castanio", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Juliana Rossi", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Gonzalo Etchevasdfadsfdsafsadsdeksadfgay", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Kevin Raud", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("El Colo", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Juan De Garay", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Lisandro Lopez", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("General Paz", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Jose de San Martin", numTelRandom));
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        participantes.add(new Participante("Domingo Faustino Sarmiento", numTelRandom));
        return participantes;
    }

    public void setEsCreador(boolean esCreador) {
        this.esCreador = esCreador;
    }

    public void setEsSinAsignar(boolean esSinAsignar) {
        this.esSinAsignar = esSinAsignar;
    }

    @Override
    public int compareTo(@NonNull Participante participante) {
        if (this.esSinAsignar()) return -1;
        else if (participante.esSinAsignar()) return 1;
        if (this.esCreadorEvento()) return -1;
        else if (participante.esCreadorEvento()) return 1;
        else return this.getNombreApellido().compareToIgnoreCase(participante.getNombreApellido());
    }
}
