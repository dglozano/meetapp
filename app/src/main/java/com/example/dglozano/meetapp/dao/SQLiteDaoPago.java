package com.example.dglozano.meetapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.dglozano.meetapp.modelo.EstadoPago;
import com.example.dglozano.meetapp.modelo.EstadoTarea;
import com.example.dglozano.meetapp.modelo.Evento;
import com.example.dglozano.meetapp.modelo.Pago;
import com.example.dglozano.meetapp.modelo.Participante;
import com.example.dglozano.meetapp.modelo.Tarea;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SQLiteDaoPago implements DaoEventoMember<Pago> {

    private SQLiteDatabase db;
    private final Context context;
    private final MeetAppOpenHelper dbhelper;
    private SQLiteDaoParticipante daoParticipante;
    private SQLiteDaoEvento daoEvento;

    /**
     * Constructor. Setea el contexto y obtiene la instancia del singleton del dbhelper
     * @param c
     */
    public SQLiteDaoPago(Context c){
        context = c;
        dbhelper = MeetAppOpenHelper.getInstance(context, Constants.DATABASE_NAME,
                Constants.DATABASE_VERSION);
        daoParticipante = new SQLiteDaoParticipante(c);
        daoEvento = new SQLiteDaoEvento(c);
    }

    /**
     * Hace un SELECT * FROM TABLE PAGO y, por cada resultado de la query, crea un
     * PAGO, le setea los datos y la agrega a la lista a retornar.
     *
     * NOTA: LA HAGO SOLO PARA CUMPLIR LA INTERFACE, PERO NO SE USARIA PORQUE
     * TRAE TODAS LOS PAGOS DE TODOS LOS EVENTOS.
     * @return Lista de PAGOS en la tabla tareas
     */
    @Override
    public List<Pago> getAll() {
        List<Pago> pagos = new ArrayList<>();
        db = dbhelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Constants.PAGO_TABLENAME, null);
        // Nos movemos con el cursor por cada resultado
        while(c.moveToNext()){
            // Y creamos el pago con los datos correspondientes
            Pago pago = parsePagoFromCursor(c);
            pagos.add(pago);
        }
        c.close();
        db.close();
        return pagos;
    }

    @NonNull
    private Pago parsePagoFromCursor(Cursor c) {
        Pago pago = new Pago();
        pago.setId(c.getInt(c.getColumnIndex(Constants.PAGO_ID)));
        pago.setMonto(c.getDouble(c.getColumnIndex(Constants.PAGO_MONTO)));
        int estadoOrdinal = c.getInt(c.getColumnIndex(Constants.PAGO_ESTADO));
        pago.setEstadoPago(EstadoPago.values()[estadoOrdinal]);
        int idParticipantePagador = c.getInt(c.getColumnIndex(Constants.PAGO_PARTICIPANTE_PAGADOR_FK));
        int idParticipanteCobrador = c.getInt(c.getColumnIndex(Constants.PAGO_PARTICIPANTE_COBRADOR_FK));
        Participante cobrador = daoParticipante.getById(idParticipanteCobrador);
        Participante pagador = daoParticipante.getById(idParticipantePagador);
        pago.setCobrador(cobrador);
        pago.setPagador(pagador);
        return pago;
    }

    public Pago getById(int id){
        Pago pago = null;
        db = dbhelper.getReadableDatabase();
        /**
         * FIXME: ESTO IRIA CON UN ? COMO PARAM Y UNA ARRAY DE STRING CON EL ID PERO A.S. TIENE UN BUG
         */
        Cursor c = db.rawQuery("SELECT * FROM "
                + Constants.PAGO_TABLENAME + " WHERE "
                + Constants.PAGO_ID + " = " +String.valueOf(id),null);
        // Nos movemos con el cursor por cada resultado (deberia ser uno solo)
        while(c.moveToNext()){
            // Y creamos el tarea con los datos correspondientes
            pago = parsePagoFromCursor(c);
        }
        return pago;
    }

    @Override
    public List<Pago> getAllDelEvento(int eventoId){
        List<Pago> pagos = new ArrayList<>();
        db = dbhelper.getReadableDatabase();
        /**
         * FIXME: ESTO IRIA CON UN ? COMO PARAM Y UNA ARRAY DE STRING CON EL ID PERO A.S. TIENE UN BUG
         */
        Cursor c = db.rawQuery("SELECT * FROM "
                + Constants.PAGO_TABLENAME + " WHERE "
                + Constants.PAGO_EVENTO_FK + " = " +String.valueOf(eventoId),null);
        // Nos movemos con el cursor por cada resultado
        while(c.moveToNext()){
            // Y creamos el tarea con los datos correspondientes
            Pago pago = parsePagoFromCursor(c);
            pagos.add(pago);
        }
        return pagos;
    }

    /**
     * Hace un insert en la DB donde en cada columna se ponen los valores en las variables de
     * instancia del Pago P pasado como parametro.
     * @param p Pago a crear
     */
    @Override
    public void save(Pago p, int eventoId) {
        db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.PAGO_MONTO, p.getMonto());
        cv.put(Constants.PAGO_ESTADO, p.getEstadoPago().ordinal());
        cv.put(Constants.PAGO_EVENTO_FK, eventoId);
        cv.put(Constants.PAGO_PARTICIPANTE_COBRADOR_FK, p.getCobrador().getId());
        cv.put(Constants.PAGO_PARTICIPANTE_PAGADOR_FK, p.getPagador().getId());
        db.insert(Constants.PAGO_TABLENAME,null, cv);
        db.close();
    }

    /**
     * Hace un delete en la DB donde se borrara la fila con id igual al de la
     * instancia del Pago p pasado como parametro.
     * @param p Pago a eliminar
     */
    @Override
    public void delete(Pago p) {
        db = dbhelper.getWritableDatabase();
        //TODO VER QUE HACER SI HAY PAGOS CON ESTE PARTICIPANTE
        db.delete(Constants.PAGO_TABLENAME, Constants.PAGO_ID + "=" + p.getId(), null);
        db.close();
    }

    public void createMockData(List<Evento> eventosYaGuardadosEnDb){
        for(Evento e: eventosYaGuardadosEnDb){
            // TODO GENERAR PAGOS DE LOS EVENTOS MOCKEADOS.
        }
    }

    //TODO UPDATE?
}
