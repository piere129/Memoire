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

    /**
     * Adds a card to the database
     *
     * @param card
     */
    @Insert
    public void addCard(Card card);

    /**
     * Updates a card from the database
     *
     * @param card
     */
    @Update
    public void modifyCard(Card card);

    /**
     * Deletes a card from the database
     *
     * @param card
     */
    @Delete
    public void deleteCard(Card card);

    /**
     * Gets every Card that belongs the a certain Theme Object
     *
     * @param themeId
     * @return
     */
    @Query("Select * from Card where theme_id=:themeId")
    public List<Card> getCardsFromTheme(int themeId);

    /**
     * Gets every Card that is added to the TimelineFragment
     *
     * @return
     */
    @Query("Select * from Card where in_timeline=1")
    public List<Card> getCardsInGallery();

    /**
     * Gets all Cards
     *
     * @return
     */
    @Query("SELECT * FROM Card")
    List<Card> getAllCards();
}
