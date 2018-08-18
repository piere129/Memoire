package com.example.pieter.memoire.ViewHolders;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.pieter.memoire.Models.Card;
import com.example.pieter.memoire.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineViewHolder extends RecyclerView.ViewHolder {

    private int size = 120;
    @BindView(R.id.timeline_image_grid)
    ImageView imageView;

    @BindView(R.id.play_button_video_timeline)
    ImageView overlay;

    public TimelineViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    /**
     * Sets the data passed by the TimelineAdapter.
     * Checks whether the uri is of an image or a video and saves the image/creates a thumbnail
     *
     * @param card
     */
    public void setData(Card card) {
        if (card.getUri().isEmpty() || card.getUri() == null) {
            Picasso.get().load(R.drawable.default_image_card).resize(size, size).centerCrop().into(imageView);

        } else {
            Uri uri = Uri.parse(card.getUri());
            if (!card.getHasVideo()) {
                Picasso.get().load(uri).fit().centerCrop().into(imageView);
                overlay.setVisibility(View.INVISIBLE);

            } else {
                Bitmap bitmap2 = ThumbnailUtils.createVideoThumbnail(card.getUri(), MediaStore.Video.Thumbnails.MICRO_KIND);
                imageView.setImageBitmap(bitmap2);
                overlay.setVisibility(View.VISIBLE);
            }

        }
    }
}
