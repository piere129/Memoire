package com.example.pieter.memoire.Models;

public class Card {

    private int image;
    private String title;
    private String description;

    public Card(int image,String title)
    {
        this.image = image;
        this.title = title;
    }

    public Card(int image,String title, String description)
    {
        this.image = image;
        this.title = title;
        this.description = description;
    }

    public int getImage(){
        return this.image;
    }

    public void setImage(int image) {
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
