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
import com.example.dglozano.meetapp.adapters.PagoItemAdapter;
import com.example.dglozano.meetapp.dao.mock.MockDaoEvento;
import com.example.dglozano.meetapp.modelo.Evento;
import com.example.dglozano.meetapp.modelo.Pago;

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

    private List<Pago> pagosListDelEvento = Pago.getPagosMock();

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

        evento = MockDaoEvento.getInstance().getById(getArguments().getInt(EVENTO_ID));
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
        pagosListDisplayed.addAll(pagosListDelEvento);
        mPagoItemAdapter.notifyDataSetChanged();
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
}
