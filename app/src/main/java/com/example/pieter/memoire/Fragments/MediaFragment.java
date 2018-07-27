package com.example.pieter.memoire.Fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pieter.memoire.Activities.MainActivity;
import com.example.pieter.memoire.Adapters.MediaAdapter;
import com.example.pieter.memoire.ClickListeners.ClickListener;
import com.example.pieter.memoire.ClickListeners.ItemTouchListener;
import com.example.pieter.memoire.Models.Card;
import com.example.pieter.memoire.Models.Theme;
import com.example.pieter.memoire.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaFragment extends Fragment {


    @BindView(R.id.fab_photos)
    FloatingActionButton fab_photos;

    Theme theme;
    int position;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.media_activity, container, false);
        ButterKnife.bind(this, v);

        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        theme = getArguments().getParcelable("theme");
        position = getArguments().getInt("position",-1);
        Log.d("position1",Integer.toString(position));

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_media);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_camera));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",theme);
                returnIntent.putExtra("position",position);
                Log.d("positionInFragment2",Integer.toString(position));
                getActivity().setResult(Activity.RESULT_OK,returnIntent);
                getActivity().finish();
            }
        });

        final MediaAdapter adapter = new MediaAdapter(theme.getCards(), getActivity());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
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
                Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();
            }
        });

        mediaRecyclerView.addOnItemTouchListener(new ItemTouchListener(getContext(), mediaRecyclerView, new ClickListener() {
            @Override
            public void onClick(View v, int position) {
                Toast.makeText(getActivity(), "short tap", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View v, int position) {
                List<Card> cards = theme.getCards();
                cards.remove(position);
                theme.setCards(cards);
                mediaRecyclerView.getRecycledViewPool().clear();
                adapter.notifyItemRemoved(position);
            }
        }));

        return v;
    }
}
