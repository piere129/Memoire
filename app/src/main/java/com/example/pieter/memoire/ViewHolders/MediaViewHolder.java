package com.example.pieter.memoire.ViewHolders;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.pieter.memoire.Models.Card;
import com.example.pieter.memoire.Models.Theme;
import com.example.pieter.memoire.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaViewHolder extends RecyclerView.ViewHolder {

    //in dp
    private int size = 120;

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

    @BindView(R.id.play_button_video)
    ImageView overlay;
   /* @BindView(R.id.media_description)

    TextView description;*/


    /**
     * Sets the data passed by the MediaAdapter.
     * Checks whether the uri is of an image or a video and saves the image/creates a thumbnail
     * @param card
     */
    public void setData(Card card) {
        //title.setText(card.getTitle());
       // description.setText(card.getDescription());

        if(card.getUri().isEmpty() || card.getUri() == null) {
            Picasso.get().load(R.drawable.default_image_card).resize(size, size).centerCrop().into(imageView);

        }
        else {
            Uri uri = Uri.parse(card.getUri());
            if (!card.getHasVideo()) {
                Picasso.get().load(uri).fit().centerCrop().into(imageView);
                overlay.setVisibility(View.INVISIBLE);
            }
            else {
                Bitmap bitmap2 = ThumbnailUtils.createVideoThumbnail(card.getUri(), MediaStore.Video.Thumbnails.MICRO_KIND);
                imageView.setImageBitmap(bitmap2);
                overlay.setVisibility(View.VISIBLE);
            }

        }

    }
}
