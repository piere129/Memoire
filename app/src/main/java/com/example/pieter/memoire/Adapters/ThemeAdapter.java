package com.example.pieter.memoire.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pieter.memoire.Activities.ThemeActivity;
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
    public void onBindViewHolder(ThemesViewHolder holder, final int position) {
        holder.setData(list.get(position));
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"test" + position,Toast.LENGTH_LONG).show();
            }
        });

        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ThemeActivity.class);
                Theme t = list.get(position);
                intent.putExtra("theme", t);
                intent.putExtra("position", position);
                ((Activity) context).startActivityForResult(intent,1);

            }
        });

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

}


