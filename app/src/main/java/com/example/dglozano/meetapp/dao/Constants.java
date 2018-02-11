package com.example.dglozano.meetapp.dao;

class Constants {
    public static final String DATABASE_NAME = "meetapp";
    public static final int DATABASE_VERSION = 2;

    public static final String EVENTO_TABLENAME = "evento";
    public static final String EVENTO_ID = "_id";
    public static final String EVENTO_NAME = "nombre";
    public static final String EVENTO_LAT = "latitud";
    public static final String EVENTO_LNG = "longitud";
    public static final String EVENTO_FECHA = "fecha";

    public static final String ESTADO_TAREA_TABLENAME = "estado_tarea";
    public static final String ESTADO_TAREA_ID = "_id";
    public static final String ESTADO_TAREA_NOMBRE = "nombre_estado";

    public static final String ESTADO_PAGO_TABLENAME = "estado_pago";
    public static final String ESTADO_PAGO_ID = "_id";
    public static final String ESTADO_PAGO_NOMBRE = "nombre_estado";

    public static final String TAREA_TABLENAME = "tarea";
    public static final String TAREA_ID = "_id";
    public static final String TAREA_TITULO = "titulo";
    public static final String TAREA_ESTADO_FK = "estado_fk";
    public static final String TAREA_DESCRIPCION = "descripcion";
    public static final String TAREA_EVENTO_FK = "evento_fk";
    public static final String TAREA_PARTICIPANTE_FK = "participante_fk";

    public static final String PARTICIPANTE_TABLENAME = "participante";
    public static final String PARTICIPANTE_ID = "_id";
    public static final String PARTICIPANTE_NOMBRE = "nombre";
    public static final String PARTICIPANTE_EVENTO_FK = "evento_fk";
    public static final String PARTICIPANTE_PICTURE_ID = "picutre_id";

    public static final String PAGO_TABLENAME = "pago";
    public static final String PAGO_ID = "_id";
    public static final String PAGO_MONTO = "monto";
    public static final String PAGO_ESTADO_FK = "estado_fk";
    public static final String PAGO_PARTICIPANTE_COBRADOR_FK = "participante_cob_fk";
    public static final String PAGO_PARTICIPANTE_PAGADOR_FK = "participante_pag_fk";

    public static final String DATE_FORMAT = "dd/MM/yyyy";
}