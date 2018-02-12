package com.example.dglozano.meetapp.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dglozano on 01/02/18.
 */

public class Pago {

    private int id;
    private EstadoPago estadoPago;
    private Participante pagador;
    private Participante cobrador;
    private double monto;

    public Pago(EstadoPago estadoPago, Participante pagador, Participante cobrador, double monto) {
        this.estadoPago = estadoPago;
        this.pagador = pagador;
        this.cobrador = cobrador;
        this.monto = monto;
    }

    public Pago(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    public Participante getPagador() {
        return pagador;
    }

    public void setPagador(Participante pagador) {
        this.pagador = pagador;
    }

    public Participante getCobrador() {
        return cobrador;
    }

    public void setCobrador(Participante cobrador) {
        this.cobrador = cobrador;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public boolean matches (String query){
        boolean matches = false;
        if(this.getPagador().matches(query) || this.getCobrador().matches(query)){
            matches = true;
        }
        if(Double.toString(this.getMonto()).contains(query)){
            matches = true;
        }
        return matches;
    }
}
