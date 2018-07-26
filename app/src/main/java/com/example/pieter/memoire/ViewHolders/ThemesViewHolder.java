package com.example.pieter.memoire.ViewHolders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pieter.memoire.Models.Theme;
import com.example.pieter.memoire.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThemesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.cardview)
    CardView themeWrapper;
    @BindView(R.id.card_title)
    TextView name;
    @BindView(R.id.card_image)
    ImageView imageView;

    public ThemesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(Theme theme) {
        name.setText(theme.getName());
        if(theme.getImage() != 0) {
            Picasso.get().load(theme.getImage()).resize(300, 300).centerCrop().into(imageView);
        }
        else {
            Picasso.get().load(R.drawable.default_image).resize(300, 300).centerCrop().into(imageView);
        }
    }
}
