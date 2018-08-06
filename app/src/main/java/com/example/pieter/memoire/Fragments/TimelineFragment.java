package com.example.pieter.memoire.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pieter.memoire.Adapters.TimelineAdapter;
import com.example.pieter.memoire.Models.Card;
import com.example.pieter.memoire.Persistence.ThemeDatabase;
import com.example.pieter.memoire.R;
import com.example.pieter.memoire.Utilities.GridAutofitLayoutManager;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineFragment extends Fragment {

    private List<Card> cards = new ArrayList<>();
    ThemeDatabase themeDatabase;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.timeline_fragment, container, false);
        ButterKnife.bind(this, v);

        themeDatabase = ThemeDatabase.getInstance(getActivity());

        cards = themeDatabase.getCardDao().getAllCards();
        Collections.sort(cards);

        TimelineAdapter adapter = new TimelineAdapter(cards,getActivity());
        RecyclerView timelineRecyclerView = (RecyclerView) v.findViewById(R.id.timeline_recyclerview);
        timelineRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new GridAutofitLayoutManager(getActivity(), 400);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        timelineRecyclerView.setLayoutManager(layoutManager);

        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        timelineRecyclerView.setItemAnimator(itemAnimator);



        return v;
    }

}
