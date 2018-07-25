package com.example.pieter.memoire.Models;

import android.widget.ImageView;

import java.util.List;

public class Theme {

    private String name;
    private int image;
    private List<Card> cards;

    public Theme(){};

    public Theme(String name, int image) {

        this.name = name;
        this.image = image;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
