package com.example.dglozano.meetapp.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.example.dglozano.meetapp.actividades.ContactosActivity;
import com.example.dglozano.meetapp.adapters.ParticipanteItemAdapter;
import com.example.dglozano.meetapp.dao.DaoEvento;
import com.example.dglozano.meetapp.dao.DaoEventoMember;
import com.example.dglozano.meetapp.dao.SQLiteDaoEvento;
import com.example.dglozano.meetapp.dao.SQLiteDaoParticipante;
import com.example.dglozano.meetapp.modelo.Evento;
import com.example.dglozano.meetapp.modelo.Participante;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParticipantesPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParticipantesPageFragment extends android.support.v4.app.Fragment {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private List<Participante> participantesListDisplayed = new ArrayList<>();
    private ParticipanteItemAdapter mParticipanteAdapter;
    private RecyclerView mParticipantesRecyclerView;
    private final int CREAR_PARTICIPANTE = 1;

    private DaoEvento daoEvento;
    private DaoEventoMember<Participante> daoParticipante;
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

        daoParticipante = new SQLiteDaoParticipante(getActivity());
        daoEvento = new SQLiteDaoEvento(getActivity());

        evento = daoEvento.getById(getArguments().getInt(EVENTO_ID));
        participantesListDelEvento = daoParticipante.getAllDelEvento(evento.getId());
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
        participantesListDisplayed.clear();
        participantesListDisplayed.addAll(participantesListDelEvento);
        mParticipanteAdapter.notifyDataSetChanged();

        FloatingActionButton fab = view.findViewById(R.id.fab_btn_agregar_participante);
        fab.setOnClickListener(new MyFabIconOnClickListener());
    }

    public void pedirPermisoContactos(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CONTACTS)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                        builder.setTitle("Acceso a Contactos");
                        builder.setPositiveButton(android.R.string.ok, null);
                        builder.setMessage("La APP necesita acceso a sus contactos para que los pueda agregar como participantes a un evento");
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                requestPermissions(
                                        new String[]
                                                {Manifest.permission.READ_CONTACTS}
                                        , MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                            }
                        });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            }
            else {
                Intent i = new Intent(getActivity(), ContactosActivity.class);
                i.putExtra(ContactosActivity.KEY_EVENTO_ID, evento.getId());
                startActivityForResult(i, CREAR_PARTICIPANTE);
            }
        }
        else {
            Intent i = new Intent(getActivity(), ContactosActivity.class);
            i.putExtra(ContactosActivity.KEY_EVENTO_ID, evento.getId());
            startActivityForResult(i, CREAR_PARTICIPANTE);
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case CREAR_PARTICIPANTE: {
                if(resultCode == RESULT_OK) {
                    // TODO agregar toast
                    participantesListDelEvento = daoParticipante.getAllDelEvento(evento.getId());
                    restoreOriginalParticipantesList();
                }
                break;
            }
        }
    }

    private class MyFabIconOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            pedirPermisoContactos();
        }
    }
}
