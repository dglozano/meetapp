package com.example.dglozano.meetapp.dao;

class Constants {
    public static final String DATABASE_NAME = "meetapp";
    public static final int DATABASE_VERSION = 130;

    public static final String EVENTO_TABLENAME = "evento";
    public static final String EVENTO_ID = "_id";
    public static final String EVENTO_NAME = "nombre";
    public static final String EVENTO_LAT = "latitud";
    public static final String EVENTO_LNG = "longitud";
    public static final String EVENTO_DIVISION_YA_REALIZADA = "division_ya_realizada";
    public static final String EVENTO_GASTO_TOTAL = "gasto_total";
    public static final String EVENTO_GASTO_POR_PARTICIPANTE = "gasto_por_participante";
    public static final String EVENTO_FECHA = "fecha";

    public static final String TAREA_TABLENAME = "tarea";
    public static final String TAREA_ID = "_id";
    public static final String TAREA_TITULO = "titulo";
    public static final String TAREA_ESTADO = "estado";
    public static final String TAREA_DESCRIPCION = "descripcion";
    public static final String TAREA_GASTO = "gasto";
    public static final String TAREA_EVENTO_FK = "evento_fk";
    public static final String TAREA_PARTICIPANTE_FK = "participante_fk";

    public static final String PARTICIPANTE_TABLENAME = "participante";
    public static final String PARTICIPANTE_ID = "_id";
    public static final String PARTICIPANTE_NOMBRE = "nombre";
    public static final String PARTICIPANTE_TELEFONO = "telefono";
    public static final String PARTICIPANTE_ES_SIN_ASIGNAR = "es_sin_asignar";
    public static final String PARTICIPANTE_ES_CREADOR = "es_creador";
    public static final String PARTICIPANTE_EVENTO_FK = "evento_fk";

    public static final String PAGO_TABLENAME = "pago";
    public static final String PAGO_ID = "_id";
    public static final String PAGO_MONTO = "monto";
    public static final String PAGO_EVENTO_FK = "evento_fk";
    public static final String PAGO_PARTICIPANTE_COBRADOR_FK = "participante_cob_fk";
    public static final String PAGO_PARTICIPANTE_PAGADOR_FK = "participante_pag_fk";

    public static final String DATE_FORMAT = "dd/MM/yyyy";
}