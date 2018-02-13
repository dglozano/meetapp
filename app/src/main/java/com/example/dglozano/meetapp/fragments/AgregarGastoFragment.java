package com.example.dglozano.meetapp.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.dao.SQLiteDaoTarea;
import com.example.dglozano.meetapp.modelo.Tarea;

public class AgregarGastoFragment extends DialogFragment {

    private Double gasto;
    private SQLiteDaoTarea dao;
    private Tarea tarea;
    private TextView tv_nombreTarea;
    private TextView tv_gastoActual;
    private EditText et_gasto;
    private Button sumar;
    private Button restar;
    private Button guardar;
    private Button cancelar;

    public static AgregarGastoFragment newInstance(int idTarea) {
        AgregarGastoFragment f = new AgregarGastoFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("id", idTarea);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        int id = getArguments().getInt("id");
        dao = new SQLiteDaoTarea(getContext());
        tarea = dao.getById(id);
        gasto = tarea.getGasto();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.agregar_gasto_fragment, null);
        builder.setView(view);

        findViews(view);

        tv_nombreTarea.setText(tarea.getTitulo());
        tv_gastoActual.setText("$" + gasto.toString());

        setListeners();

        return builder.create();
    }

    private void findViews(View view) {
        tv_nombreTarea = view.findViewById(R.id.tv_nombre_tarea);
        tv_gastoActual = view.findViewById(R.id.textView_label_gastoactual_valor);
        et_gasto = view.findViewById(R.id.editText_gasto);
        sumar = view.findViewById(R.id.button_sumar);
        restar = view.findViewById(R.id.button_restar);
        guardar = view.findViewById(R.id.btn_guardar);
        cancelar = view.findViewById(R.id.btn_cancelar);
    }

    private void setListeners() {
        sumar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String texto = et_gasto.getText().toString();
                if(!texto.equals("")) {
                    gasto += Double.valueOf(texto);
                    tv_gastoActual.setText("$" + gasto.toString());
                }
            }
        });
        restar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String texto = et_gasto.getText().toString();
                if(!texto.equals("")) {
                    gasto -= Double.valueOf(texto);
                    if(gasto < 0) gasto = 0.0;
                    tv_gastoActual.setText("$" + gasto.toString());
                }
            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tarea.setGasto(gasto);
                dao.update(tarea);
                AgregarGastoFragment.this.getDialog().dismiss();
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AgregarGastoFragment.this.getDialog().cancel();
            }
        });
    }
}