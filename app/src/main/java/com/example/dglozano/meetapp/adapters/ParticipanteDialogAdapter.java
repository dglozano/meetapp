package com.example.dglozano.meetapp.adapters;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.dao.SQLiteDaoTarea;
import com.example.dglozano.meetapp.fragments.DialogSelectParticipante;
import com.example.dglozano.meetapp.modelo.EstadoTarea;
import com.example.dglozano.meetapp.modelo.Participante;
import com.example.dglozano.meetapp.modelo.Tarea;

import java.util.List;

/**
 * Created by dglozano on 01/02/18.
 */

public class ParticipanteDialogAdapter extends RecyclerView.Adapter<ParticipanteDialogAdapter.ParticipanteViewHolder> {

    private List<Participante> participantesList;
    private int idTarea;
    private SQLiteDaoTarea daoTarea;
    private DialogSelectParticipante dialogFragment;

    public class ParticipanteViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreTextView;

        public ParticipanteViewHolder(View view) {
            super(view);
            nombreTextView = view.findViewById(R.id.tv_participante_dialog_row);
        }
    }

    public ParticipanteDialogAdapter(DialogSelectParticipante df, List<Participante> participantesList, int idTarea, Context context) {
        this.participantesList = participantesList;
        this.idTarea = idTarea;
        this.daoTarea = new SQLiteDaoTarea(context);
        this.dialogFragment = df;
    }

    @Override
    public ParticipanteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.participante_dialog_row, parent, false);
        itemView.setOnClickListener(new MyOnClickListener());
        return new ParticipanteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ParticipanteViewHolder holder, int position) {
        final Participante participante = participantesList.get(position);
        holder.nombreTextView.setText(participante.toString());
    }

    @Override
    public int getItemCount() {
        return participantesList.size();
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            RecyclerView rv = (RecyclerView) view.getParent();
            int itemPosition = rv.getChildLayoutPosition(view);
            Participante participante = participantesList.get(itemPosition);
            Tarea tarea = daoTarea.getById(idTarea);
            tarea.setPersonaAsignada(participante);
            if(participante.esSinAsignar()){
                tarea.setEstadoTarea(EstadoTarea.SIN_ASIGNAR);
            } else if (tarea.getEstadoTarea() == EstadoTarea.SIN_ASIGNAR){
                tarea.setEstadoTarea(EstadoTarea.EN_PROGRESO);
            }
            daoTarea.update(tarea);
            dialogFragment.dismiss();
        }
    }
}