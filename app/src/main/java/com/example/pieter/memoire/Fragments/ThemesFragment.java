package com.example.pieter.memoire.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.pieter.memoire.Activities.ThemeActivity;
import com.example.pieter.memoire.Adapters.ThemeAdapter;
import com.example.pieter.memoire.ClickListeners.ButtonClickListener;
import com.example.pieter.memoire.ClickListeners.ClickListener;
import com.example.pieter.memoire.ClickListeners.ItemTouchListener;
import com.example.pieter.memoire.Models.Card;
import com.example.pieter.memoire.Models.Theme;
import com.example.pieter.memoire.Persistence.ThemeDatabase;
import com.example.pieter.memoire.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;


public class ThemesFragment extends Fragment implements ButtonClickListener {

    private List<Theme> themesList = new ArrayList<>();
    RecyclerView themesRecyclerView;
    ThemeAdapter themeAdapter;
    CompositeDisposable compositeDisposable;
    ThemeDatabase themeDatabase;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    /**
     * Initialises the Fragment and does the setup for the Recyclerview and
     * its necessary click events for creating a new theme or deleting themes.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.theme_activity, container, false);
        ButterKnife.bind(this, v);

        themeDatabase = ThemeDatabase.getInstance(getActivity());
        if(themeDatabase.getThemeDao().getThemes().size() == 0) {
            generateData();
        }
        compositeDisposable = new CompositeDisposable();
        if (savedInstanceState == null || !savedInstanceState.containsKey("themes")) {
            //generateData();
            List<Theme> themes = themeDatabase.getThemeDao().getThemes();
            for (Theme t : themes) {
                Theme temp = t;
                //.getCardsFromTheme(temp.getId())
                //temp.setCards();
                List<Card> cards = themeDatabase.getCardDao().getCardsFromTheme(t.getId());
                temp.setCards(cards);
                themesList.add(temp);
            }


        } else {
            themesList = savedInstanceState.getParcelableArrayList("themes");
        }
        themeAdapter = new ThemeAdapter(themesList, getActivity(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        themesRecyclerView = (RecyclerView) v.findViewById(R.id.themeRecyclerView);

        themesRecyclerView.setAdapter(themeAdapter);
        themesRecyclerView.setLayoutManager(layoutManager);
        themesRecyclerView.addItemDecoration(dividerItemDecoration);

        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        themesRecyclerView.setItemAnimator(itemAnimator);
        themesRecyclerView.setTag("theme_recyclerview");


        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final View dialogview = getLayoutInflater().inflate(R.layout.dialog_create_theme, null);
                final EditText inputName = (EditText) dialogview.findViewById(R.id.input_name);
                ImageButton btnEditTheme = (ImageButton) dialogview.findViewById(R.id.btn_edit_theme);
                Button btnCreateTheme = (Button) dialogview.findViewById(R.id.btn_create_theme);

                builder.setView(dialogview);

                final AlertDialog dialog = builder.show();

                btnCreateTheme.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (!inputName.getText().toString().isEmpty()) {

                            addTheme(inputName.getText().toString());
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

            }

            @Override
            public void onLongClick(View v, final int position) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Theme")
                        .setMessage("Do you really want to delete this theme?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Delete theme", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int buttonClicked) {
                                deleteTheme(position);
                            }})
                        .setNegativeButton("No", null).show();

            }
        }));
        return v;
    }

    private void generateData() {
        themeDatabase.getThemeDao().addTheme(new Theme("Relaties"));
        themeDatabase.getThemeDao().addTheme(new Theme("Wonen"));
        themeDatabase.getThemeDao().addTheme(new Theme("Vrije tijd & dagbesteding"));
        themeDatabase.getThemeDao().addTheme(new Theme("Gezondheid & welzijn"));
    }

    /**
     * Saves the state of the fragment on screen rotation
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("themes", new ArrayList<Theme>(themesList));
        super.onSaveInstanceState(outState);
    }

    /**
     * Allows the editing of theme name
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Theme theme = data.getParcelableExtra("result");
                int position = data.getIntExtra("position", 20);
                Log.d("test", Integer.toString(position));
                themesList.set(position, theme);
                themesRecyclerView.getRecycledViewPool().clear();
                themeAdapter.notifyItemChanged(position);
            }
        }
    }

    /**
     * Adds a theme to the themeDatabase and reloads the fragment
     *
     * @param s
     */
    private void addTheme(String s) {
        final String temp = s;

        Theme t = new Theme(temp);

        themeDatabase.getThemeDao().addTheme(t);
        loadData();
    }

    /**
     * Deletes a theme from the themeDatabase and reloads the fragment
     *
     * @param position
     */
    private void deleteTheme(int position) {
        final int temp = position;

        themeDatabase.getThemeDao().deleteTheme(themesList.get(temp));
        loadData();

    }

    /**
     * Calls the themes and passes it to a new method to reload the data
     */
    private void loadData() {
        List<Theme> themes = themeDatabase.getThemeDao().getThemes();
        onGetAllThemeSuccess(themes);

    }

    /**
     * Reloads the themes in the fragment
     *
     * @param themes
     */
    private void onGetAllThemeSuccess(List<Theme> themes) {
        themesList.clear();
        for (Theme t : themes) {
            t.setCards(themeDatabase.getCardDao().getCardsFromTheme(t.getId()));
        }

        themesList.addAll(themes);
        themesRecyclerView.getRecycledViewPool().clear();
        themeAdapter.notifyDataSetChanged();
    }

    /**
     * Destroys unnecessary Garbage
     */
    @Override
    public void onDestroy() {
        themeDatabase = null;
        super.onDestroy();


    }

    /**
     * starts an Intent to ThemeActivity
     *
     * @param position
     */
    @Override
    public void startIntentToCards(int position) {
        Intent intent = new Intent(getActivity(), ThemeActivity.class);
        Theme t = themesList.get(position);
        intent.putExtra("theme", t);
        intent.putExtra("position", position);
        startActivityForResult(intent, 1);
    }
}
