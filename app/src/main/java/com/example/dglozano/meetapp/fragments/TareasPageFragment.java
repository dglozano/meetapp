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
import com.example.dglozano.meetapp.adapters.TareaItemAdapter;
import com.example.dglozano.meetapp.modelo.Tarea;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TareasPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TareasPageFragment extends android.support.v4.app.Fragment {

    private List<Tarea> tareasList = new ArrayList<>();
    private TareaItemAdapter mTareaAdapter;
    private RecyclerView mTareasRecyclerview;

    public TareasPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment TareasPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TareasPageFragment newInstance() {
        TareasPageFragment fragment = new TareasPageFragment();
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
        return inflater.inflate(R.layout.fragment_tareas_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTareasRecyclerview = view.findViewById(R.id.recvw_tareas_list);
        mTareaAdapter =  new TareaItemAdapter(tareasList);
        mTareasRecyclerview.setAdapter(mTareaAdapter);
        //TODO: ver que mostrar si no hay tareas aun
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mTareasRecyclerview.setLayoutManager(mLayoutManager);
        mTareasRecyclerview.setItemAnimator(new DefaultItemAnimator());
        tareasList.addAll(Tarea.getTareasMock());
        mTareaAdapter.notifyDataSetChanged();
    }
}