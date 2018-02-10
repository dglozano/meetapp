package com.example.dglozano.meetapp.util;

import android.util.Pair;

import com.example.dglozano.meetapp.dao.MockDaoParticipante;
import com.example.dglozano.meetapp.dao.MockDaoTarea;
import com.example.dglozano.meetapp.modelo.EstadoPago;
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

    private static List<Pago> listaPagos;

    public static List<Pago> getListaPagos() {
        return listaPagos;
    }

    public static void calcularPagos() {
        // TODO obtener tareas y participantes del evento

        // FIXME quitar este mock
        List<Tarea> tareasDelEvento = MockDaoTarea.getInstance().getAll();
        List<Participante> participantesDelEvento = MockDaoParticipante.getInstance().getAll();

        HashMap<Participante, Double> gastosPorParticipante = new HashMap<>();
        for(Tarea tarea : tareasDelEvento) {
            if(!tarea.getPersonaAsignada().equals(Participante.getParticipanteSinAsignar())) {
                Double gasto = gastosPorParticipante.get(tarea.getPersonaAsignada());
                if(gasto == null) {
                    gasto = tarea.getGasto();
                } else {
                    gasto += tarea.getGasto();
                }
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
        double promedio = 0;
        for(Double g : gastosPorParticipante.values()) {
            promedio += g;
        }
        promedio = promedio / gastosPorParticipante.values().size();

        // ajusta el promedio a dos decimales
        promedio = promedio * 100;
        long aux = (long) promedio;
        promedio = ((double) aux) / 100;

        // deudas por participante (ya sean positivas, porque debe dar plata, o negativas porque debe recibir plata)
        List<Pair<Participante, Double>> deudasPorParticipante = new ArrayList<>();
        for(Participante participante : gastosPorParticipante.keySet()) {
            deudasPorParticipante.add(new Pair(participante, promedio - gastosPorParticipante.get(participante)));
        }

        listaPagos = pagos(deudasPorParticipante, 0.009);
    }

    private static List<Pago> pagos(List<Pair<Participante, Double>> deudasPorParticipante, double tolerancia) {
        List<Pago> pagos = new ArrayList<>();
        int resueltos = 0;
        while(resueltos != deudasPorParticipante.size()) {
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
            pagos.add(new Pago(EstadoPago.NO_PAGADO, deudor.first, acreedor.first, monto));

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
