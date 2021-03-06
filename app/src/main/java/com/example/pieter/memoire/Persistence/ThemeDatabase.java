package com.example.pieter.memoire.Persistence;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.pieter.memoire.Models.Card;
import com.example.pieter.memoire.Models.Theme;

@android.arch.persistence.room.Database(entities = {Theme.class, Card.class}, version = 1, exportSchema = false)
public abstract class ThemeDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "ThemeDatabase";

    public abstract ThemeDao getThemeDao();

    public abstract CardDao getCardDao();

    private static ThemeDatabase instance;

    /**
     * Returns a Singleton Object of this class.
     * The purpose of this class is to provide the DAO's for
     * data manipulation in the database.
     *
     * @param context
     * @return
     */
    public static ThemeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, ThemeDatabase.class, DATABASE_NAME).allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
