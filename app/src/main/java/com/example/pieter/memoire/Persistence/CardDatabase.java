package com.example.pieter.memoire.Persistence;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.pieter.memoire.Models.Card;
import com.example.pieter.memoire.Models.Theme;

@android.arch.persistence.room.Database(entities = {Card.class, Theme.class}, version = 1, exportSchema = false)
public abstract class CardDatabase extends RoomDatabase{

    public static final String DATABASE_NAME = "CardDatabase";

    public abstract CardDao cardDao();

    private static CardDatabase instance;

    public static CardDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context, CardDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
