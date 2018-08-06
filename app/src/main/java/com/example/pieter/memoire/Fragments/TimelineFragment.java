package com.example.pieter.memoire.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pieter.memoire.Models.Card;
import com.example.pieter.memoire.R;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineFragment extends Fragment {

    private List<Card> cards;
    @BindView(R.id.test_timeline)
    TextView test;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.timeline_fragment, container, false);
        ButterKnife.bind(this, v);
        test.setText("test timeline");
        return v;
    }

}
