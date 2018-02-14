package com.example.dglozano.meetapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dglozano.meetapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by dglozano on 11/02/18.
 */

public class DialogVerFotoTarea extends android.support.v4.app.DialogFragment {

    public static DialogVerFotoTarea newInstance(Integer idEvento, Integer idTarea, String titulo) {
        DialogVerFotoTarea f = new DialogVerFotoTarea();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("idEvento", idEvento);
        args.putInt("idTarea", idTarea);
        args.putString("titulo", titulo);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        Integer idEvento = getArguments().getInt("idEvento");
        Integer idTarea = getArguments().getInt("idTarea");
        String titulo = getArguments().getString("titulo");
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.fragment_dialog_ver_foto, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.header_alert_ver_foto);
        ImageView imagen = (ImageView) view.findViewById(R.id.alert_dialog_foto_img_picture);
        LinearLayout layoutEmptyMsg = (LinearLayout) view.findViewById(R.id.empty_msg_layout_foto_dialog);
        Button btnOk = (Button) view.findViewById(R.id.alert_dialog_foto_button_ok);

        tvTitle.setText(titulo);
        try {
            File directory = getActivity().getApplicationContext().getDir("imagenes", Context.MODE_PRIVATE);
            File f = new File(directory, "evento_" + idEvento + "tarea_" + idTarea + ".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            layoutEmptyMsg.setVisibility(View.GONE);
            imagen.setVisibility(View.VISIBLE);
            imagen.setImageBitmap(b);
        } catch (FileNotFoundException e){
            imagen.setVisibility(View.GONE);
            layoutEmptyMsg.setVisibility(View.VISIBLE);
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