package com.example.dglozano.meetapp.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.actividades.EventoForm;
import com.example.dglozano.meetapp.adapters.EventoItemAdapter;
import com.example.dglozano.meetapp.dao.DaoEvento;
import com.example.dglozano.meetapp.dao.SQLiteDaoEvento;
import com.example.dglozano.meetapp.modelo.Evento;
import com.example.dglozano.meetapp.util.Recordatorios;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventosPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventosPageFragment extends android.support.v4.app.Fragment implements View.OnCreateContextMenuListener {

    private final int CREAR_EVENTO = 1;
    private final int EDITAR_EVENTO = 2;

    private List<Evento> eventosListDisplayed = new ArrayList<>();
    private EventoItemAdapter mEventoItemAdapter;

    private LinearLayout mLayoutEmptyMsg;

    private DaoEvento daoEvento;
    private List<Evento> eventosDelUsuario;


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
        setHasOptionsMenu(true);

        daoEvento = new SQLiteDaoEvento(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_eventos_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        eventosDelUsuario = daoEvento.getAll();
        mLayoutEmptyMsg = view.findViewById(R.id.empty_msg_layout_eventos);
        mLayoutEmptyMsg.setVisibility(View.INVISIBLE);
        RecyclerView mEventosRecyclerView = view.findViewById(R.id.rcvw_eventos_list);
        mEventoItemAdapter = new EventoItemAdapter(eventosListDisplayed, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                getActivity().getApplicationContext());
        mEventosRecyclerView.setLayoutManager(mLayoutManager);
        mEventosRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mEventosRecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL));
        mEventosRecyclerView.setAdapter(mEventoItemAdapter);
        eventosListDisplayed.clear();
        eventosListDisplayed.addAll(eventosDelUsuario);
        mEventoItemAdapter.notifyDataSetChanged();
        if(eventosDelUsuario.isEmpty()){
            mLayoutEmptyMsg.setVisibility(View.VISIBLE);
        }
        FloatingActionButton fab = view.findViewById(R.id.fab_btn_crear_evento);
        fab.setOnClickListener(new MyFabIconOnClickListener());
    }


    private void search(String query) {
        List<Evento> result = new ArrayList<>();
        for(Evento e : eventosDelUsuario) {
            if(e.matches(query)) {
                result.add(e);
            }
        }
        eventosListDisplayed.clear();
        eventosListDisplayed.addAll(result);
        mEventoItemAdapter.notifyDataSetChanged();
    }

    private void restoreOriginalEventosList() {
        eventosListDisplayed.clear();
        eventosListDisplayed.addAll(eventosDelUsuario);
        mEventoItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        final MenuItem searchItem = menu.findItem(R.id.toolbar_search_main);
        final SearchView searchView =
                (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new MyOnQueryTextListener());
        searchView.setOnCloseListener(new MyOnCloseListener());
    }

    private class MyOnQueryTextListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            search(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String query) {

            search(query);
            if(query.trim().isEmpty()) {
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Evento evento = eventosListDisplayed.get(item.getGroupId());

        switch(item.getItemId()) {
            case 1:
                editarEvento(evento);
                return true;
            case 2:
                daoEvento.delete(evento);
                eventosDelUsuario.clear();
                eventosDelUsuario.addAll(daoEvento.getAll());
                restoreOriginalEventosList();
                Recordatorios recordatorios = new Recordatorios();
                recordatorios.eliminarRecordatorio(getContext(),evento.getId());
                if(eventosDelUsuario.isEmpty()){
                    mLayoutEmptyMsg.setVisibility(View.VISIBLE);
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    private void editarEvento(Evento evento) {
        Intent i = new Intent(getActivity(), EventoForm.class);
        i.putExtra(EventoForm.KEY_EVENTO_ID, evento.getId());
        startActivityForResult(i, EDITAR_EVENTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case CREAR_EVENTO: {
                if(resultCode == RESULT_OK) {
                    Toast.makeText(this.getContext(), R.string.evento_creado, Toast.LENGTH_SHORT).show();
                    eventosDelUsuario = daoEvento.getAll();
                    restoreOriginalEventosList();
                    mLayoutEmptyMsg.setVisibility(View.INVISIBLE);
                }
                break;
            }
            case EDITAR_EVENTO: {
                if(resultCode == RESULT_OK) {
                    Toast.makeText(this.getContext(), R.string.evento_editado, Toast.LENGTH_SHORT).show();
                    eventosDelUsuario = daoEvento.getAll();
                    restoreOriginalEventosList();
                }
                break;
            }
        }
    }

    private class MyFabIconOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(getActivity(), EventoForm.class);
            startActivityForResult(i, CREAR_EVENTO);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // load data here
            if(eventosDelUsuario != null && daoEvento != null){
                eventosDelUsuario = daoEvento.getAll();
                restoreOriginalEventosList();
                if(eventosDelUsuario.isEmpty()){
                    mLayoutEmptyMsg.setVisibility(View.VISIBLE);
                }
            }
        }else{
            // fragment is no longer visible
        }
    }

    @Override
    public void onResume() {
        if(eventosDelUsuario != null && daoEvento != null){
            eventosDelUsuario = daoEvento.getAll();
            restoreOriginalEventosList();
            if(eventosDelUsuario.isEmpty()){
                mLayoutEmptyMsg.setVisibility(View.VISIBLE);
            }
        }
        super.onResume();
    }
}
