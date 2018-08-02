package com.example.pieter.memoire.Persistence;

import com.example.pieter.memoire.Models.Theme;

import java.util.List;

import io.reactivex.Flowable;

public class ThemeRepository implements IThemeDataSource {

    private IThemeDataSource dataSource;

    private static ThemeRepository instance;

    public ThemeRepository(IThemeDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static ThemeRepository getInstance(IThemeDataSource dataSource)
    {
        if (instance == null)
        {
            instance = new ThemeRepository(dataSource);
        }
        return instance;
    }

    @Override
    public void addTheme(Theme theme) {
         dataSource.addTheme(theme);
    }

    @Override
    public void modifyTheme(Theme theme) {
        dataSource.modifyTheme(theme);
    }

    @Override
    public void deleteTheme(Theme theme) {
        dataSource.deleteTheme(theme);
    }

    @Override
    public Flowable<List<Theme>> getThemes() {
        return dataSource.getThemes();
    }
}
