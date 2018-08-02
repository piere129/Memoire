package com.example.pieter.memoire.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pieter.memoire.Activities.MainActivity;
import com.example.pieter.memoire.Activities.ThemeActivity;
import com.example.pieter.memoire.Adapters.ThemeAdapter;
import com.example.pieter.memoire.ClickListeners.ClickListener;
import com.example.pieter.memoire.ClickListeners.ItemTouchListener;
import com.example.pieter.memoire.Models.Card;
import com.example.pieter.memoire.Models.Theme;
import com.example.pieter.memoire.Persistence.ThemeDataSource;
import com.example.pieter.memoire.Persistence.ThemeDatabase;
import com.example.pieter.memoire.Persistence.ThemeRepository;
import com.example.pieter.memoire.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ThemesFragment extends Fragment {

    private List<Theme> themesList = new ArrayList<>();
    RecyclerView themesRecyclerView;
    ThemeAdapter themeAdapter;
    private ThemeRepository themeRepository;
    private CompositeDisposable compositeDisposable;


    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.theme_activity, container, false);
        ButterKnife.bind(this, v);

        ThemeDatabase database = ThemeDatabase.getInstance(getActivity());
        themeRepository = ThemeRepository.getInstance(ThemeDataSource.getInstance(database.themeDao()));
        compositeDisposable = new CompositeDisposable();
        if (savedInstanceState == null || !savedInstanceState.containsKey("themes")) {
            //generateData();
            loadData();

        } else {
            themesList = savedInstanceState.getParcelableArrayList("themes");
        }
        themeAdapter = new ThemeAdapter(themesList, getActivity());
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

                Intent intent = new Intent(getContext(), ThemeActivity.class);
                getActivity().overridePendingTransition(R.anim.start, R.anim.end);
                Theme t = themesList.get(position);
                Log.d("wow", Integer.toString(position));
                intent.putExtra("theme", t);
                intent.putExtra("position", position);
                startActivityForResult(intent, 1);
            }

            @Override
            public void onLongClick(View v, final int position) {
                deleteTheme(position);
            }
        }));
        return v;
    }

    private void generateData() {

        List<Card> cards1 = new ArrayList<>();
        cards1.add(new Card(Uri.parse("android.resource://com.example.pieter.memoire/drawable/relations").toString()
                , "Card 1", "Something about relationships"));
        cards1.add(new Card(Uri.parse("android.resource://com.example.pieter.memoire/drawable/health").toString(), "Card 2", "Something about life"));
        cards1.add(new Card(Uri.parse("android.resource://com.example.pieter.memoire/drawable/firewatch").toString(), "Card 3", "Something about home"));
        cards1.add(new Card("Card 4", "Something about nothing"));

        List<Card> cards2 = new ArrayList<>();
        cards2.add(new Card(Uri.parse("android.resource://com.example.pieter.memoire/drawable/firewatch").toString(), "Card 1", "Something about life"));
        cards2.add(new Card(Uri.parse("android.resource://com.example.pieter.memoire/drawable/relations").toString(), "Card 2", "Something about living"));
        cards2.add(new Card(Uri.parse("android.resource://com.example.pieter.memoire/drawable/health").toString(), "Card 3", "Something about life"));

        List<Card> cards3 = new ArrayList<>();
        cards3.add(new Card(Uri.parse("android.resource://com.example.pieter.memoire/drawable/health").toString(), "Card 3", "Something about life"));
        cards3.add(new Card(Uri.parse("android.resource://com.example.pieter.memoire/drawable/firewatch").toString(), "Card 1", "Something about life"));
        cards3.add(new Card(Uri.parse("android.resource://com.example.pieter.memoire/drawable/default_image").toString(), "Card 2", "Something about living"));


        Theme theme1 = new Theme("Relaties");
        themesList.add(theme1);

        Theme theme2 = new Theme("Wonen", cards1);
        themesList.add(theme2);

        Theme theme3 = new Theme("Vrijetijd & Dagbesteding" , cards2);
        themesList.add(theme3);

        Theme theme4 = new Theme("Gezondheid & Welzijn" ,cards3);
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

    private void addTheme(String s) {
        final String temp = s;
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {

            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                Theme t = new Theme(temp);

                themeRepository.addTheme(t);
                e.onComplete();

            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Toast.makeText(getActivity(), "User added", Toast.LENGTH_LONG).show();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //for refreshing data
                        loadData();
                    }
                });
        compositeDisposable.add(disposable);

    }

    private void deleteTheme(int position) {
        final int temp = position;
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {

            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                themeRepository.deleteTheme(themesList.get(temp));
                e.onComplete();

            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Toast.makeText(getActivity(), "Theme removed", Toast.LENGTH_LONG).show();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //for refreshing data
                        loadData();
                    }
                });
        compositeDisposable.add(disposable);

    }

    private void updateTheme(Theme t) {

        final Theme temp = t;
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {

            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                themeRepository.modifyTheme(temp);
                e.onComplete();

            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Toast.makeText(getActivity(), "Theme modified", Toast.LENGTH_LONG).show();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //for refreshing data
                        loadData();
                    }
                });
        compositeDisposable.add(disposable);
    }


    private void loadData() {
        Disposable disposable = themeRepository.getThemes()
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Theme>>() {
                    @Override
                    public void accept(List<Theme> themes) throws Exception {
                        onGetAllThemeSuccess(themes);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), ""+throwable.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetAllThemeSuccess(List<Theme> themes) {
        themesList.clear();
        themesList.addAll(themes);
        themesRecyclerView.getRecycledViewPool().clear();
        themeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();

    }

}
