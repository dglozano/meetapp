package com.example.dglozano.meetapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
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
import com.example.dglozano.meetapp.adapters.ParticipanteItemAdapter;
import com.example.dglozano.meetapp.dao.Dao;
import com.example.dglozano.meetapp.dao.MockDaoEvento;
import com.example.dglozano.meetapp.dao.MockDaoParticipante;
import com.example.dglozano.meetapp.modelo.Evento;
import com.example.dglozano.meetapp.modelo.Participante;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParticipantesPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParticipantesPageFragment extends android.support.v4.app.Fragment {

    private List<Participante> participantesListDisplayed = new ArrayList<>();
    private ParticipanteItemAdapter mParticipanteAdapter;
    private RecyclerView mParticipantesRecyclerView;

    private Dao<Participante> dao;
    private List<Participante> participantesListDelEvento;

    private static final String EVENTO_ID = "EVENTO_ID";
    private Evento evento;

    public ParticipantesPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment ParticipantesPageFragment.
     */
    public static ParticipantesPageFragment newInstance(int eventoId) {
        ParticipantesPageFragment fragment = new ParticipantesPageFragment();
        Bundle args = new Bundle();
        args.putInt(EVENTO_ID, eventoId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //TODO cambiar a sqlite cuando se implemente
        evento = MockDaoEvento.getInstance().getById(getArguments().getInt(EVENTO_ID));
        dao = MockDaoParticipante.getInstance();
        participantesListDelEvento = dao.getAll();
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
        mParticipanteAdapter = new ParticipanteItemAdapter(participantesListDisplayed);
        //TODO: VER QUE MOSTRAR CUANDO NO HAY PARTICIPANTES TODAVIA
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                getActivity().getApplicationContext());
        mParticipantesRecyclerView.setLayoutManager(mLayoutManager);
        mParticipantesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mParticipantesRecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL));
        mParticipantesRecyclerView.setAdapter(mParticipanteAdapter);
        participantesListDisplayed.addAll(participantesListDelEvento);
        mParticipanteAdapter.notifyDataSetChanged();
    }

    private void search(String query) {
        List<Participante> result = new ArrayList<>();
        for(Participante p : participantesListDelEvento) {
            if(p.matches(query)) {
                result.add(p);
            }
        }
        participantesListDisplayed.clear();
        participantesListDisplayed.addAll(result);
        System.out.println(participantesListDisplayed);
        mParticipanteAdapter.notifyDataSetChanged();
    }

    private void restoreOriginalParticipantesList() {
        participantesListDisplayed.clear();
        participantesListDisplayed.addAll(participantesListDelEvento);
        mParticipanteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        final MenuItem searchItem = menu.findItem(R.id.toolbar_search);
        final SearchView searchView =
                (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new ParticipantesPageFragment.MyOnQueryTextListener());
        searchView.setOnCloseListener(new ParticipantesPageFragment.MyOnCloseListener());
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
                restoreOriginalParticipantesList();
            }
            return false;
        }
    }

    private class MyOnCloseListener implements SearchView.OnCloseListener {
        @Override
        public boolean onClose() {
            restoreOriginalParticipantesList();
            return false;
        }
    }
}
