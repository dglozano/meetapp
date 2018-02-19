package com.example.dglozano.meetapp.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.dglozano.meetapp.R;


/**
 * Created by dglozano on 11/02/18.
 */

public class DialogDeletePagos extends android.support.v4.app.DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, int idAccion, int idElemento);
        void onDialogNegativeClick(DialogFragment dialog, int idAccion, int idElemento);
    }

    // Use this instance of the interface to deliver action events
    private NoticeDialogListener mListener;

    public static DialogDeletePagos newInstance(int idAccion, int idElemento) {
        DialogDeletePagos f = new DialogDeletePagos();

        Bundle args = new Bundle();
        args.putInt("idAccion", idAccion);
        args.putInt("idElemento", idElemento);
        f.setArguments(args);

        return f;
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final int idAccion = getArguments().getInt("idAccion");
        final int idElemento = getArguments().getInt("idElemento");
        builder.setTitle(R.string.dialog_borrar_pagos_title)
                .setMessage(R.string.dialog_borrar_pagos_message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(DialogDeletePagos.this, idAccion, idElemento);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick(DialogDeletePagos.this, idAccion, idElemento);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}