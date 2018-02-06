package com.example.dglozano.meetapp.dao;

import java.util.List;

public interface EventoDao<T> {
    T getById(int id);

    void guardar(T item);

    List<T> getAll();
}
