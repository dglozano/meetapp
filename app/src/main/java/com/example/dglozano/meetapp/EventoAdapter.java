package com.example.dglozano.meetapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created by augusto on 01/02/2018.
 */

public class EventoAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<Evento> listaEventos;

    public EventoAdapter(Context applicationContext, List<Evento> listaEventos) {
        super();
        inflater = LayoutInflater.from(applicationContext);
        this.listaEventos = listaEventos;
    }

    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public int getCount() {
        return listaEventos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaEventos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaEventos.get(position).getId();;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        if(row == null) row = inflater.inflate(R.layout.fila_evento, parent, false);
        ViewItemHolder holder = (ViewItemHolder) row.getTag();
        if(holder ==null){
            holder = new ViewItemHolder(row);
            row.setTag(holder);
        }

        Evento evento = (Evento) getItem(position);

        holder.tituloEventoTV.setText(evento.getTitulo());
        holder.fechaEventoTV.setText(evento.getFecha());
        holder.cantOrganizadoresTV.setText(evento.getOrganizadores());
        holder.tareasRestantesTV.setText(evento.getTareas());

        return null;
    }

    private class ViewItemHolder {
        TextView tituloEventoTV = null;
        TextView fechaEventoTV = null;
        TextView cantOrganizadoresTV = null;
        TextView tareasRestantesTV = null;


        ViewItemHolder(View base) {
            this.tituloEventoTV = base.findViewById(R.id.titulo_evento);
            this.fechaEventoTV = base.findViewById(R.id.fecha_evento);
            this.cantOrganizadoresTV = base.findViewById(R.id.cant_organizadores_evento);
            this.tareasRestantesTV = base.findViewById(R.id.tareas_restantes_evento);
        }
    }
}

