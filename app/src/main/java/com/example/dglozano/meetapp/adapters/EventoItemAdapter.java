package com.example.dglozano.meetapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.actividades.EventoActivity;
import com.example.dglozano.meetapp.modelo.EstadoTarea;
import com.example.dglozano.meetapp.modelo.Evento;
import com.example.dglozano.meetapp.modelo.Tarea;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by augusto on 01/02/2018.
 */

public class EventoItemAdapter extends RecyclerView.Adapter<EventoItemAdapter.EventoViewHolder> {

    private List<Evento> eventosList;
    private Context mContext;

    public static final String EXTRA_EVENTO_ID = "com.example.dglozano.meetapp.adapters.EXTRA_EVENTO_ID";

    public class EventoViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView tituloEventoTV, fechaEventoTV, cantOrganizadoresTV, tareasRestantesTV, lugarEventoTV;

        public EventoViewHolder(View view) {
            super(view);
            tituloEventoTV = (TextView) view.findViewById(R.id.tv_titulo_evento);
            lugarEventoTV = (TextView) view.findViewById(R.id.tv_lugar_evento);
            fechaEventoTV = (TextView) view.findViewById(R.id.tv_fecha_evento);
            cantOrganizadoresTV = (TextView) view.findViewById(R.id.tv_organizadores_evento);
            tareasRestantesTV = (TextView) view.findViewById(R.id.tv_tareas_completas_evento);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.add(this.getAdapterPosition(), 1, 0, R.string.texto_editar);
            menu.add(this.getAdapterPosition(), 2, 1, R.string.texto_borrar);
        }
    }

    public EventoItemAdapter(List<Evento> eventosList, Context context) {
        this.eventosList = eventosList;
        mContext = context;
    }

    @Override
    public EventoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.evento_list_row, parent, false);
        itemView.setOnClickListener(new MyOnClickListener());
        return new EventoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventoViewHolder holder, int position) {
        Evento evento = eventosList.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        holder.tituloEventoTV.setText(evento.getNombre());
        Geocoder geoCoder = new Geocoder(mContext);
        List<Address> matches = null;
        String lugarAMostrar = "Lugar no especificado";
        try {
            matches = geoCoder.getFromLocation(evento.getLugar().latitude,
                    evento.getLugar().longitude,
                    1);
            Address bestMatch = (matches == null || matches.isEmpty()) ? null : matches.get(0);
            if(bestMatch != null) {
                String direccion = bestMatch.getAddressLine(0);
                lugarAMostrar = String.format("%s, %s, %s",
                        direccion.substring(0, direccion.indexOf(',')),
                        bestMatch.getLocality(),
                        bestMatch.getCountryCode());
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        holder.lugarEventoTV.setText(lugarAMostrar);
        holder.fechaEventoTV.setText(sdf.format(evento.getFecha()));
        holder.cantOrganizadoresTV.setText(evento.getParticipantes().size() + " Participantes");
        int totalTareas = evento.getTareas().size();
        int tareasHechas = 0;
        for(Tarea t : evento.getTareas()) {
            if(t.getEstadoTarea() == EstadoTarea.FINALIZADA) {
                tareasHechas++;
            }
        }
        holder.tareasRestantesTV.setText(tareasHechas + " de " + totalTareas + " Tareas");
    }

    @Override
    public int getItemCount() {
        return eventosList.size();
    }

    public Evento getItemAtPosition(int position) {

        Evento evento = eventosList.get(position);

        return evento;
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            RecyclerView rv = (RecyclerView) view.getParent();
            int itemPosition = rv.getChildLayoutPosition(view);
            Evento eventoClickeado = eventosList.get(itemPosition);
            Intent intent = new Intent(mContext, EventoActivity.class);
            intent.putExtra(EXTRA_EVENTO_ID, eventoClickeado.getId());
            mContext.startActivity(intent);
        }
    }
}

