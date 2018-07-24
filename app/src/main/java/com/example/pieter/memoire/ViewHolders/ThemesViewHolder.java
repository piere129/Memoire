package com.example.pieter.memoire.ViewHolders;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.pieter.memoire.R;

import butterknife.BindView;

public class ThemesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.themeSubView) ConstraintLayout themeWrapper;
    @BindView(R.id.themeName) TextView name;

    public ThemesViewHolder(View itemView) {
        super(itemView);
    }
}
