package com.example.pieter.memoire.ClickListeners;

import android.view.View;

/**
 * Default ClickListener Interface for RecyclerView
 */
public interface ClickListener {
    void onClick(View v, int position);

    void onLongClick(View v, int position);


}
