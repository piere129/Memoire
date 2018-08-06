package com.example.pieter.memoire.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.pieter.memoire.R;

import com.example.pieter.memoire.Models.Card;
import com.example.pieter.memoire.ViewHolders.TimelineViewHolder;

import java.util.List;

public class TimelineAdapter extends  RecyclerView.Adapter<TimelineViewHolder> {

    private List<Card> cards;
    private Context context;

    public TimelineAdapter(List<Card> cards,  Context context)
    {
        this.cards = cards;
        this.context = context;
    }


    @NonNull
    @Override
    public TimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_activity_card, parent, false);
        TimelineViewHolder timelineViewHolder = new TimelineViewHolder(v);
        return timelineViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineViewHolder holder, int position) {
        holder.setData(cards.get(position));
    }

    @Override
    public int getItemCount() {
        if (cards != null) {
            return cards.size();
        }
        return 0;
    }
}
