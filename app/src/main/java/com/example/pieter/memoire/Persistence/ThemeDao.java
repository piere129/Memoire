package com.example.pieter.memoire.Persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.pieter.memoire.Models.Theme;

import java.util.List;
@Dao
public interface ThemeDao {

    /**
     * Adds a theme to the database
     * @param theme
     */
    @Insert
    public void addTheme(Theme theme);

    /**
     * Updates a theme from the database
     * @param theme
     */
    @Update
    public void modifyTheme(Theme theme);

    /**
     * Deletes a theme from the database
     * @param theme
     */
    @Delete
    public void deleteTheme(Theme theme);

    /**

     * Gets all themes
     * @return
     */
    @Query("select * from Theme")
    List<Theme> getThemes();

}
