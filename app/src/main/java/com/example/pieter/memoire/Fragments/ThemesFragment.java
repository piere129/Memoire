package com.example.pieter.memoire.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pieter.memoire.Adapters.ThemeAdapter;
import com.example.pieter.memoire.Models.Theme;
import com.example.pieter.memoire.R;

import java.util.ArrayList;
import java.util.List;

public class ThemesFragment extends Fragment {

    private List<Theme> themesList = new ArrayList<>();
    private RecyclerView themesRecyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.recycler_view,container,false);
        themesRecyclerView = (RecyclerView) v.findViewById(R.id.themeRecyclerView);
        generateData();
        ThemeAdapter themeAdapter =  new ThemeAdapter(themesList,getActivity());
        themesRecyclerView.setAdapter(themeAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        themesRecyclerView.setLayoutManager(layoutManager);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

    private void generateData(){
        for(int i = 0; i<100; i++)
        {
            Theme theme = new Theme(i + "hallo vrienden it's ya boy poeter");
            themesList.add(theme);
        }
    }

}
