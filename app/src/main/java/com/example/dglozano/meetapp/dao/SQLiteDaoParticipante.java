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
    private final Context context;
    private final MeetAppOpenHelper dbhelper;

    /**
     * Constructor. Setea el contexto y obtiene la instancia del singleton del dbhelper
     *
     * @param c
     */
    public SQLiteDaoParticipante(Context c) {
        context = c;
        dbhelper = MeetAppOpenHelper.getInstance(context, Constants.DATABASE_NAME,
                Constants.DATABASE_VERSION);
    }

    /**
     * Hace un SELECT * FROM TABLE Participante y, por cada resultado de la query, crea un
     * Participante, le setea los datos y la agrega a la lista a retornar.
     * <p>
     * NOTA: LA HAGO SOLO PARA CUMPLIR LA INTERFACE, PERO NO SE USARIA PORQUE
     * TRAE TODOS LOS PARTICIPANTES DE TODOS LOS EVENTOS.
     *
     * @return Lista de eventos en la tabla evento
     */
    @Override
    public List<Participante> getAll() {
        List<Participante> participantes = new ArrayList<>();
        db = dbhelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Constants.PARTICIPANTE_TABLENAME, null);
        // Nos movemos con el cursor por cada resultado
        while(c.moveToNext()) {
            // Y creamos el evento con los datos correspondientes
            Participante participante = parseParticipanteFromCursor(c);
            participantes.add(participante);
        }
        c.close();
        db.close();
        Collections.sort(participantes);
        return participantes;
    }

    @NonNull
    private Participante parseParticipanteFromCursor(Cursor c) {
        Participante participante = new Participante();
        participante.setId(c.getInt(c.getColumnIndex(Constants.PARTICIPANTE_ID)));
        participante.setNombreApellido(c.getString(c.getColumnIndex(Constants.PARTICIPANTE_NOMBRE)));
        participante.setNumero(c.getString(c.getColumnIndex(Constants.PARTICIPANTE_TELEFONO)));
        return participante;
    }

    public Participante getById(int id) {
        Participante participante = null;
        db = dbhelper.getReadableDatabase();
        /**
         * FIXME: ESTO IRIA CON UN ? COMO PARAM Y UNA ARRAY DE STRING CON EL ID PERO A.S. TIENE UN BUG
         */
        Cursor c = db.rawQuery("SELECT * FROM "
                + Constants.PARTICIPANTE_TABLENAME + " WHERE "
                + Constants.PARTICIPANTE_ID + " = " + String.valueOf(id), null);
        // Nos movemos con el cursor por cada resultado (deberia ser uno solo)
        while(c.moveToNext()) {
            // Y creamos el participante con los datos correspondientes
            participante = parseParticipanteFromCursor(c);
        }
        return participante;
    }

    public List<Participante> getAllDelEvento(int eventoId) {
        List<Participante> participantes = new ArrayList<>();
        db = dbhelper.getReadableDatabase();
        /**
         * FIXME: ESTO IRIA CON UN ? COMO PARAM Y UNA ARRAY DE STRING CON EL ID PERO A.S. TIENE UN BUG
         */
        Cursor c = db.rawQuery("SELECT * FROM "
                + Constants.PARTICIPANTE_TABLENAME + " WHERE "
                + Constants.PARTICIPANTE_EVENTO_FK + " = " + String.valueOf(eventoId), null);
        // Nos movemos con el cursor por cada resultado
        while(c.moveToNext()) {
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
    public void save(Participante p, int eventoId) {
        db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.PARTICIPANTE_NOMBRE, p.getNombreApellido());
        cv.put(Constants.PARTICIPANTE_EVENTO_FK, eventoId);
        cv.put(Constants.PARTICIPANTE_TELEFONO, p.getNumeroTel());
        db.insert(Constants.PARTICIPANTE_TABLENAME, null, cv);
        db.close();
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
        //TODO VER QUE HACER SI HAY PAGOS CON ESTE PARTICIPANTE
        db.delete(Constants.PARTICIPANTE_TABLENAME, Constants.PARTICIPANTE_ID + "=" + p.getId(), null);
        db.close();
    }

    @Override
    public void update(Participante p) {
        db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.PARTICIPANTE_NOMBRE, p.getNombreApellido());
        cv.put(Constants.PARTICIPANTE_TELEFONO, p.getNumeroTel());
        db.update(Constants.PARTICIPANTE_TABLENAME, cv, Constants.PARTICIPANTE_ID + "=" + p.getId(), null);
        db.close();
    }

    public void createMockData(List<Evento> eventosYaGuardadosEnDb) {
        List<Participante> participantesMock = Participante.getParticipantesMock();
        for(Evento e : eventosYaGuardadosEnDb) {
            int totalPart = ThreadLocalRandom.current().nextInt(1, participantesMock.size() + 1);
            for(int i = 0; i < totalPart; i++) {
                int partRandom = ThreadLocalRandom.current().nextInt(0, participantesMock.size());
                Participante p = participantesMock.get(partRandom);
                participantesMock.remove(partRandom);
                e.addParticipante(p);
                save(p, e.getId());
            }
            participantesMock = Participante.getParticipantesMock();
        }
    }

    //TODO UPDATE?
}
