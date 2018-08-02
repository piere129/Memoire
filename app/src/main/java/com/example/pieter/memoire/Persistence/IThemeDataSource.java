package com.example.pieter.memoire.Persistence;


import com.example.pieter.memoire.Models.Theme;

import java.util.List;

import io.reactivex.Flowable;

public interface IThemeDataSource {

    public void addTheme(Theme theme);
    public void modifyTheme(Theme theme);
    public void deleteTheme(Theme theme);
    Flowable<List<Theme>> getThemes();
}
