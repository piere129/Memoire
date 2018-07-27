package com.example.pieter.memoire.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pieter.memoire.Activities.ThemeActivity;
import com.example.pieter.memoire.Adapters.MediaAdapter;
import com.example.pieter.memoire.ClickListeners.ClickListener;
import com.example.pieter.memoire.ClickListeners.ItemTouchListener;
import com.example.pieter.memoire.Models.Theme;
import com.example.pieter.memoire.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaFragment extends Fragment {


    @BindView(R.id.fab_photos)
    FloatingActionButton fab_photos;

    Theme theme;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.theme_activity, container, false);
        ButterKnife.bind(this,v);
        theme = getArguments().getParcelable("theme");


        final MediaAdapter adapter = new MediaAdapter(theme.getCards(),getActivity());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        final RecyclerView mediaRecyclerView = (RecyclerView) v.findViewById(R.id.photosRecyclerView);

        mediaRecyclerView.setAdapter(adapter);
        mediaRecyclerView.setLayoutManager(layoutManager);

        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        mediaRecyclerView.setItemAnimator(itemAnimator);

        fab_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"test",Toast.LENGTH_LONG).show();
            }
        });

        mediaRecyclerView.addOnItemTouchListener(new ItemTouchListener(getContext(), mediaRecyclerView, new ClickListener() {
            @Override
            public void onClick(View v, int position) {
                Toast.makeText(getActivity(),"short tap",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongClick(View v, int position) {
                Toast.makeText(getActivity(),"long tap",Toast.LENGTH_LONG).show();

            }
        }));

        return v;
    }
}
