package com.example.pieter.memoire.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pieter.memoire.Models.Theme;
import com.example.pieter.memoire.R;
import com.example.pieter.memoire.ViewHolders.ThemesViewHolder;

import java.util.List;

public class ThemeAdapter extends RecyclerView.Adapter<ThemesViewHolder> {

    private List<Theme> list;
    private Context context;

    public ThemeAdapter(List<Theme> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ThemesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_activity_card, parent, false);
        ThemesViewHolder themesViewHolder = new ThemesViewHolder(v);
        return themesViewHolder;
    }

    @Override
    public void onBindViewHolder(ThemesViewHolder holder, int position) {
        holder.setData(list.get(position));

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

}


