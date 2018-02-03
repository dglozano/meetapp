package com.example.dglozano.meetapp.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.adapters.EventoAdapter;
import com.example.dglozano.meetapp.adapters.PagoItemAdapter;
import com.example.dglozano.meetapp.modelo.Evento;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by augusto on 03/02/2018.
 */

/**
 * A simple {@link android.app.Fragment} subclass.
 * Use the {@link DivisionGastosPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventosPageFragment extends android.support.v4.app.Fragment {

    private List<Evento> eventosList = Evento.getEventosMock();
    private EventoAdapter mEventoAdapter;
    private RecyclerView mEventosRecyclerView;


    public EventosPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment EventosPageFragment.
     */
    public static EventosPageFragment newInstance() {
        EventosPageFragment fragment = new EventosPageFragment();
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
        return inflater.inflate(R.layout.fragment_eventos_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mEventosRecyclerView = view.findViewById(R.id.rcvw_eventos_list);
        mEventoAdapter =  new EventoAdapter(eventosList);
        //TODO: VER QUE MOSTRAR CUANDO NO HAY PAGOS TODAVIA
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                getActivity().getApplicationContext());
        mEventosRecyclerView.setLayoutManager(mLayoutManager);
        mEventosRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mEventosRecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL));
        mEventosRecyclerView.setAdapter(mEventoAdapter);
        mEventoAdapter.notifyDataSetChanged();
    }
}
