package com.example.pieter.memoire.Models;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class ThemeWithCards {

    @Embedded
    public Theme theme;

    @Relation(parentColumn = "id",entityColumn = "theme_id", entity = Card.class)
    public List<Card> cardList;
}
