package com.example.pieter.memoire.Persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.pieter.memoire.Models.Card;
import com.example.pieter.memoire.Models.Theme;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CardDao {

    @Insert
    public void addCard(Card card);

    @Update
    public void modifyCard(Card card);

    @Delete
    public void deleteCard(Card card);

    @Query("Select * from Card where theme_id=:themeId")
    public List<Card> getCardsFromTheme(int themeId);

    @Query("Select * from Card where in_gallery=1")
    public List<Card> getCardsInGallery();

    @Query("SELECT * FROM Card")
    List<Card> getAllCards();
}
