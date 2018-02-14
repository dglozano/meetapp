package com.example.dglozano.meetapp.adapters;

/**
 * Created by dglozano on 01/02/18.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.modelo.Pago;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by dglozano on 01/02/18.
 */

public class PagoItemAdapter extends RecyclerView.Adapter<PagoItemAdapter.PagoViewHolder> {

    private List<Pago> pagosList;

    public class PagoViewHolder extends RecyclerView.ViewHolder {
        private TextView montoTextView;
        private TextView pagadorTextView;
        private ImageView arrowImageView;
        private TextView cobradorTextView;


        public PagoViewHolder(View view) {
            super(view);
            montoTextView = (TextView) view.findViewById(R.id.tv_payment_row_monto);
            pagadorTextView = (TextView) view.findViewById(R.id.tv_payment_row_pagador);
            arrowImageView = (ImageView) view.findViewById(R.id.img_payment_row_arrow);
            cobradorTextView = (TextView) view.findViewById(R.id.tv_payment_row_cobrador);
        }
    }

    public PagoItemAdapter(List<Pago> pagosList) {
        this.pagosList = pagosList;
    }

    @Override
    public PagoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_list_row, parent, false);

        return new PagoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PagoViewHolder holder, int position) {
        Pago pago = pagosList.get(position);
        DecimalFormat df = new DecimalFormat("$ ###,##0.00");
        holder.montoTextView.setText(df.format(pago.getMonto()));
        holder.pagadorTextView.setText(pago.getPagador().toString());
        holder.arrowImageView.setImageResource(R.drawable.ic_subdirectory_arrow_right_black_24dp);
        holder.cobradorTextView.setText(pago.getCobrador().toString());
    }

    @Override
    public int getItemCount() {
        return pagosList.size();
    }

}