package com.retigalo.dglozano.meetapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.retigalo.dglozano.meetapp.R;
import com.retigalo.dglozano.meetapp.actividades.EventoActivity;
import com.retigalo.dglozano.meetapp.modelo.EstadoTarea;
import com.retigalo.dglozano.meetapp.modelo.Evento;
import com.retigalo.dglozano.meetapp.modelo.Tarea;
import com.retigalo.dglozano.meetapp.util.AddressFormater;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.tituloEventoTV.setText(evento.getNombre());
        AddressFormater af = new AddressFormater(new Geocoder(mContext, Locale.getDefault()));
        String lugarAMostrar = af.format(evento.getLugar());
        holder.lugarEventoTV.setText(lugarAMostrar);
        holder.fechaEventoTV.setText(sdf.format(evento.getFecha()));
        holder.cantOrganizadoresTV.setText(String.format(mContext.getString(R.string.participantes),
                evento.getParticipantes().size() - 1));
        int totalTareas = evento.getTareas().size();
        int tareasHechas = 0;
        for (Tarea t : evento.getTareas()) {
            if (t.getEstadoTarea() == EstadoTarea.FINALIZADA) {
                tareasHechas++;
            }
        }
        holder.tareasRestantesTV.setText(String.format(mContext.getString(R.string.tareas_avance),tareasHechas,totalTareas));
    }

    @Override
    public int getItemCount() {
        return eventosList.size();
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

