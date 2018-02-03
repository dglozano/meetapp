package com.example.dglozano.meetapp.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.adapters.EventoItemAdapter;
import com.example.dglozano.meetapp.modelo.Evento;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by augusto on 03/02/2018.
 */

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventosPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventosPageFragment extends android.support.v4.app.Fragment {

    private List<Evento> eventosListDisplayed = new ArrayList<>();
    private EventoItemAdapter mEventoItemAdapter;
    private RecyclerView mEventosRecyclerView;

    private List<Evento> eventosDelUsuario = Evento.getEventosMock();


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
        mEventoItemAdapter =  new EventoItemAdapter(eventosListDisplayed);
        //TODO: VER QUE MOSTRAR CUANDO NO HAY PAGOS TODAVIA
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                getActivity().getApplicationContext());
        mEventosRecyclerView.setLayoutManager(mLayoutManager);
        mEventosRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mEventosRecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL));
        mEventosRecyclerView.setAdapter(mEventoItemAdapter);
        eventosListDisplayed.addAll(eventosDelUsuario);
        mEventoItemAdapter.notifyDataSetChanged();
    }

    private void search(String query) {
        List<Evento> result = new ArrayList<>();
        for(Evento e: eventosDelUsuario){
            if(e.matches(query)){
                result.add(e);
            }
        }
        eventosListDisplayed.clear();
        eventosListDisplayed.addAll(result);
        System.out.println(eventosListDisplayed);
        mEventoItemAdapter.notifyDataSetChanged();
    }

    private void restoreOriginalEventosList(){
        eventosListDisplayed.clear();
        eventosListDisplayed.addAll(eventosDelUsuario);
        mEventoItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        final MenuItem searchItem = menu.findItem(R.id.toolbar_search_main);
        final SearchView searchView =
                (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new EventosPageFragment.MyOnQueryTextListener());
        searchView.setOnCloseListener(new EventosPageFragment.MyOnCloseListener());
    }

    private class MyOnQueryTextListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            search(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String query) {
            System.out.println("Entro)");
            search(query);
            if(query.trim().isEmpty()){
                restoreOriginalEventosList();
            }
            return false;
        }
    }

    private class MyOnCloseListener implements SearchView.OnCloseListener {
        @Override
        public boolean onClose() {
            restoreOriginalEventosList();
            return false;

        }
    }
}
