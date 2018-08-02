package com.example.pieter.memoire.Persistence;

import com.example.pieter.memoire.Models.Theme;

import java.util.List;

import io.reactivex.Flowable;

public class ThemeDataSource implements IThemeDataSource {

    private ThemeDao themeDao;
    private static ThemeDataSource instance;

    public ThemeDataSource(ThemeDao themeDao)
    {
        this.themeDao = themeDao;
    }

    public static ThemeDataSource getInstance(ThemeDao themeDao) {
        if (instance == null)
        {
            instance = new ThemeDataSource(themeDao);
        }

        return instance;
    }

    @Override
        public void addTheme(Theme theme) {
         themeDao.addTheme(theme);
    }

    @Override
    public void modifyTheme(Theme theme) {
        themeDao.modifyTheme(theme);
    }

    @Override
    public void deleteTheme(Theme theme) {
        themeDao.deleteTheme(theme);
    }

    @Override
    public Flowable<List<Theme>> getThemes() {
        return themeDao.getThemes();
    }
}
