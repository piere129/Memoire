package com.example.pieter.memoire.Models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.pieter.memoire.R;

public class Card implements Parcelable {

    private String uri;
    private String title;
    private String description;
    private boolean hasVideo;

    public Card(String uri, String title, String description, boolean hasVideo) {
        this.uri = uri;
        this.title = title;
        this.description = description;
        this.hasVideo = hasVideo;
    }

    public Card(String uri, String title, String description) {
        this.uri = uri;
        this.title = title;
        this.description = description;
    }

    public Card(String title, String description) {
        this.title = title;
        this.description = description;
        this.uri = Uri.parse("android.resource://com.example.pieter.memoire/drawable/default_image_card").toString();
    }

    public boolean getHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String image) {
        this.uri = uri;
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
        uri = in.readString();
        title = in.readString();
        description = in.readString();
        hasVideo = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uri);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeByte((byte) (hasVideo ? 0x01 : 0x00));
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