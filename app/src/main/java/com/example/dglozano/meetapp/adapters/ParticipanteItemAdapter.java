package com.example.dglozano.meetapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.modelo.Participante;

import java.util.List;

/**
 * Created by dglozano on 01/02/18.
 */

public class ParticipanteItemAdapter extends RecyclerView.Adapter<ParticipanteItemAdapter.ParticipanteViewHolder> {

    private List<Participante> participantesList;

    public class ParticipanteViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreTextView;
        private ImageView fotoImageView;

        public ParticipanteViewHolder(View view) {
            super(view);
            nombreTextView = (TextView) view.findViewById(R.id.tv_participante_row_nombre);
            fotoImageView = (ImageView) view.findViewById(R.id.img_participante_row_picture);
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
        Participante participante = participantesList.get(position);
        holder.nombreTextView.setText(participante.toString());
        switch(participante.getPictureId()){
            case 0:
                holder.fotoImageView.setImageResource(R.drawable.ic_adb_black_36dp);
                break;
            case 1:
                holder.fotoImageView.setImageResource(R.drawable.ic_face_black_36dp);
                break;
            case 2:
                holder.fotoImageView.setImageResource(R.drawable.ic_child_care_black_36dp);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return participantesList.size();
    }
}
