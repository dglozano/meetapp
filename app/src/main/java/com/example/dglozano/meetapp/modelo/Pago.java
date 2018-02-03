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

    public static List<Pago> getPagosMock() {
        List<Pago> listaPagosMock = new ArrayList<>();
        List<Participante> listaParticipantesMock = Participante.getParticipantesMock();

        Participante pag = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        Participante cob = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        Pago pago = new Pago(EstadoPago.NO_PAGADO, pag, cob, 1225);
        listaPagosMock.add(pago);

        pag = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        cob = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        pago = new Pago(EstadoPago.NO_PAGADO, pag, cob, 10.25);
        listaPagosMock.add(pago);

        pag = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        cob = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        pago = new Pago(EstadoPago.NO_PAGADO, pag, cob, 77.13);
        listaPagosMock.add(pago);

        pag = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        cob = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        pago = new Pago(EstadoPago.NO_PAGADO, pag, cob, 18.56);
        listaPagosMock.add(pago);

        pag = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        cob = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        pago = new Pago(EstadoPago.PAGADO, pag, cob, 25.00);
        listaPagosMock.add(pago);

        pag = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        cob = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        pago = new Pago(EstadoPago.PAGADO, pag, cob, 80.75);
        listaPagosMock.add(pago);

        pag = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        cob = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        pago = new Pago(EstadoPago.PAGADO, pag, cob, 155.00);
        listaPagosMock.add(pago);

        pag = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        cob = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        pago = new Pago(EstadoPago.PAGADO, pag, cob, 100.00);
        listaPagosMock.add(pago);

        pag = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        cob = listaParticipantesMock.get(new Random().nextInt(listaParticipantesMock.size()));
        pago = new Pago(EstadoPago.PAGADO, pag, cob, 250.25);
        listaPagosMock.add(pago);

        return listaPagosMock;
    }
}
