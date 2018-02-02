package com.example.dglozano.meetapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.adapters.ParticipanteItemAdapter;
import com.example.dglozano.meetapp.adapters.TareaItemAdapter;
import com.example.dglozano.meetapp.modelo.Participante;
import com.example.dglozano.meetapp.modelo.Tarea;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParticipantesPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParticipantesPageFragment extends android.support.v4.app.Fragment {

    private List<Participante> participantesList = Participante.getParticipantesMock();
    private ParticipanteItemAdapter mParticipanteAdapter;
    private RecyclerView mParticipantesRecyclerView;

    public ParticipantesPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment ParticipantesPageFragment.
     */
    public static ParticipantesPageFragment newInstance() {
        ParticipantesPageFragment fragment = new ParticipantesPageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_participantes_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mParticipantesRecyclerView = view.findViewById(R.id.recvw_participantes_list);
        mParticipanteAdapter =  new ParticipanteItemAdapter(participantesList);
        //TODO: VER QUE MOSTRAR CUANDO NO HAY PARTICIPANTES TODAVIA
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                getActivity().getApplicationContext());
        mParticipantesRecyclerView.setLayoutManager(mLayoutManager);
        mParticipantesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mParticipantesRecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL));
        mParticipantesRecyclerView.setAdapter(mParticipanteAdapter);
        mParticipanteAdapter.notifyDataSetChanged();
    }
}
