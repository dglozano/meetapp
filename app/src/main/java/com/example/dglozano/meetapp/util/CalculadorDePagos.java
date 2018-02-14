package com.example.dglozano.meetapp.util;

import android.content.Context;
import android.util.Pair;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.dao.SQLiteDaoParticipante;
import com.example.dglozano.meetapp.dao.SQLiteDaoTarea;
import com.example.dglozano.meetapp.modelo.EstadoTarea;
import com.example.dglozano.meetapp.modelo.Pago;
import com.example.dglozano.meetapp.modelo.Participante;
import com.example.dglozano.meetapp.modelo.Tarea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class CalculadorDePagos {

    private List<Pago> listaPagos;
    private int idEvento;
    private SQLiteDaoTarea daoTarea;
    private SQLiteDaoParticipante daoParticipante;
    private double gastoTotal;
    private double gastoPorParticipante;
    private int codigoError;

    public CalculadorDePagos(Context c, int idEvento) {
        this.idEvento = idEvento;
        this.daoParticipante = new SQLiteDaoParticipante(c);
        this.daoTarea = new SQLiteDaoTarea(c);
    }

    public List<Pago> getListaPagos() {
        return listaPagos;
    }

    public double getGastoTotal() {
        return gastoTotal;
    }

    public double getGastoPorParticipante() {
        return gastoPorParticipante;
    }

    public boolean puedeCalcular() {
        List<Tarea> tareasDelEvento = daoTarea.getAllDelEvento(idEvento);
        if(tareasDelEvento.isEmpty()){
            codigoError = R.string.no_hay_tareas_msg_error;
            return false;
        }
        for(Tarea tarea : tareasDelEvento) {
            if(!tarea.getEstadoTarea().equals(EstadoTarea.FINALIZADA)) {
                codigoError = R.string.tareas_sin_finalizar;
                return false;
            } else if(tarea.getPersonaAsignada().esSinAsignar()) {
                codigoError = R.string.tareas_sin_encargado;
                return false;
            }
        }
        return true;
    }

    public int getCodigoError() {
        return this.codigoError;
    }

    public void calcularPagos() {
        List<Tarea> tareasDelEvento = daoTarea.getAllDelEvento(idEvento);
        List<Participante> participantesDelEvento = daoParticipante.getAllDelEvento(idEvento);

        HashMap<Participante, Double> gastosPorParticipante = new HashMap<>();
        for(Tarea tarea : tareasDelEvento) {
            if(!tarea.getPersonaAsignada().esSinAsignar()) {
                Double gasto = gastosPorParticipante.get(tarea.getPersonaAsignada());
                gasto = gasto == null ? tarea.getGasto() : gasto + tarea.getGasto();
                gastosPorParticipante.put(tarea.getPersonaAsignada(), gasto);
            }
        }

        // Agrego con gasto 0 a los participantes del evento que no estaban en ninguna tarea
        List<Participante> participantesSinGastos = new ArrayList<>();
        for(Participante participante : participantesDelEvento) {
            if(!gastosPorParticipante.containsKey(participante)) {
                participantesSinGastos.add(participante);
            }
        }
        for(Participante participantesSinGasto : participantesSinGastos) {
            gastosPorParticipante.put(participantesSinGasto, 0.0);
        }

        // calcula el promedio, es decir, lo que debería gastar cada participante para quedar saldados
        double sumaTotal = 0.0;
        for(Double g : gastosPorParticipante.values()) {
            sumaTotal += g;
        }
        double promedio = sumaTotal / gastosPorParticipante.values().size();
        this.gastoTotal = sumaTotal;
        this.gastoPorParticipante = promedio;

        // ajusta el promedio a dos decimales
        promedio = promedio * 100;
        long aux = (long) promedio;
        promedio = ((double) aux) / 100;

        // deudas por participante (ya sean positivas, porque debe dar plata, o negativas porque debe recibir plata)
        List<Pair<Participante, Double>> deudasPorParticipante = new ArrayList<>();
        for(Participante participante : gastosPorParticipante.keySet()) {
            deudasPorParticipante.add(new Pair(participante, promedio - gastosPorParticipante.get(participante)));
        }

        for(Pair<Participante, Double> participanteDoublePair : deudasPorParticipante) {
            System.out.println(participanteDoublePair.first + ": " + participanteDoublePair.second);
        }
        listaPagos = pagos(deudasPorParticipante, 0.009);
    }

    private List<Pago> pagos(List<Pair<Participante, Double>> deudasPorParticipante, double tolerancia) {
        List<Pago> pagos = new ArrayList<>();
        int resueltos = 0;
        while(resueltos < deudasPorParticipante.size()) {
            // Desde deuda más alta a deuda más baja (negativa)
            Collections.sort(deudasPorParticipante, new Comparator<Pair<Participante, Double>>() {
                @Override
                public int compare(Pair<Participante, Double> p1, Pair<Participante, Double> p2) {
                    return p2.second.compareTo(p1.second);
                }
            });
            Pair<Participante, Double> deudor = deudasPorParticipante.get(0);
            Pair<Participante, Double> acreedor = deudasPorParticipante.get(deudasPorParticipante.size() - 1);
            double deudorDeberiaPagar = Math.abs(deudor.second);
            double acreedorDeberiaRecibir = Math.abs(acreedor.second);
            double monto;
            if(deudorDeberiaPagar > acreedorDeberiaRecibir) {
                monto = acreedorDeberiaRecibir;
            } else {
                monto = deudorDeberiaPagar;
            }
            deudasPorParticipante.remove(deudor);
            deudasPorParticipante.add(new Pair(deudor.first, deudor.second - monto));
            deudasPorParticipante.remove(acreedor);
            deudasPorParticipante.add(new Pair(acreedor.first, acreedor.second + monto));

            // crea pago
            pagos.add(new Pago(deudor.first, acreedor.first, monto));

            // aumenta contador si ya están saldados
            deudorDeberiaPagar = Math.abs(deudor.second - monto);
            acreedorDeberiaRecibir = Math.abs(acreedor.second + monto);
            if(deudorDeberiaPagar <= tolerancia)
                resueltos++;
            if(acreedorDeberiaRecibir <= tolerancia)
                resueltos++;
        }
        // limita las transacciones por la tolerancia
        Iterator<Pago> iterator = pagos.iterator();
        while(iterator.hasNext()) {
            Pago pago = iterator.next();
            if(pago.getMonto() <= tolerancia)
                iterator.remove();
        }
        return pagos;
    }
}