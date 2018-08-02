package com.example.pieter.memoire.Persistence;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.pieter.memoire.Models.Card;

import java.util.List;

import io.reactivex.Flowable;

public interface ICardDataSource {

    public void addCard(Card card);
    public void modifyCard(Card card);
    public void deleteCard(Card card);
    Flowable<List<Card>> getCardsForTheme(int themeId);
}
