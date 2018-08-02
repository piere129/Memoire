package com.example.pieter.memoire.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.pieter.memoire.R;

import java.text.DateFormat;
import java.util.Date;

@Entity(tableName = "cards")
public class Card implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name="theme_id")
    private int themeId;

    @ColumnInfo(name = "card_date")
    private String date;

    @ColumnInfo(name = "card_uri")
    private String uri;

    @ColumnInfo(name = "card_title")
    private String title;

    @ColumnInfo(name = "card_description")
    private String description;

    @ColumnInfo(name = "card_hasVideo")
    private boolean hasVideo;

    public Card(){}

    @Ignore
    public Card(String uri, String title, String description, boolean hasVideo) {
        this.uri = uri;
        this.title = title;
        this.description = description;
        this.hasVideo = hasVideo;
        this.date = DateFormat.getDateTimeInstance().format(new Date());
    }

    @Ignore
    public Card(String uri, String title, String description) {
        this.uri = uri;
        this.title = title;
        this.description = description;
        this.date = DateFormat.getDateTimeInstance().format(new Date());

    }

    @Ignore
    public Card(String title, String description) {
        this.title = title;
        this.description = description;
        this.uri = Uri.parse("android.resource://com.example.pieter.memoire/drawable/default_image_card").toString();
        this.date = DateFormat.getDateTimeInstance().format(new Date());
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    protected Card(Parcel in) {
        id = in.readInt();
        date = in.readString();
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
        dest.writeInt(id);
        dest.writeString(date);
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