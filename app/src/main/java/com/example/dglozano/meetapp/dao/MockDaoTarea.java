package com.example.dglozano.meetapp.dao;

/**
 * Created by Esteban on 6/2/2018.
 */

class MockDaoTarea implements Dao<Tarea> {
    private static final MockDaoTarea ourInstance = new MockDaoTarea();

    static MockDaoTarea getInstance() {
        return ourInstance;
    }

    private MockDaoTarea() {
    }
}
