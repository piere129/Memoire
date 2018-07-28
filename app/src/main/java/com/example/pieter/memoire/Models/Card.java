package com.example.pieter.memoire.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Card implements Parcelable {

    private int image;
    private String title;
    private String description;

    public Card(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public Card(int image, String title, String description) {
        this.image = image;
        this.title = title;
        this.description = description;
    }

    public Card( String title, String description) {
        this.title = title;
        this.description = description;
    }

    public int getImage() {
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

    protected Card(Parcel in) {
        image = in.readInt();
        title = in.readString();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(image);
        dest.writeString(title);
        dest.writeString(description);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };
}
