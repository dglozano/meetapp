package com.example.dglozano.meetapp.dao;

import com.example.dglozano.meetapp.modelo.Evento;

import java.util.List;

public class MockDaoEvento implements Dao<Evento> {
    private static final MockDaoEvento ourInstance = new MockDaoEvento();

    public static MockDaoEvento getInstance() {
        return ourInstance;
    }

    private List<Evento> list;

    private MockDaoEvento() {
        this.list = Evento.getEventosMock();
    }

    @Override
    public Evento getById(int id) {
        for(Evento e : this.list) {
            if(e.getId() == id) return e;
        }
        return null;
    }

    @Override
    public void save(Evento item) {
        if(item.getId() == null) {
            item.setId(getNextId());
        } else {
            list.remove(list.indexOf(item));
        }
        this.list.add(item);
    }

    @Override
    public List<Evento> getAll() {
        return this.list;
    }

    private int getNextId() {
        int id = 1;
        for(Evento evento : this.list) {
            if(evento.getId() > id) id = evento.getId();
        }
        return id + 1;
    }
}
