package com.example.dglozano.meetapp.dao;

import com.example.dglozano.meetapp.modelo.Evento;

import java.util.List;

/**
 * Created by dglozano on 11/02/18.
 */

public interface DaoEventoMember<T> {
    T getById(int id);

    void save(T item, int eventoId);

    void delete(T item);

    List<T> getAll();

    List<T> getAllDelEvento(int idEvento);
}
