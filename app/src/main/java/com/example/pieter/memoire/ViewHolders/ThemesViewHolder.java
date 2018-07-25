package com.example.pieter.memoire.ViewHolders;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pieter.memoire.Models.Theme;
import com.example.pieter.memoire.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThemesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.cardview) CardView themeWrapper;
    @BindView(R.id.card_title) TextView name;
    @BindView(R.id.card_description) TextView description;
    @BindView(R.id.card_image) ImageView imageView;

    public ThemesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(Theme theme)
    {
        name.setText(theme.getName());
        description.setText(theme.getDescription());
    }
}
