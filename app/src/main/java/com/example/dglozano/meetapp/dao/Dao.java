package com.example.dglozano.meetapp.dao;

import java.util.List;

public interface Dao<T> {
    T getById(int id);

    void save(T item);

    List<T> getAll();
}
