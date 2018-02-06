package com.example.dglozano.meetapp.dao;

/**
 * Created by Esteban on 6/2/2018.
 */

class MockDaoEvento implements Dao {
    private static final MockDaoEvento ourInstance = new MockDaoEvento();

    static MockDaoEvento getInstance() {
        return ourInstance;
    }

    private MockDaoEvento() {
    }
}
