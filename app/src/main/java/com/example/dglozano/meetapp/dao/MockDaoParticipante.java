package com.example.dglozano.meetapp.dao;

/**
 * Created by Esteban on 6/2/2018.
 */

class MockDaoParticipante implements Dao<Participante> {
    private static final MockDaoParticipante ourInstance = new MockDaoParticipante();

    static MockDaoParticipante getInstance() {
        return ourInstance;
    }

    private MockDaoParticipante() {
    }
}
