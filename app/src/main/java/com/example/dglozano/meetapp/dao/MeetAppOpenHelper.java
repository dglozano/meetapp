package com.example.dglozano.meetapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

/**
 * Utiliza el patron Singleton.
 * Contiene el DDL para la creacion de las tablas y, al extender SQLiteOpenHelper,
 * hereda los metodos para el acceso a la db
 * Created by dglozano on 24/10/17.
 */

public class MeetAppOpenHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_EVENTO = "CREATE TABLE "
            + Constants.EVENTO_TABLENAME + "("
            + Constants.EVENTO_ID +  " integer primary key autoincrement,"
            + Constants.EVENTO_NAME +  " string,"
            + Constants.EVENTO_LAT + " real,"
            + Constants.EVENTO_LNG + " real,"
            + Constants.EVENTO_FECHA + " string);";

    private static final String SQL_CREATE_PARTICIPANTE= "CREATE TABLE "
            + Constants.PARTICIPANTE_TABLENAME + "("
            + Constants.PARTICIPANTE_ID +  " integer primary key autoincrement,"
            + Constants.PARTICIPANTE_NOMBRE + " string,"
            + Constants.PARTICIPANTE_TELEFONO + " string,"
            + Constants.PARTICIPANTE_EVENTO_FK + " integer,"
            + "FOREIGN KEY("+Constants.PARTICIPANTE_EVENTO_FK+") " +
            "REFERENCES " + Constants.EVENTO_TABLENAME + "(" + Constants.EVENTO_ID + "));";

    private static final String SQL_CREATE_TAREA= "CREATE TABLE "
            + Constants.TAREA_TABLENAME + "("
            + Constants.TAREA_ID +  " integer primary key autoincrement,"
            + Constants.TAREA_TITULO + " string,"
            + Constants.TAREA_DESCRIPCION + " text,"
            + Constants.TAREA_ESTADO + " integer,"
            + Constants.TAREA_GASTO + " real,"
            + Constants.TAREA_EVENTO_FK + " integer,"
            + Constants.TAREA_PARTICIPANTE_FK + " integer,"
            + "FOREIGN KEY("+Constants.TAREA_EVENTO_FK+") " +
            "REFERENCES " + Constants.TAREA_TABLENAME + "(" + Constants.TAREA_ID + "),"
            + "FOREIGN KEY("+Constants.TAREA_PARTICIPANTE_FK+") " +
            "REFERENCES " + Constants.PARTICIPANTE_TABLENAME + "(" + Constants.PARTICIPANTE_ID + "));";

    private static final String SQL_CREATE_PAGO= "CREATE TABLE "
            + Constants.PAGO_TABLENAME + "("
            + Constants.PAGO_ID +  " integer primary key autoincrement,"
            + Constants.PAGO_MONTO + " real,"
            + Constants.PAGO_ESTADO + " integer,"
            + Constants.PAGO_PARTICIPANTE_COBRADOR_FK + " integer,"
            + Constants.PAGO_PARTICIPANTE_PAGADOR_FK + " integer,"
            + Constants.PAGO_EVENTO_FK + " integer,"
            + "FOREIGN KEY("+Constants.PAGO_EVENTO_FK+") " +
            "REFERENCES " + Constants.EVENTO_TABLENAME + "(" + Constants.EVENTO_ID + "),"
            + "FOREIGN KEY("+Constants.PAGO_PARTICIPANTE_COBRADOR_FK+") " +
            "REFERENCES " + Constants.PARTICIPANTE_TABLENAME + "(" + Constants.PARTICIPANTE_ID + "),"
            + "FOREIGN KEY("+Constants.PAGO_PARTICIPANTE_PAGADOR_FK+") " +
            "REFERENCES " + Constants.PARTICIPANTE_TABLENAME + "(" + Constants.PARTICIPANTE_ID + "));";

    private static MeetAppOpenHelper _INSTANCE;

    private MeetAppOpenHelper(Context ctx, String dbname, Integer version){
        super(ctx, dbname, null, version);
    }

    public static MeetAppOpenHelper getInstance(Context ctx, String dbname, Integer version){
        if(_INSTANCE==null) _INSTANCE = new MeetAppOpenHelper(ctx, dbname, version);
        return _INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_EVENTO);
        sqLiteDatabase.execSQL(SQL_CREATE_PARTICIPANTE);
        sqLiteDatabase.execSQL(SQL_CREATE_TAREA);
        sqLiteDatabase.execSQL(SQL_CREATE_PAGO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + Constants.PAGO_TABLENAME);
        sqLiteDatabase.execSQL("drop table if exists " + Constants.TAREA_TABLENAME);
        sqLiteDatabase.execSQL("drop table if exists " + Constants.PARTICIPANTE_TABLENAME);
        sqLiteDatabase.execSQL("drop table if exists " + Constants.EVENTO_TABLENAME);
        onCreate(sqLiteDatabase);
    }
}