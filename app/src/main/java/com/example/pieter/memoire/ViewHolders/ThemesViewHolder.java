package com.example.pieter.memoire.ViewHolders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pieter.memoire.Models.Theme;
import com.example.pieter.memoire.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThemesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.themes_linear_layout)
    RelativeLayout themeWrapper;

    @BindView(R.id.card_title)
    TextView name;

    public ThemesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(Theme theme) {
        name.setText(theme.getName());
    }
}
