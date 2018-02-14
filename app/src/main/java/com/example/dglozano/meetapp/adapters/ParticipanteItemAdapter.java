package com.example.dglozano.meetapp.adapters;

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
import com.example.dglozano.meetapp.modelo.Participante;

import java.util.List;

/**
 * Created by dglozano on 01/02/18.
 */

public class ParticipanteItemAdapter extends RecyclerView.Adapter<ParticipanteItemAdapter.ParticipanteViewHolder> {

    private List<Participante> participantesList;

    public class ParticipanteViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView nombreTextView;
        public ImageButton btnLlamarParticipante;
        public ImageButton btnSmsParticipante;

        public ParticipanteViewHolder(View view) {
            super(view);
            nombreTextView = (TextView) view.findViewById(R.id.tv_participante_row_nombre);
            btnLlamarParticipante = (ImageButton) view.findViewById(R.id.btn_llamar_participante);
            btnSmsParticipante = (ImageButton) view.findViewById(R.id.btn_sms_participante);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), 5, 0, R.string.texto_borrar);
        }
    }

    public ParticipanteItemAdapter(List<Participante> participantesList) {
        this.participantesList = participantesList;
    }

    @Override
    public ParticipanteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.participante_list_row, parent, false);

        return new ParticipanteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ParticipanteViewHolder holder, int position) {
        final Participante participante = participantesList.get(position);
        holder.nombreTextView.setText(participante.toString());

        if(participante.esCreadorEvento()) {
            holder.btnLlamarParticipante.setVisibility(View.INVISIBLE);
            holder.btnSmsParticipante.setVisibility(View.INVISIBLE);
        } else {
            holder.btnLlamarParticipante.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String numero = participante.getNumeroTel();
                    String dial = "tel:" + numero;
                    Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse(dial));
                    view.getContext().startActivity(intentCall);
                }
            });
            holder.btnSmsParticipante.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String numero = participante.getNumeroTel();
                    String dial = "sms:" + numero;
                    Intent intentSMS = new Intent(Intent.ACTION_VIEW, Uri.parse(dial));
                    intentSMS.putExtra("sms_body", R.string.sms_msg);
                    view.getContext().startActivity(intentSMS);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return participantesList.size();
    }
}
