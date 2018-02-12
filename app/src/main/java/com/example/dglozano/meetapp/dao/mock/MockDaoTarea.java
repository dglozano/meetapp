package com.example.dglozano.meetapp.dao.mock;


import com.example.dglozano.meetapp.dao.DaoEvento;
import com.example.dglozano.meetapp.dao.DaoEventoMember;
import com.example.dglozano.meetapp.modelo.Tarea;

import java.util.List;

public class MockDaoTarea implements DaoEventoMember<Tarea> {

    private static final MockDaoTarea ourInstance = new MockDaoTarea();

    public static MockDaoTarea getInstance() {
        return ourInstance;
    }

    private List<Tarea> list;

    private MockDaoTarea() {
        this.list = Tarea.getTareasMock();
    }

    @Override
    public Tarea getById(int id) {
        for(Tarea e : this.list) {
            if(e.getId() == id) return e;
        }
        return null;
    }

    @Override
    public void save(Tarea item, int eventoId) {
        if(item.getId() == null) {
            item.setId(getNextId());
        } else {
            list.remove(list.indexOf(item));
        }
        this.list.add(item);
    }

    @Override
    public void delete(Tarea item) {
        for(Tarea t: this.list){
            if(t.getId() == item.getId()) list.remove(t);
        }
    }

    @Override
    public void update(Tarea item) {

    }

    @Override
    public List<Tarea> getAll() {
        return this.list;
    }

    @Override
    public List<Tarea> getAllDelEvento(int idEvento) {
        return this.list;
    }

    private int getNextId() {
        int id = 1;
        for(Tarea t : this.list) {
            if(t.getId() > id) id = t.getId();
        }
        return id + 1;
    }
}
