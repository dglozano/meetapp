package com.example.dglozano.meetapp.dao;

import java.util.List;

/**
 * Created by dglozano on 11/02/18.
 */

public interface DaoEventoMember<T> {
    T getById(int id);

    void save(T item, int eventoId);

    void delete(T item);

    void update(T item);

    List<T> getAll();

    List<T> getAllDelEvento(int idEvento);
}
