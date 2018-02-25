package com.example.dglozano.meetapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.dglozano.meetapp.modelo.Evento;
import com.example.dglozano.meetapp.modelo.Participante;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SQLiteDaoParticipante implements DaoEventoMember<Participante> {

    private SQLiteDatabase db;
    private final MeetAppOpenHelper dbhelper;

    /**
     * Constructor. Setea el contexto y obtiene la instancia del singleton del dbhelper
     *
     * @param c
     */
    public SQLiteDaoParticipante(Context c) {
        Context context = c;
        dbhelper = MeetAppOpenHelper.getInstance(context, Constants.DATABASE_NAME,
                Constants.DATABASE_VERSION);
    }

    @NonNull
    private Participante parseParticipanteFromCursor(Cursor c) {
        Participante participante;
        participante = new Participante();
        participante.setId(c.getInt(c.getColumnIndex(Constants.PARTICIPANTE_ID)));
        participante.setNombreApellido(c.getString(c.getColumnIndex(Constants.PARTICIPANTE_NOMBRE)));
        participante.setNumero(c.getString(c.getColumnIndex(Constants.PARTICIPANTE_TELEFONO)));
        participante.setEsCreador(c.getInt(c.getColumnIndex(Constants.PARTICIPANTE_ES_CREADOR)) != 0);
        participante.setEsSinAsignar(c.getInt(c.getColumnIndex(Constants.PARTICIPANTE_ES_SIN_ASIGNAR)) != 0);
        return participante;
    }

    public Participante getById(int id) {
        Participante participante = null;
        db = dbhelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "
                + Constants.PARTICIPANTE_TABLENAME + " WHERE "
                + Constants.PARTICIPANTE_ID + " = " + String.valueOf(id), null);
        // Nos movemos con el cursor por cada resultado (deberia ser uno solo)
        while (c.moveToNext()) {
            // Y creamos el participante con los datos correspondientes
            participante = parseParticipanteFromCursor(c);
        }
        return participante;
    }

    public Participante getSinAsignar(int eventoId) {
        Participante participante = null;
        db = dbhelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "
                + Constants.PARTICIPANTE_TABLENAME + " WHERE "
                + Constants.PARTICIPANTE_EVENTO_FK + " = " + String.valueOf(eventoId) + " AND "
                + Constants.PARTICIPANTE_ES_SIN_ASIGNAR + " = 1", null);
        // Nos movemos con el cursor por cada resultado (deberia ser uno solo)
        while (c.moveToNext()) {
            // Y creamos el participante con los datos correspondientes
            participante = parseParticipanteFromCursor(c);
        }
        return participante;
    }

    public List<Participante> getAllDelEvento(int eventoId) {
        List<Participante> participantes = new ArrayList<>();
        db = dbhelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "
                + Constants.PARTICIPANTE_TABLENAME + " WHERE "
                + Constants.PARTICIPANTE_EVENTO_FK + " = " + String.valueOf(eventoId), null);
        // Nos movemos con el cursor por cada resultado
        while (c.moveToNext()) {
            // Y creamos el evento con los datos correspondientes
            Participante participante = parseParticipanteFromCursor(c);
            participantes.add(participante);
        }
        Collections.sort(participantes);
        return participantes;
    }

    public List<Participante> getAllDelEventoMenosSinAsignar(int eventoId) {
        List<Participante> participantes = new ArrayList<>();
        db = dbhelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "
                + Constants.PARTICIPANTE_TABLENAME + " WHERE "
                + Constants.PARTICIPANTE_EVENTO_FK + " = " + String.valueOf(eventoId) + " AND "
                + Constants.PARTICIPANTE_ES_SIN_ASIGNAR + " = 0", null);
        // Nos movemos con el cursor por cada resultado
        while (c.moveToNext()) {
            // Y creamos el evento con los datos correspondientes
            Participante participante = parseParticipanteFromCursor(c);
            participantes.add(participante);
        }
        Collections.sort(participantes);
        return participantes;
    }

    /**
     * Hace un insert en la DB donde en cada columna se ponen los valores en las variables de
     * instancia del Evento E pasado como parametro.
     *
     * @param p Evento a crear
     */
    @Override
    public long save(Participante p, int eventoId) {
        db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.PARTICIPANTE_NOMBRE, p.getNombreApellido());
        cv.put(Constants.PARTICIPANTE_EVENTO_FK, eventoId);
        cv.put(Constants.PARTICIPANTE_TELEFONO, p.getNumeroTel());
        cv.put(Constants.PARTICIPANTE_ES_CREADOR, p.esCreadorEvento());
        cv.put(Constants.PARTICIPANTE_ES_SIN_ASIGNAR, p.esSinAsignar());
        long id = db.insert(Constants.PARTICIPANTE_TABLENAME, null, cv);
        db.close();
        return id;
    }

    /**
     * Hace un delete en la DB donde se borrara la fila con id igual al de la
     * instancia del Participante e pasado como parametro.
     *
     * @param p Participante a eliminar
     */
    @Override
    public void delete(Participante p) {
        db = dbhelper.getWritableDatabase();
        db.delete(Constants.PARTICIPANTE_TABLENAME, Constants.PARTICIPANTE_ID + "=" + p.getId(), null);
        db.close();
    }

    @Override
    public void update(Participante p) {
        db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.PARTICIPANTE_NOMBRE, p.getNombreApellido());
        cv.put(Constants.PARTICIPANTE_TELEFONO, p.getNumeroTel());
        cv.put(Constants.PARTICIPANTE_ES_CREADOR, p.esCreadorEvento());
        cv.put(Constants.PARTICIPANTE_ES_SIN_ASIGNAR, p.esSinAsignar());
        db.update(Constants.PARTICIPANTE_TABLENAME, cv, Constants.PARTICIPANTE_ID + "=" + p.getId(), null);
        db.close();
    }

    public void createMockData(List<Evento> eventosYaGuardadosEnDb) {
        List<Participante> participantesMock = Participante.getParticipantesMock();
        for (Evento e : eventosYaGuardadosEnDb) {
            Participante creadorEvento = Participante.participanteCreadorEvento();
            Participante sinAsignar = Participante.getParticipanteSinAsignar();
            e.addParticipante(creadorEvento);
            e.addParticipante(sinAsignar);
            save(creadorEvento, e.getId());
            save(sinAsignar, e.getId());
            int totalPart = ThreadLocalRandom.current().nextInt(1, participantesMock.size() + 1);
            for (int i = 0; i < totalPart; i++) {
                int partRandom = ThreadLocalRandom.current().nextInt(0, participantesMock.size());
                Participante p = participantesMock.get(partRandom);
                participantesMock.remove(partRandom);
                e.addParticipante(p);
                save(p, e.getId());
            }
            participantesMock = Participante.getParticipantesMock();
        }
    }
}
