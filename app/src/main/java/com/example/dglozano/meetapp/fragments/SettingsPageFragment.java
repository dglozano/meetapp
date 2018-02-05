package com.example.dglozano.meetapp.fragments;

/**
 * Created by augusto on 03/02/2018.
 */


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dglozano.meetapp.R;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Use the {@link SettingsPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsPageFragment extends android.support.v4.app.Fragment{

    public SettingsPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment EventosPageFragment.
     */
    public static SettingsPageFragment newInstance() {
        SettingsPageFragment fragment = new SettingsPageFragment();
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

}
