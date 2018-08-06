package com.example.pieter.memoire.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pieter.memoire.Models.Card;
import com.example.pieter.memoire.R;
import com.example.pieter.memoire.ViewHolders.MediaViewHolder;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaViewHolder> {

    private List<Card> list;
    private Context context;
    MediaViewHolder mediaViewHolder;

    public MediaAdapter(List<Card> list, Context context) {
        this.list = list;
        this.context = context;
    }


    /**
     * Inflates the elements added to the Recyclerview
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_activity_card, parent, false);
        mediaViewHolder = new MediaViewHolder(v);
        return mediaViewHolder;
    }

    /**
     * Prompts the Viewholder to set the data of the Object
     * to the view
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    /**
     * Returns the itemcount of Objects
     *
     * @return
     */
    @Override
    public int getItemCount() {
        {
            if (list != null) {
                return list.size();
            }
            return 0;
        }
    }
}
