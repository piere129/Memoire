package com.example.pieter.memoire.Fragments;

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
        final ThemeAdapter themeAdapter = new ThemeAdapter(themesList, getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        final RecyclerView themesRecyclerView = (RecyclerView) v.findViewById(R.id.themeRecyclerView);

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
                String extra = t.getName();
                intent.putExtra("theme", t);
                //startactivityforresult to edit changed list of theme object here, pass position!
                startActivity(intent);
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
        cards.add(new Card(R.drawable.relations, "Card 1", "Something about relationships"));
        cards.add(new Card(R.drawable.relations, "Card 2", "Something about life"));
        cards.add(new Card(R.drawable.relations, "Card 3", "Something about worthiness"));
        cards.add(new Card(R.drawable.relations, "Card 4", "Something about love"));


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
}
