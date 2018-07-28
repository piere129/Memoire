package com.example.pieter.memoire.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.pieter.memoire.R;

import java.util.ArrayList;
import java.util.List;


public class Theme implements Parcelable {

    private String name;
    private List<Card> cards;

    public Theme() {
        cards = new ArrayList<Card>();
    }

    public Theme(String name) {
        this.name = name;
        cards = new ArrayList<Card>();
    }


    public Theme(String name, List<Card> cards) {
        this.name = name;
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addCardToList(Card card) {
        this.cards.add(card);
    }

    public void deleteCardFromList(int position) {this.cards.remove(position);}

    protected Theme(Parcel in) {
        name = in.readString();
        if (in.readByte() == 0x01) {
            cards = new ArrayList<Card>();
            in.readList(cards, Card.class.getClassLoader());
        } else {
            cards = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        if (cards == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(cards);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Theme> CREATOR = new Parcelable.Creator<Theme>() {
        @Override
        public Theme createFromParcel(Parcel in) {
            return new Theme(in);
        }

        @Override
        public Theme[] newArray(int size) {
            return new Theme[size];
        }
    };
}