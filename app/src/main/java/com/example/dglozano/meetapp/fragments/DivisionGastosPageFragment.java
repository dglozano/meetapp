package com.example.dglozano.meetapp.fragments;

import android.os.Bundle;
import android.app.Fragment;
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
import android.widget.Toast;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.adapters.PagoItemAdapter;
import com.example.dglozano.meetapp.dao.DaoEvento;
import com.example.dglozano.meetapp.dao.DaoEventoMember;
import com.example.dglozano.meetapp.dao.SQLiteDaoEvento;
import com.example.dglozano.meetapp.dao.SQLiteDaoPago;
import com.example.dglozano.meetapp.modelo.Evento;
import com.example.dglozano.meetapp.modelo.Pago;
import com.example.dglozano.meetapp.util.CalculadorDePagos;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DivisionGastosPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DivisionGastosPageFragment extends android.support.v4.app.Fragment{

    private List<Pago> pagosListDisplayed = new ArrayList<>();
    private PagoItemAdapter mPagoItemAdapter;
    private RecyclerView mPagosRecyclerView;

    private static final String EVENTO_ID = "EVENTO_ID";
    private Evento evento;

    private DaoEvento daoEvento;
    private DaoEventoMember<Pago> daoPago;
    private List<Pago> pagosListDelEvento;

    private FloatingActionButton fab;

    public DivisionGastosPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment ParticipantesPageFragment.
     */
    public static DivisionGastosPageFragment newInstance(int eventoId) {
        DivisionGastosPageFragment fragment = new DivisionGastosPageFragment();
        Bundle args = new Bundle();
        args.putInt(EVENTO_ID,eventoId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        daoEvento = new SQLiteDaoEvento(getActivity());
        daoPago = new SQLiteDaoPago(getActivity());
        evento = daoEvento.getById(getArguments().getInt(EVENTO_ID));
        pagosListDelEvento = daoPago.getAllDelEvento(evento.getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dividir_gastos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mPagosRecyclerView = view.findViewById(R.id.recvw_payments_list);
        mPagoItemAdapter =  new PagoItemAdapter(pagosListDisplayed);
        //TODO: VER QUE MOSTRAR CUANDO NO HAY PAGOS TODAVIA
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                getActivity().getApplicationContext());
        mPagosRecyclerView.setLayoutManager(mLayoutManager);
        mPagosRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mPagosRecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL));
        mPagosRecyclerView.setAdapter(mPagoItemAdapter);
        pagosListDisplayed.clear();
        pagosListDisplayed.addAll(pagosListDelEvento);
        mPagoItemAdapter.notifyDataSetChanged();

        fab = view.findViewById(R.id.fab_btn_dividir_gastos);
        fab.setOnClickListener(new MyFabIconOnClickListener());
    }

    private void search(String query) {
        List<Pago> result = new ArrayList<>();
        for(Pago p: pagosListDelEvento){
            if(p.matches(query)){
                result.add(p);
            }
        }
        pagosListDisplayed.clear();
        pagosListDisplayed.addAll(result);
        System.out.println(pagosListDisplayed);
        mPagoItemAdapter.notifyDataSetChanged();
    }

    private void restoreOriginalPagosList(){
        pagosListDisplayed.clear();
        pagosListDisplayed.addAll(pagosListDelEvento);
        mPagoItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        final MenuItem searchItem = menu.findItem(R.id.toolbar_search);
        final SearchView searchView =
                (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new DivisionGastosPageFragment.MyOnQueryTextListener());
        searchView.setOnCloseListener(new DivisionGastosPageFragment.MyOnCloseListener());
    }

    private void calcularPagos() {
        final CalculadorDePagos calculadorDePagos = new CalculadorDePagos(getActivity(),evento.getId());
        if(evento.isDivisionGastosYaHecha()){
            DialogDivisionGastosSuccess.newInstance(evento.getGastosTotales(),
                    evento.getGastosPorParticipante(), true)
                    .show(getActivity().getFragmentManager(), "dialog");
        } else if(calculadorDePagos.puedeCalcular()) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    calculadorDePagos.calcularPagos();
                    //FIXME ON CASCADE
                    evento.addAllPagos(calculadorDePagos.getListaPagos());
                    for(Pago p: evento.getPagos()){
                        daoPago.save(p, evento.getId());
                    }
                    evento.setDivisionGastosYaHecha(true);
                    evento.setGastosTotales(calculadorDePagos.getGastoTotal());
                    evento.setGastosPorParticipante(calculadorDePagos.getGastoPorParticipante());
                    //daoEvento.save(evento);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogDivisionGastosSuccess.newInstance(evento.getGastosTotales(),
                                    evento.getGastosPorParticipante(), false)
                                    .show(getActivity().getFragmentManager(), "dialog");
                            pagosListDelEvento = calculadorDePagos.getListaPagos();
                            restoreOriginalPagosList();
                            fab.setImageResource(R.drawable.ic_info_white_24dp);
                        }
                    });
                }
            };
            Thread t = new Thread(r);
            t.start();
        } else {
            Toast.makeText(getContext(), R.string.tareas_sin_finalizar, Toast.LENGTH_SHORT);
        }
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
            if(query.trim().isEmpty()){
                restoreOriginalPagosList();
            }
            return false;
        }
    }

    private class MyOnCloseListener implements SearchView.OnCloseListener {
        @Override
        public boolean onClose() {
            restoreOriginalPagosList();
            return false;
        }
    }

    private class MyFabIconOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            calcularPagos();
        }
    }
}
