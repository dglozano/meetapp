package com.retigalo.dglozano.meetapp.dao;

import com.retigalo.dglozano.meetapp.modelo.Evento;

import java.util.List;

public interface DaoEvento {
    Evento getById(int id);

    long save(Evento item);

    void update(Evento item);

    void delete(Evento item);

    List<Evento> getAll();
}
