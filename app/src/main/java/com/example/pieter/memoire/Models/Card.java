package com.example.pieter.memoire.Models;

import android.widget.ImageView;

public class Card {

    private ImageView image;
    private String title;
    private String description;

    public Card(ImageView image, String title) {
        this.image = image;
        this.title = title;
    }

    public Card(ImageView image, String title, String description) {
        this.image = image;
        this.title = title;
        this.description = description;
    }

    public ImageView getImage() {
        return this.image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
