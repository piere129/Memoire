package com.example.pieter.memoire.Persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.pieter.memoire.Models.Card;

import java.util.List;

@Dao
public interface CardDao {

    @Insert
    public void addCard(Card card);

    @Update
    public void modifyCard(Card card);

    @Delete
    public void deleteCard(Card card);

    @Query("select * from cards")
    public List<Card> getCards();
}
