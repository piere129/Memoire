package com.example.pieter.memoire.Persistence;

import com.example.pieter.memoire.Models.Card;

import java.util.List;

import io.reactivex.Flowable;

public class CardRepository implements ICardDataSource {

    private ICardDataSource dataSource;

    private static CardRepository instance;

    public CardRepository(ICardDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static CardRepository getInstance(ICardDataSource dataSource)
    {
        if (instance == null)
        {
            instance = new CardRepository(dataSource);
        }
        return instance;
    }

    @Override
    public void addCard(Card card) {
        dataSource.addCard(card);
    }

    @Override
    public void modifyCard(Card card) {
        dataSource.modifyCard(card);
    }

    @Override
    public void deleteCard(Card card) {
        dataSource.deleteCard(card);
    }

    @Override
    public Flowable<List<Card>> getCardsForTheme(int themeId) {
        return dataSource.getCardsForTheme(themeId);
    }
}
