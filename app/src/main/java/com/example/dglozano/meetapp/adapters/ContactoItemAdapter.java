package com.example.dglozano.meetapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.modelo.Contacto;

import java.util.List;

/**
 * Created by dglozano on 14/02/18.
 */


public class ContactoItemAdapter extends RecyclerView.Adapter<ContactoItemAdapter.ContactoItemViewHolder> {

    private List<Contacto> contactosList;

    public class ContactoItemViewHolder extends RecyclerView.ViewHolder {
        TextView nombreContactoTv, numeroContactoTv;
        CheckBox contactoCheckBox;

        public ContactoItemViewHolder(View view) {
            super(view);
            nombreContactoTv = view.findViewById(R.id.contacto_row_nombre);
            numeroContactoTv = view.findViewById(R.id.contacto_row_numero);
            contactoCheckBox = view.findViewById(R.id.contacto_row_chbox);
        }
    }

    public ContactoItemAdapter(List<Contacto> contactosList, Context context) {
        this.contactosList = contactosList;
    }

    @Override
    public ContactoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacto_add_row, parent, false);
        return new ContactoItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactoItemViewHolder holder, int position) {
        final Contacto contacto = contactosList.get(position);
        holder.nombreContactoTv.setText(contacto.getNombre());
        holder.numeroContactoTv.setText(contacto.getNumero());
        //in some cases, it will prevent unwanted situations
        holder.contactoCheckBox.setOnCheckedChangeListener(null);
        holder.contactoCheckBox.setChecked(contacto.isChecked());
        holder.contactoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                contacto.setChecked(b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactosList.size();
    }

}