package com.example.pieter.memoire.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pieter.memoire.Activities.ThemeActivity;
import com.example.pieter.memoire.Adapters.ThemeAdapter;
import com.example.pieter.memoire.ClickListeners.ClickListener;
import com.example.pieter.memoire.ClickListeners.ItemTouchListener;
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
        generateData();
        final ThemeAdapter themeAdapter =  new ThemeAdapter(themesList,getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        themesRecyclerView = (RecyclerView) v.findViewById(R.id.themeRecyclerView);

        themesRecyclerView.setAdapter(themeAdapter);
        themesRecyclerView.setLayoutManager(layoutManager);

        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        themesRecyclerView.setItemAnimator(itemAnimator);

        themesRecyclerView.addOnItemTouchListener(new ItemTouchListener(getContext(), themesRecyclerView, new ClickListener() {
            @Override
            public void onClick(View v, int position) {
                Toast.makeText(getActivity(), "Hold a theme to delete it!",
                        Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getContext(), ThemeActivity.class);
                String extra = themesList.get(position).getName();
                intent.putExtra("name",extra);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View v, int position) {
               themeAdapter.removeItem(position);
            }
        }));
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

    private void generateData(){

        Theme theme1 = new Theme("Relaties", R.drawable.relations );
        themesList.add(theme1);

        Theme theme2 = new Theme("Wonen", R.drawable.firewatch );
        themesList.add(theme2);

        Theme theme3 = new Theme("Vrijetijd & Dagbesteding", R.drawable.vrijetijd );
        themesList.add(theme3);

        Theme theme4 = new Theme("Gezondheid & Welzijn", R.drawable.health );
        themesList.add(theme4);

        //load other themes v
    }

}
