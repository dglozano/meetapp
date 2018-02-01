package com.example.dglozano.meetapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.adapters.PagoItemAdapter;
import com.example.dglozano.meetapp.modelo.Pago;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DivisionGastosPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DivisionGastosPageFragment extends android.support.v4.app.Fragment {

    private List<Pago> pagosList = new ArrayList<>();
    private PagoItemAdapter mPagoItemAdapter;
    private RecyclerView mPagosRecyclerView;


    public DivisionGastosPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment ParticipantesPageFragment.
     */
    public static DivisionGastosPageFragment newInstance() {
        DivisionGastosPageFragment fragment = new DivisionGastosPageFragment();
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
        return inflater.inflate(R.layout.fragment_dividir_gastos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mPagosRecyclerView = view.findViewById(R.id.recvw_payments_list);
        mPagoItemAdapter =  new PagoItemAdapter(pagosList);
        mPagosRecyclerView.setAdapter(mPagoItemAdapter);
        //TODO: VER QUE MOSTRAR CUANDO NO HAY PAGOS TODAVIA
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mPagosRecyclerView.setLayoutManager(mLayoutManager);
        mPagosRecyclerView.setItemAnimator(new DefaultItemAnimator());
        pagosList.addAll(Pago.getPagosMock());
        mPagoItemAdapter.notifyDataSetChanged();
    }
}
