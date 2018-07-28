package com.example.pieter.memoire.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pieter.memoire.Activities.ThemeActivity;
import com.example.pieter.memoire.Adapters.ThemeAdapter;
import com.example.pieter.memoire.ClickListeners.ClickListener;
import com.example.pieter.memoire.ClickListeners.ItemTouchListener;
import com.example.pieter.memoire.Models.Card;
import com.example.pieter.memoire.Models.Theme;
import com.example.pieter.memoire.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThemesFragment extends Fragment {

    private List<Theme> themesList = new ArrayList<>();
    RecyclerView themesRecyclerView;
    ThemeAdapter themeAdapter;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.theme_activity, container, false);
        ButterKnife.bind(this, v);
        if (savedInstanceState == null || !savedInstanceState.containsKey("themes")) {
            generateData();
        } else {
            themesList = savedInstanceState.getParcelableArrayList("themes");
        }
        themeAdapter = new ThemeAdapter(themesList, getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        themesRecyclerView = (RecyclerView) v.findViewById(R.id.themeRecyclerView);

        themesRecyclerView.setAdapter(themeAdapter);
        themesRecyclerView.setLayoutManager(layoutManager);

        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        themesRecyclerView.setItemAnimator(itemAnimator);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final View dialogview = getLayoutInflater().inflate(R.layout.dialog_create_theme, null);
                final EditText inputName = (EditText) dialogview.findViewById(R.id.input_name);
                Button btnCreateTheme = (Button) dialogview.findViewById(R.id.btn_create_theme);

                builder.setView(dialogview);

                final AlertDialog dialog = builder.show();

                btnCreateTheme.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (!inputName.getText().toString().isEmpty()) {
                            Theme t = new Theme(inputName.getText().toString());
                            themesList.add(t);
                            themeAdapter.notifyItemInserted(themesList.size() - 1);
                            dialog.dismiss();

                        } else {
                            Toast.makeText(getContext(), "Please fill in the name field!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        themesRecyclerView.addOnItemTouchListener(new ItemTouchListener(getContext(), themesRecyclerView, new ClickListener() {
            @Override
            public void onClick(View v, int position) {

                Intent intent = new Intent(getContext(), ThemeActivity.class);
                Theme t = themesList.get(position);
                Log.d("wow",Integer.toString(position));
                intent.putExtra("theme", t);
                intent.putExtra("position",position);
                startActivityForResult(intent,1);
            }

            @Override
            public void onLongClick(View v, int position) {
                themesList.remove(position);
                themesRecyclerView.getRecycledViewPool().clear();
                themeAdapter.notifyItemRemoved(position);
            }
        }));
        return v;
    }

    private void generateData() {

        List<Card> cards = new ArrayList<>();
        cards.add(new Card( "Card 1", "Something about relationships"));
        cards.add(new Card(R.drawable.health, "Card 2", "Something about life"));
        cards.add(new Card(R.drawable.firewatch, "Card 3", "Something about worthiness"));
        cards.add(new Card(R.drawable.vrijetijd, "Card 4", "Something about love"));


        Theme theme1 = new Theme("Relaties", R.drawable.relations, cards);
        themesList.add(theme1);

        Theme theme2 = new Theme("Wonen", R.drawable.firewatch);
        themesList.add(theme2);

        Theme theme3 = new Theme("Vrijetijd & Dagbesteding", R.drawable.vrijetijd, cards);
        themesList.add(theme3);

        Theme theme4 = new Theme("Gezondheid & Welzijn", R.drawable.health, cards);
        themesList.add(theme4);

        //load other themes v
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("themes", new ArrayList<Theme>(themesList));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Theme theme =data.getParcelableExtra("result");
                int position = data.getIntExtra("position",20) ;
                Log.d("test",Integer.toString(position));
                themesList.set(position,theme);
                themesRecyclerView.getRecycledViewPool().clear();
                themeAdapter.notifyItemChanged(position);
            }
        }
    }
}
