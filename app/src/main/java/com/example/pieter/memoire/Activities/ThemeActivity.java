package com.example.pieter.memoire.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.pieter.memoire.Fragments.MediaFragment;
import com.example.pieter.memoire.Models.Theme;
import com.example.pieter.memoire.R;


public class ThemeActivity extends AppCompatActivity {

    Fragment mediaFragment;
    Theme theme;

    /**
     * Initialises the ThemeActivity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_container);

        final Intent intent = getIntent();
        theme = intent.getParcelableExtra("theme");
        int position = intent.getIntExtra("position", -1);

        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.media_fragment_container) == null) {
            mediaFragment = new MediaFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("theme", theme);
            bundle.putInt("position", position);
            mediaFragment.setArguments(bundle);
            FragmentTransaction transaction = fm.beginTransaction();
            //try also with media_container later! media_fragment_container might be unnecessary!
            transaction.add(R.id.media_fragment_container, mediaFragment);
            transaction.commit();
        }
    }


}
