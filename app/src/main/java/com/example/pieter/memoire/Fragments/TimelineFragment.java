package com.example.pieter.memoire.Fragments;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.pieter.memoire.Adapters.TimelineAdapter;
import com.example.pieter.memoire.ClickListeners.ClickListener;
import com.example.pieter.memoire.ClickListeners.ItemTouchListener;
import com.example.pieter.memoire.Models.Card;
import com.example.pieter.memoire.Persistence.ThemeDatabase;
import com.example.pieter.memoire.R;
import com.example.pieter.memoire.Utilities.GridAutofitLayoutManager;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineFragment extends Fragment {

    private List<Card> cards = new ArrayList<>();
    ThemeDatabase themeDatabase;

    @BindView(R.id.btn_ascending)
    Button sortAsc;

    @BindView(R.id.btn_descending)
    Button sortDesc;


    /**
     * Initialises the TimelineFragment and binds its' Recyclerview +
     * the Onclick event for showing details of the card
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.timeline_fragment, container, false);
        ButterKnife.bind(this, v);

        themeDatabase = ThemeDatabase.getInstance(getActivity());

        cards = themeDatabase.getCardDao().getCardsInGallery();
        Collections.sort(cards);

        final TimelineAdapter adapter = new TimelineAdapter(cards, getActivity());
        RecyclerView timelineRecyclerView = (RecyclerView) v.findViewById(R.id.timeline_recyclerview);
        timelineRecyclerView.setAdapter(adapter);
        timelineRecyclerView.setTag("timeline_recyclerview");

        RecyclerView.LayoutManager layoutManager = new GridAutofitLayoutManager(getActivity(), 400);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        timelineRecyclerView.setLayoutManager(layoutManager);

        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        timelineRecyclerView.setItemAnimator(itemAnimator);

        timelineRecyclerView.addOnItemTouchListener(new ItemTouchListener(getContext(), timelineRecyclerView,
                new ClickListener() {
                    @Override
                    public void onClick(View v, int position) {
                        setupDetailsDialog(position);
                    }

                    @Override
                    public void onLongClick(View v, final int position) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Delete Media")
                                .setMessage("Do you really want to remove this item from the timeline?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("Delete card from Timeline", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int buttonClicked) {
                                        Card card = cards.get(position);
                                        card.setInTimeline(0);
                                        themeDatabase.getCardDao().modifyCard(card);
                                        cards.remove(position);
                                        adapter.notifyItemRemoved(position);
                                    }})
                                .setNegativeButton("No", null).show();

                    }
                }));

        sortDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(cards);
                adapter.notifyDataSetChanged();
            }
        });

        sortAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.reverse(cards);
                adapter.notifyDataSetChanged();
            }
        });

        return v;
    }

    /**
     * Method for initialising the details dialog
     *
     * @param position
     */
    private void setupDetailsDialog(int position) {

        final Card card = cards.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogViewDetailsImage = getLayoutInflater().inflate(R.layout.media_details, null);
        View dialogViewDetailsVideo = getLayoutInflater().inflate(R.layout.media_details_video, null);

        TextView titleDetails = (TextView) dialogViewDetailsImage.findViewById(R.id.title_details);
        TextView descriptionDetails = (TextView) dialogViewDetailsImage.findViewById(R.id.description_details);
        TextView dateDetails = (TextView) dialogViewDetailsImage.findViewById(R.id.date_details);
        ImageView imageDetails = (ImageView) dialogViewDetailsImage.findViewById(R.id.image_details);
        Button editButtonImage = (Button) dialogViewDetailsImage.findViewById(R.id.btn_edit_card);
        Button editButtonVideo = (Button) dialogViewDetailsVideo.findViewById(R.id.btn_edit_card_video);
        Button cardToTimelineButtonImage = (Button) dialogViewDetailsImage.findViewById(R.id.btn_add_card_to_timeline);
        Button cardToTimelineButtonVideo = (Button) dialogViewDetailsVideo.findViewById(R.id.btn_add_card_to_timeline_video);
        editButtonImage.setVisibility(View.INVISIBLE);
        editButtonVideo.setVisibility(View.INVISIBLE);
        cardToTimelineButtonImage.setVisibility(View.INVISIBLE);
        cardToTimelineButtonVideo.setVisibility(View.INVISIBLE);

        final VideoView videoDetails;

        if (card.getHasVideo()) {
            titleDetails = (TextView) dialogViewDetailsVideo.findViewById(R.id.title_details_video);
            descriptionDetails = (TextView) dialogViewDetailsVideo.findViewById(R.id.description_details_video);
            dateDetails = (TextView) dialogViewDetailsVideo.findViewById(R.id.date_details_video);
            videoDetails = (VideoView) dialogViewDetailsVideo.findViewById(R.id.video_details);

            videoDetails.setMediaController(new MediaController(getContext()));
            videoDetails.setVideoPath(card.getUri());
            videoDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videoDetails.start();
                }
            });
        }

        if (card.getHasVideo()) {
            builder.setView(dialogViewDetailsVideo);
        } else {
            builder.setView(dialogViewDetailsImage);
        }

        final AlertDialog dialog = builder.show();

        titleDetails.setText(card.getTitle());
        descriptionDetails.setText(card.getDescription());
        dateDetails.setText(card.getDate());
        Picasso.get().load(Uri.parse(card.getUri())).into(imageDetails);
    }
}
