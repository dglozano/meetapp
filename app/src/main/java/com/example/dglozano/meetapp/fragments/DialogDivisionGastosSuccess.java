package com.example.dglozano.meetapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dglozano.meetapp.R;

import java.text.DecimalFormat;

/**
 * Created by dglozano on 11/02/18.
 */

public class DialogDivisionGastosSuccess extends DialogFragment {

    public static DialogDivisionGastosSuccess newInstance(double gastoTotal,
                                                          double gastoCadaParticipante,
                                                          boolean divisionYaHecha) {
        DialogDivisionGastosSuccess f = new DialogDivisionGastosSuccess();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putDouble("gastoTotal", gastoTotal);
        args.putDouble("gastoCadaParticipante", gastoCadaParticipante);
        args.putBoolean("divisionYaHecha", divisionYaHecha);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        double gastoTotal = getArguments().getDouble("gastoTotal");
        double gastoCadaParticipante = getArguments().getDouble("gastoCadaParticipante");
        boolean divisionYaHecha = getArguments().getBoolean("divisionYaHecha");
        DecimalFormat df = new DecimalFormat("$ ###,##0.00");
        String msgBaseGastoTotal = getString(R.string.alert_success_total_msg) + " ";
        String msgBaseGastoCadaParticipante = getString(R.string.alert_success_individual_msg) + " ";
        String msgGastoTotal = msgBaseGastoTotal + " " + df.format(gastoTotal);
        String msgGastoCadaParticipante = msgBaseGastoCadaParticipante + df.format(gastoCadaParticipante);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.fragment_dialog_success_division, null);
        TextView tituloTV = (TextView) view.findViewById(R.id.tv_alert_success_gastos_titulo);
        TextView gastosTotalTV = (TextView) view.findViewById(R.id.tv_alert_success_gastos_total_msg);
        TextView gastosPorPartTV = (TextView) view.findViewById(R.id.tv_alert_success_gastos_individual_msg);
        Button btnOk = (Button) view.findViewById(R.id.tv_alert_success_button_ok);
        gastosTotalTV.setText(msgGastoTotal);
        gastosPorPartTV.setText(msgGastoCadaParticipante);
        if(divisionYaHecha){
            tituloTV.setText(R.string.success_division_info_msg);
        }
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        builder.setView(view);
        return builder.create();
    }
}