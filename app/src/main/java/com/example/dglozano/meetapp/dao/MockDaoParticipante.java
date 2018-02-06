package com.example.dglozano.meetapp.dao;

import com.example.dglozano.meetapp.modelo.Participante;

import java.util.List;

public class MockDaoParticipante implements Dao<Participante> {

    private static final MockDaoParticipante ourInstance = new MockDaoParticipante();

    public static MockDaoParticipante getInstance() {
        return ourInstance;
    }

    private List<Participante> list;

    private MockDaoParticipante() {
        this.list = Participante.getParticipantesMock();
    }

    @Override
    public Participante getById(int id) {
        for(Participante e : this.list) {
            if(e.getId() == id) return e;
        }
        return null;
    }

    @Override
    public void save(Participante item) {
        if(item.getId() == null) {
            item.setId(getNextId());
        } else {
            list.remove(list.indexOf(item));
        }
        this.list.add(item);
    }

    @Override
    public List<Participante> getAll() {
        return this.list;
    }

    private int getNextId() {
        int id = 1;
        for(Participante t : this.list) {
            if(t.getId() > id) id = t.getId();
        }
        return id + 1;
    }
}
