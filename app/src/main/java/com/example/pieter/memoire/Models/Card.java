package com.example.pieter.memoire.Models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.pieter.memoire.R;

public class Card implements Parcelable {

    private String imageUri;
    private String title;
    private String description;

    public Card(String imageUri, String title, String description) {
        this.imageUri = imageUri;
        this.title = title;
        this.description = description;
    }

    public Card(String title, String description) {
        this.title = title;
        this.description = description;
        this.imageUri = Uri.parse("android.resource://com.example.pieter.memoire/drawable/default_image_card").toString();
        ;
    }

    public String getImageUri() {
        return this.imageUri;
    }

    public void setImageUri(String image) {
        this.imageUri = imageUri;
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
        imageUri = in.readString();
        title = in.readString();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUri);
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