package com.example.dglozano.meetapp.dao;

/**
 * Created by dglozano on 08/02/18.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.dglozano.meetapp.modelo.Evento;
import com.example.dglozano.meetapp.modelo.Participante;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diegogarcialozano on 29/10/17.
 */

public class SQLiteDaoParticipante implements Dao<Participante> {

    private SQLiteDatabase db;
    private final Context context;
    private final MeetAppOpenHelper dbhelper;

    /**
     * Constructor. Setea el contexto y obtiene la instancia del singleton del dbhelper
     * @param c
     */
    public SQLiteDaoParticipante(Context c){
        context = c;
        //TODO BORRAR CUANDO SAQUEMOS MOCK DATA
        dbhelper = MeetAppOpenHelper.getInstance(context, Constants.DATABASE_NAME,
                Constants.DATABASE_VERSION);
        this.createMockData();
    }

    /**
     * Hace un SELECT * FROM TABLE Participante y, por cada resultado de la query, crea un
     * Participante, le setea los datos y la agrega a la lista a retornar.
     *
     * NOTA: LA HAGO SOLO PARA CUMPLIR LA INTERFACE, PERO NO SE USARIA PORQUE
     * TRAE TODOS LOS PARTICIPANTES DE TODOS LOS EVENTOS.
     * @return Lista de eventos en la tabla evento
     */
    @Override
    public List<Participante> getAll() {
        List<Participante> participantes = new ArrayList<>();
        db = dbhelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Constants.PARTICIPANTE_TABLENAME, null);
        // Nos movemos con el cursor por cada resultado
        while(c.moveToNext()){
            // Y creamos el evento con los datos correspondientes
            Participante participante = parseParticipanteFromCursor(c);
            participantes.add(participante);
        }
        c.close();
        db.close();
        return participantes;
    }

    @NonNull
    private Participante parseParticipanteFromCursor(Cursor c) {
        Participante participante = new Participante();
        participante.setId(c.getInt(c.getColumnIndex(Constants.PARTICIPANTE_ID)));
        participante.setNombreApellido(c.getString(c.getColumnIndex(Constants.PARTICIPANTE_NOMBRE)));
        participante.setPictureId(c.getInt(c.getColumnIndex(Constants.PARTICIPANTE_PICTURE_ID)));
        return participante;
    }

    public Participante getById(int id){
        Participante participante = null;
        db = dbhelper.getReadableDatabase();
        /**
         * FIXME: ESTO IRIA CON UN ? COMO PARAM Y UNA ARRAY DE STRING CON EL ID PERO A.S. TIENE UN BUG
         */
        Cursor c = db.rawQuery("SELECT * FROM "
                + Constants.PARTICIPANTE_TABLENAME + " WHERE "
                + Constants.PARTICIPANTE_ID + " = " +String.valueOf(id),null);
        // Nos movemos con el cursor por cada resultado (deberia ser uno solo)
        while(c.moveToNext()){
            // Y creamos el evento con los datos correspondientes
            participante = parseParticipanteFromCursor(c);
        }
        return participante;
    }

    public Participante getAllParticipantesDelEvento(int eventoId){
        Participante participante = null;
        db = dbhelper.getReadableDatabase();
        /**
         * FIXME: ESTO IRIA CON UN ? COMO PARAM Y UNA ARRAY DE STRING CON EL ID PERO A.S. TIENE UN BUG
         */
        Cursor c = db.rawQuery("SELECT * FROM "
                + Constants.PARTICIPANTE_TABLENAME + " WHERE "
                + Constants.PARTICIPANTE_EVENTO_FK + " = " +String.valueOf(eventoId),null);
        // Nos movemos con el cursor por cada resultado
        while(c.moveToNext()){
            // Y creamos el evento con los datos correspondientes
            participante = parseParticipanteFromCursor(c);
        }
        return participante;
    }

    /**
     * Hace un insert en la DB donde en cada columna se ponen los valores en las variables de
     * instancia del Evento E pasado como parametro.
     * @param p Evento a crear
     */
    @Override
    public void save(Participante p) {
        db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.PARTICIPANTE_NOMBRE, p.getNombreApellido());
        cv.put(Constants.PARTICIPANTE_PICTURE_ID, p.getPictureId());
        db.insert(Constants.PARTICIPANTE_TABLENAME,null, cv);
        db.close();
    }

    /**
     * Hace un delete en la DB donde se borrara la fila con id igual al de la
     * instancia del Participante e pasado como parametro.
     * @param p Participante a eliminar
     */
    @Override
    public void delete(Participante p) {
        db = dbhelper.getWritableDatabase();
        //TODO VER QUE HACER SI HAY PAGOS CON ESTE PARTICIPANTE
        db.delete(Constants.PARTICIPANTE_TABLENAME, Constants.PARTICIPANTE_ID + "=" + p.getId(), null);
        db.close();
    }

    private void createMockData(){
        List<Evento> eventos = Evento.getEventosMock();
        for(Evento e: eventos){
            for(Participante p: e.getParticipantes()){
                save(p);
            }
        }
    }

    //TODO UPDATE?
}
