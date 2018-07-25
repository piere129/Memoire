package com.example.pieter.memoire.Models;

import android.widget.ImageView;

public class Theme {

    private String name;
    private String description;
    private ImageView image;

    public Theme(){};

    public Theme(String name, String description, ImageView image) {

        this.name = name;
        this.description = description;
        this.image = image;
    }

    public Theme(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }
}
