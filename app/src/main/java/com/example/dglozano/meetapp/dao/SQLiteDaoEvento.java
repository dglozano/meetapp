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
import com.example.dglozano.meetapp.modelo.Pago;
import com.example.dglozano.meetapp.modelo.Participante;
import com.example.dglozano.meetapp.modelo.Tarea;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by diegogarcialozano on 29/10/17.
 */

public class SQLiteDaoEvento implements DaoEvento {

    private SQLiteDatabase db;
    private final Context context;
    private final MeetAppOpenHelper dbhelper;
    private SQLiteDaoParticipante daoParticipante;
    private SQLiteDaoTarea daoTarea;
    private SQLiteDaoPago daoPago;

    /**
     * Constructor. Setea el contexto y obtiene la instancia del singleton del dbhelper
     *
     * @param c
     */
    public SQLiteDaoEvento(Context c) {
        context = c;
        dbhelper = MeetAppOpenHelper.getInstance(context, Constants.DATABASE_NAME,
                Constants.DATABASE_VERSION);
        daoParticipante = new SQLiteDaoParticipante(c);
        daoTarea = new SQLiteDaoTarea(c);
        daoPago = new SQLiteDaoPago(c);
    }

    /**
     * Hace un SELECT * FROM TABLE evento y, por cada resultado de la query, crea un
     * Evento, le setea los datos y la agrega a la lista a retornar.
     *
     * @return Lista de eventos en la tabla evento
     */
    @Override
    public List<Evento> getAll() {
        List<Evento> eventos = new ArrayList<>();
        db = dbhelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Constants.EVENTO_TABLENAME, null);
        // Nos movemos con el cursor por cada resultado
        while(c.moveToNext()) {
            // Y creamos el evento con los datos correspondientes
            Evento evento = parseEventoFromCursor(c);
            eventos.add(evento);
        }
        c.close();
        db.close();
        return eventos;
    }

    @NonNull
    private Evento parseEventoFromCursor(Cursor c) {
        Evento evento = new Evento();
        evento.setId(c.getInt(c.getColumnIndex(Constants.EVENTO_ID)));
        evento.setNombre(c.getString(c.getColumnIndex(Constants.EVENTO_NAME)));
        Double lat = c.getDouble(c.getColumnIndex(Constants.EVENTO_LAT));
        Double len = c.getDouble(c.getColumnIndex(Constants.EVENTO_LNG));
        LatLng lugar = new LatLng(lat, len);
        evento.setLugar(lugar);
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        String fechaString = c.getString(c.getColumnIndex(Constants.EVENTO_FECHA));
        try {
            evento.setFecha(sdf.parse(fechaString));
        } catch(ParseException e) {
            e.printStackTrace();
        }
        int divisionHechaInt = c.getInt(c.getColumnIndex(Constants.EVENTO_DIVISION_YA_REALIZADA));
        boolean divisionHechaFlag = (divisionHechaInt == 0) ? false : true;
        evento.setDivisionGastosYaHecha(divisionHechaFlag);
        evento.setGastosTotales(c.getDouble(c.getColumnIndex(Constants.EVENTO_GASTO_TOTAL)));
        evento.setGastosPorParticipante(c.getDouble(c.getColumnIndex(Constants.EVENTO_GASTO_POR_PARTICIPANTE)));
        evento.addAllParticipantes(daoParticipante.getAllDelEvento(evento.getId()));
        evento.addAllTareas(daoTarea.getAllDelEvento(evento.getId()));
        evento.addAllPagos(daoPago.getAllDelEvento(evento.getId()));
        return evento;
    }

    public Evento getById(int id) {
        Evento evento = null;
        db = dbhelper.getReadableDatabase();
        /**
         * FIXME: ESTO IRIA CON UN ? COMO PARAM Y UNA ARRAY DE STRING CON EL ID PERO A.S. TIENE UN BUG
         */
        Cursor c = db.rawQuery("SELECT * FROM "
                + Constants.EVENTO_TABLENAME + " WHERE "
                + Constants.EVENTO_ID + " = " + String.valueOf(id), null);
        // Nos movemos con el cursor por cada resultado (deberia ser uno solo)
        while(c.moveToNext()) {
            // Y creamos el evento con los datos correspondientes
            evento = parseEventoFromCursor(c);
        }
        return evento;
    }

    /**
     * Hace un insert en la DB donde en cada columna se ponen los valores en las variables de
     * instancia del Evento E pasado como parametro.
     *
     * @param e Evento a crear
     */
    @Override
    public long save(Evento e) {
        db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.EVENTO_NAME, e.getNombre());
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        cv.put(Constants.EVENTO_FECHA, sdf.format(e.getFecha()));
        cv.put(Constants.EVENTO_LAT, e.getLugar().latitude);
        cv.put(Constants.EVENTO_LNG, e.getLugar().longitude);
        cv.put(Constants.EVENTO_GASTO_TOTAL, e.getGastosTotales());
        cv.put(Constants.EVENTO_GASTO_POR_PARTICIPANTE, e.getGastosPorParticipante());
        cv.put(Constants.EVENTO_DIVISION_YA_REALIZADA, e.isDivisionGastosYaHecha());
        long id = db.insert(Constants.EVENTO_TABLENAME, null, cv);
        db.close();
        return id;
    }

    @Override
    public void update(Evento e) {
        db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.EVENTO_NAME, e.getNombre());
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        cv.put(Constants.EVENTO_FECHA, sdf.format(e.getFecha()));
        cv.put(Constants.EVENTO_LAT, e.getLugar().latitude);
        cv.put(Constants.EVENTO_LNG, e.getLugar().longitude);
        cv.put(Constants.EVENTO_GASTO_TOTAL, e.getGastosTotales());
        cv.put(Constants.EVENTO_GASTO_POR_PARTICIPANTE, e.getGastosPorParticipante());
        cv.put(Constants.EVENTO_DIVISION_YA_REALIZADA, e.isDivisionGastosYaHecha());
        for(Participante p : e.getParticipantes())
            if(p.getId() == null)
                daoParticipante.save(p, e.getId());
            else
                daoParticipante.update(p);
        for(Tarea t : e.getTareas())
            if(t.getId() == null)
                daoTarea.save(t, e.getId());
            else
                daoTarea.update(t);
        for(Pago p : e.getPagos())
            if(p.getId() == null)
                daoPago.save(p, e.getId());
            else
                daoPago.update(p);
        db.update(Constants.EVENTO_TABLENAME, cv, Constants.EVENTO_ID + "=" + e.getId(), null);
        db.close();
    }

    /**
     * Hace un delete en la DB donde se borrara la fila con id igual al de la
     * instancia del Evento e pasado como parametro.
     *
     * @param e Evento a eliminar
     */
    @Override
    public void delete(Evento e) {
        db = dbhelper.getWritableDatabase();
        //TODO BORRAR PARTICIPANTES PAGOS Y TAREAS PRIMERO
        db.delete(Constants.EVENTO_TABLENAME, Constants.EVENTO_ID + "=" + e.getId(), null);
        db.close();
    }

    public void createMockData() {
        List<Evento> eventos = Evento.getEventosMock();
        for(Evento e : eventos) {
            save(e);
        }
    }

    //TODO UPDATE?
}
