package com.example.dglozano.meetapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.actividades.TareaForm;
import com.example.dglozano.meetapp.fragments.TareasPageFragment;
import com.example.dglozano.meetapp.modelo.Tarea;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by dglozano on 01/02/18.
 */

public class TareaItemAdapter extends RecyclerView.Adapter<TareaItemAdapter.TareaViewHolder> {

    private List<Tarea> tareasList;
    private TareasPageFragment tareasPageFragment;

    public class TareaViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView titleTextView, personasAsignadaTextView, gastoTextview;
        private ImageView estadoImageView;

        public TareaViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.tv_tarea_row_title);
            personasAsignadaTextView = view.findViewById(R.id.tv_tarea_row_persona_asignada);
            estadoImageView = view.findViewById(R.id.img_tarea_row_estado_icon);
            gastoTextview = view.findViewById(R.id.tv_tarea_row_gasto);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.add(this.getAdapterPosition(), 3, 2, R.string.agregar_gasto);
            menu.add(this.getAdapterPosition(), 5, 3, R.string.ver_foto);
            menu.add(this.getAdapterPosition(), 4, 4, R.string.dar_por_finalizada);
            menu.add(this.getAdapterPosition(), 1, 0, R.string.texto_editar);
            menu.add(this.getAdapterPosition(), 2, 5, R.string.texto_borrar);
            menu.add(this.getAdapterPosition(), 6, 1, R.string.asignar_encargado);
        }
    }

    public TareaItemAdapter(List<Tarea> tareasList, TareasPageFragment tareasPageFragment) {
        this.tareasList = tareasList;
        this.tareasPageFragment = tareasPageFragment;
    }

    @Override
    public TareaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tarea_list_row, parent, false);
        itemView.setOnClickListener(new MyOnClickListener());
        return new TareaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TareaViewHolder holder, int position) {
        Tarea tarea = tareasList.get(position);
        holder.titleTextView.setText(tarea.getTitulo());
        DecimalFormat df = new DecimalFormat("$ ###,##0");
        holder.gastoTextview.setText(df.format(tarea.getGasto()));
        holder.personasAsignadaTextView.setText(tarea.getPersonaAsignada().toString());
        switch (tarea.getEstadoTarea()) {
            case SIN_ASIGNAR:
                holder.estadoImageView.setImageResource(R.drawable.ic_person_add_black_24dp);
                break;
            case EN_PROGRESO:
                holder.estadoImageView.setImageResource(R.drawable.ic_access_time_black_24dp);
                break;
            case FINALIZADA:
                holder.estadoImageView.setImageResource(R.drawable.ic_check_circle_black_24dp);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return tareasList.size();
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            RecyclerView rv = (RecyclerView) view.getParent();
            int itemPosition = rv.getChildLayoutPosition(view);
            Tarea tareaClickeada = tareasList.get(itemPosition);
            tareasPageFragment.editarTarea(tareaClickeada);
        }
    }
}
