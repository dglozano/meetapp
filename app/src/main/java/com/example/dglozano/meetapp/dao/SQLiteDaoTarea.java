package com.example.dglozano.meetapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.dglozano.meetapp.modelo.EstadoTarea;
import com.example.dglozano.meetapp.modelo.Evento;
import com.example.dglozano.meetapp.modelo.Participante;
import com.example.dglozano.meetapp.modelo.Tarea;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SQLiteDaoTarea implements DaoEventoMember<Tarea> {

    private SQLiteDatabase db;
    private final Context context;
    private final MeetAppOpenHelper dbhelper;
    private SQLiteDaoParticipante daoParticipante;

    /**
     * Constructor. Setea el contexto y obtiene la instancia del singleton del dbhelper
     * @param c
     */
    public SQLiteDaoTarea(Context c){
        context = c;
        dbhelper = MeetAppOpenHelper.getInstance(context, Constants.DATABASE_NAME,
                Constants.DATABASE_VERSION);
        daoParticipante = new SQLiteDaoParticipante(c);
    }

    /**
     * Hace un SELECT * FROM TABLE Tarea y, por cada resultado de la query, crea un
     * Tarea, le setea los datos y la agrega a la lista a retornar.
     *
     * NOTA: LA HAGO SOLO PARA CUMPLIR LA INTERFACE, PERO NO SE USARIA PORQUE
     * TRAE TODAS LAS TAREAS DE TODOS LOS EVENTOS.
     * @return Lista de tareas en la tabla tareas
     */
    @Override
    public List<Tarea> getAll() {
        List<Tarea> tareas = new ArrayList<>();
        db = dbhelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Constants.TAREA_TABLENAME, null);
        // Nos movemos con el cursor por cada resultado
        while(c.moveToNext()){
            // Y creamos el tarea con los datos correspondientes
            Tarea tarea = parseTareaFromCursor(c);
            tareas.add(tarea);
        }
        c.close();
        db.close();
        return tareas;
    }

    @NonNull
    private Tarea parseTareaFromCursor(Cursor c) {
        Tarea tarea = new Tarea();
        tarea.setId(c.getInt(c.getColumnIndex(Constants.TAREA_ID)));
        tarea.setDescripcion(c.getString(c.getColumnIndex(Constants.TAREA_DESCRIPCION)));
        tarea.setTitulo(c.getString(c.getColumnIndex(Constants.TAREA_TITULO)));
        int estadoOrdinal = c.getInt(c.getColumnIndex(Constants.TAREA_ESTADO));
        tarea.setEstadoTarea(EstadoTarea.values()[estadoOrdinal]);
        if(tarea.getEstadoTarea() == EstadoTarea.SIN_ASIGNAR){
            tarea.setPersonaAsignada(Participante.getParticipanteSinAsignar());
        } else {
            int idParticipanteAsignado = c.getInt(c.getColumnIndex(Constants.TAREA_PARTICIPANTE_FK));
            Participante p = daoParticipante.getById(idParticipanteAsignado);
            tarea.setPersonaAsignada(p);
        }
        return tarea;
    }

    public Tarea getById(int id){
        Tarea tarea = null;
        db = dbhelper.getReadableDatabase();
        /**
         * FIXME: ESTO IRIA CON UN ? COMO PARAM Y UNA ARRAY DE STRING CON EL ID PERO A.S. TIENE UN BUG
         */
        Cursor c = db.rawQuery("SELECT * FROM "
                + Constants.TAREA_TABLENAME + " WHERE "
                + Constants.TAREA_ID + " = " +String.valueOf(id),null);
        // Nos movemos con el cursor por cada resultado (deberia ser uno solo)
        while(c.moveToNext()){
            // Y creamos el tarea con los datos correspondientes
            tarea = parseTareaFromCursor(c);
        }
        return tarea;
    }

    @Override
    public List<Tarea> getAllDelEvento(int eventoId){
        List<Tarea> tareas = new ArrayList<>();
        db = dbhelper.getReadableDatabase();
        /**
         * FIXME: ESTO IRIA CON UN ? COMO PARAM Y UNA ARRAY DE STRING CON EL ID PERO A.S. TIENE UN BUG
         */
        Cursor c = db.rawQuery("SELECT * FROM "
                + Constants.TAREA_TABLENAME + " WHERE "
                + Constants.TAREA_EVENTO_FK + " = " +String.valueOf(eventoId),null);
        // Nos movemos con el cursor por cada resultado
        while(c.moveToNext()){
            // Y creamos el tarea con los datos correspondientes
            Tarea tarea = parseTareaFromCursor(c);
            tareas.add(tarea);
        }
        return tareas;
    }

    /**
     * Hace un insert en la DB donde en cada columna se ponen los valores en las variables de
     * instancia del Tarea T pasado como parametro.
     * @param t Tarea a crear
     */
    @Override
    public void save(Tarea t, int eventoId) {
        db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.TAREA_DESCRIPCION, t.getDescripcion());
        cv.put(Constants.TAREA_TITULO, t.getTitulo());
        cv.put(Constants.TAREA_EVENTO_FK, eventoId);
        cv.put(Constants.TAREA_ESTADO, t.getEstadoTarea().ordinal());
        if(t.getEstadoTarea() != EstadoTarea.SIN_ASIGNAR){
            cv.put(Constants.TAREA_PARTICIPANTE_FK, t.getPersonaAsignada().getId());
        }
        db.insert(Constants.TAREA_TABLENAME,null, cv);
        db.close();
    }

    /**
     * Hace un delete en la DB donde se borrara la fila con id igual al de la
     * instancia del Tarea e pasado como parametro.
     * @param t Tarea a eliminar
     */
    @Override
    public void delete(Tarea t) {
        db = dbhelper.getWritableDatabase();
        //TODO VER QUE HACER SI HAY PAGOS CON ESTE PARTICIPANTE
        db.delete(Constants.TAREA_TABLENAME, Constants.TAREA_ID + "=" + t.getId(), null);
        db.close();
    }

    public void createMockData(List<Evento> eventosYaGuardadosEnDb){
        List<Tarea> tareasMock = Tarea.getTareasMock();
        for(Evento e: eventosYaGuardadosEnDb){
            int totalTareas = ThreadLocalRandom.current().nextInt(1, tareasMock.size() + 1);
            List<Participante> listaParticipantes = daoParticipante.getAllDelEvento(e.getId());
            for(int i = 0; i < totalTareas; i++){
                int tareaRandom = ThreadLocalRandom.current().nextInt(0, totalTareas);
                int estadoRandom = ThreadLocalRandom.current().nextInt(0, EstadoTarea.values().length);
                int partRandom = ThreadLocalRandom.current().nextInt(0, listaParticipantes.size());

                Tarea tarea = tareasMock.get(tareaRandom);
                tarea.setEstadoTarea(EstadoTarea.values()[estadoRandom]);

                if(tarea.getEstadoTarea() == EstadoTarea.SIN_ASIGNAR){
                    tarea.setPersonaAsignada(Participante.getParticipanteSinAsignar());
                } else {
                    tarea.setPersonaAsignada(listaParticipantes.get(partRandom));
                }
                save(tarea, e.getId());
            }
        }
    }

    //TODO UPDATE?
}
