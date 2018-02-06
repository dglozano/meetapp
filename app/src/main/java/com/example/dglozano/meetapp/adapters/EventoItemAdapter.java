package com.example.dglozano.meetapp.adapters;

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

public class EventoItemAdapter extends RecyclerView.Adapter<EventoItemAdapter.EventoViewHolder> {

    private List<Evento> eventosList;

    public class EventoViewHolder extends RecyclerView.ViewHolder {
        TextView tituloEventoTV, fechaEventoTV, cantOrganizadoresTV, tareasRestantesTV;

        public EventoViewHolder(View view) {
            super(view);
            tituloEventoTV = (TextView) view.findViewById(R.id.tv_titulo_evento);
            fechaEventoTV = (TextView) view.findViewById(R.id.tv_fecha_evento);
            cantOrganizadoresTV =  (TextView) view.findViewById(R.id.tv_organizadores_evento);
            tareasRestantesTV = (TextView) view.findViewById(R.id.tv_tareas_completas_evento);
        }
    }

    public EventoItemAdapter(List<Evento> eventosList) {
        this.eventosList = eventosList;
    }

    @Override
    public EventoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.evento_list_row, parent, false);

        return new EventoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventoViewHolder holder, int position) {
        Evento evento = eventosList.get(position);
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        holder.tituloEventoTV.setText(evento.getNombre());
        holder.fechaEventoTV.setText(evento.getFecha());
        holder.cantOrganizadoresTV.setText("Organizadores");
        holder.tareasRestantesTV.setText("0 de 5 Tareas");
    }

    @Override
    public int getItemCount() {
        return eventosList.size();
    }



}

