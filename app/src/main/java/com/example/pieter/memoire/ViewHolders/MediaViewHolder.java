package com.example.pieter.memoire.ViewHolders;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pieter.memoire.Models.Card;
import com.example.pieter.memoire.Models.Theme;
import com.example.pieter.memoire.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaViewHolder extends RecyclerView.ViewHolder {

    private int size = 400;

    public MediaViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @BindView(R.id.cardview_media)
    CardView mediawrapper;
    /*@BindView(R.id.media_title)
    TextView title;*/
    @BindView(R.id.media_image_grid)
    ImageView imageView;
   /* @BindView(R.id.media_description)

    TextView description;*/


    public void setData(Card card) {
        //title.setText(card.getTitle());
       // description.setText(card.getDescription());

        if(card.getImage() != 0) {
            Picasso.get().load(card.getImage()).resize(size, size).centerCrop().into(imageView);
        }
        else {
            Picasso.get().load(R.drawable.default_image_card).resize(size, size).centerCrop().into(imageView);
        }

    }
}
