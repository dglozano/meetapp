package com.example.dglozano.meetapp.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Participante {
    private static Participante participanteSinAsignar = new Participante();

    private Integer id;
    private String nombreApellido;
    private String numeroTel;

    public Participante(String nombreApellido, String numeroTel) {
        this.nombreApellido = nombreApellido;
        this.numeroTel = numeroTel;
    }

    public Participante() {
        this.id = null;
        this.nombreApellido = "<Sin Asignar>";
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

    public static Participante getParticipanteSinAsignar() {
        return Participante.participanteSinAsignar;
    }

    public boolean esSinAsignar(){
        return this.id == null;
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
        numRandom = ThreadLocalRandom.current().nextInt(154000000, 157000000);
        numTelRandom = String.valueOf(numRandom);
        return participantes;
    }
}
