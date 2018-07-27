package com.example.pieter.memoire.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.pieter.memoire.Models.Theme;
import com.example.pieter.memoire.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThemeActivity extends AppCompatActivity {

    @BindView(R.id.textview_name)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theme_activity);
        ButterKnife.bind(this);

        final Intent intent = getIntent();
        Theme theme = intent.getParcelableExtra("theme");

        textView.setText(theme.getCards().get(0).getTitle());
    }

}
