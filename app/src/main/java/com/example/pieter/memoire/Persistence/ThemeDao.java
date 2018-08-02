package com.example.pieter.memoire.Persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.pieter.memoire.Models.Theme;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ThemeDao {

    @Insert
    public void addTheme(Theme theme);

    @Update
    public void modifyTheme(Theme theme);

    @Delete
    public void deleteTheme(Theme theme);

    @Query("select * from themes")
    Flowable<List<Theme>> getThemes();

}
