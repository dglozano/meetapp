package com.example.dglozano.meetapp.fragments;

import android.app.Activity;
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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.actividades.EventoForm;
import com.example.dglozano.meetapp.adapters.EventoItemAdapter;
import com.example.dglozano.meetapp.dao.Dao;
import com.example.dglozano.meetapp.dao.SQLiteDaoEvento;
import com.example.dglozano.meetapp.modelo.Evento;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by augusto on 03/02/2018.
 */

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventosPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventosPageFragment extends android.support.v4.app.Fragment implements View.OnCreateContextMenuListener{

    private final int CREAR_EVENTO = 1;
    private final int EDITAR_EVENTO = 2;

    private List<Evento> eventosListDisplayed = new ArrayList<>();
    private EventoItemAdapter mEventoItemAdapter;
    private RecyclerView mEventosRecyclerView;

    private Dao<Evento> dao;
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


        //TODO cambiar a sqlite cuando se implemente
        dao = new SQLiteDaoEvento(getActivity());
        eventosDelUsuario = dao.getAll();
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
        mEventoItemAdapter = new EventoItemAdapter(eventosListDisplayed, getActivity());
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
//        registerForContextMenu(mEventosRecyclerView);

        FloatingActionButton fab = view.findViewById(R.id.fab_btn_crear_evento);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), EventoForm.class);
                startActivityForResult(i, CREAR_EVENTO);
            }
        });
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

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        Activity act = this.getActivity();
//        MenuInflater inflater = act.getMenuInflater();
//        inflater.inflate(R.menu.cm_evento, menu);
//    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Evento evento = eventosListDisplayed.get(item.getGroupId());

        switch (item.getItemId()) {
            case 1:
                editarEvento(evento);
                return true;
            case 2:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    private void editarEvento(Evento evento) {
        Intent i = new Intent(getActivity(), EventoForm.class);
        i.putExtra(EventoForm.ID_KEY, evento.getId());
        startActivityForResult(i, EDITAR_EVENTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case CREAR_EVENTO: {
                if(resultCode == RESULT_OK) {
                    // TODO agregar toast
                    eventosDelUsuario = dao.getAll();
                    restoreOriginalEventosList();
                }
                break;
            }
            case EDITAR_EVENTO: {
                if(resultCode == RESULT_OK) {
                    // TODO agregar toast
                    eventosDelUsuario = dao.getAll();
                    restoreOriginalEventosList();
                }
                break;
            }
        }
    }
}
