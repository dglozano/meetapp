package com.example.dglozano.meetapp.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.adapters.ParticipanteDialogAdapter;
import com.example.dglozano.meetapp.dao.SQLiteDaoParticipante;
import com.example.dglozano.meetapp.dao.SQLiteDaoTarea;
import com.example.dglozano.meetapp.modelo.Participante;
import com.example.dglozano.meetapp.modelo.Tarea;
import com.example.dglozano.meetapp.util.OnDismissListener;

import java.text.DecimalFormat;
import java.util.List;

public class DialogSelectParticipante extends DialogFragment {

    private SQLiteDaoParticipante daoParticipante;
    private SQLiteDaoTarea daoTarea;
    private Tarea tarea;
    private List<Participante> participanteList;
    private OnDismissListener mListener;
    private RecyclerView mRecyclerView;

    public static DialogSelectParticipante newInstance(int idEvento, int idTarea) {
        DialogSelectParticipante f = new DialogSelectParticipante();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("idEvento", idEvento);
        args.putInt("idTarea", idTarea);
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
            mListener = (OnDismissListener) getTargetFragment();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int idTarea = getArguments().getInt("idTarea");
        int idEvento = getArguments().getInt("idEvento");

        daoTarea = new SQLiteDaoTarea(getContext());
        daoParticipante = new SQLiteDaoParticipante(getContext());
        tarea = daoTarea.getById(idTarea);
        participanteList = daoParticipante.getAllDelEvento(idEvento);

        mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(new ParticipanteDialogAdapter(this, participanteList, idTarea, getContext()));

        return new AlertDialog.Builder(getActivity())
                .setView(mRecyclerView)
                .create();
    }

    @Override
    public void onDismiss(DialogInterface di){
        this.mListener.onDismiss();
    }
}