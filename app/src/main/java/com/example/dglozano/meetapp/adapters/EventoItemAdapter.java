package com.example.dglozano.meetapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.modelo.Evento;

/**
 * Created by augusto on 01/02/2018.
 */

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {

    private List<Evento> eventosList;

    public class EventoViewHolder extends RecyclerView.ViewHolder {
        TextView tituloEventoTV = null;
        TextView fechaEventoTV = null;
        TextView cantOrganizadoresTV = null;
        TextView tareasRestantesTV = null;



        public EventoViewHolder(View view) {
            super(view);
            tituloEventoTV = (TextView) view.findViewById(R.id.titulo_evento);
            fechaEventoTV = (TextView) view.findViewById(R.id.fecha_evento);
            cantOrganizadoresTV = (TextView) view.findViewById(R.id.cant_organizadores_evento);
            tareasRestantesTV = (TextView) view.findViewById(R.id.tareas_restantes_evento);

        }
    }

    public EventoAdapter(List<Evento> eventosList) {
        this.eventosList = eventosList;
    }

    @Override
    public EventoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fila_evento, parent, false);

        return new EventoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventoViewHolder holder, int position) {
        Evento evento = eventosList.get(position);
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        holder.tituloEventoTV.setText(evento.getNombre());
        holder.fechaEventoTV.setText(evento.getFecha());
        holder.cantOrganizadoresTV.setText('A');
        holder.tareasRestantesTV.setText('B');
    }

    @Override
    public int getItemCount() {
        return eventosList.size();
    }



}

