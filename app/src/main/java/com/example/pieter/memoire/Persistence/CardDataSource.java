package com.example.pieter.memoire.Persistence;

import com.example.pieter.memoire.Models.Card;

import java.util.List;

import io.reactivex.Flowable;

public class CardDataSource implements ICardDataSource {

    private CardDao cardDao;
    private static CardDataSource instance;

    public CardDataSource(CardDao cardDao)
    {
        this.cardDao = cardDao;
    }

    public static CardDataSource getInstance(CardDao cardDao) {
        if (instance == null)
        {
            instance = new CardDataSource(cardDao);
        }

        return instance;
    }


    @Override
    public void addCard(Card card) {
        cardDao.addCard(card);
    }

    @Override
    public void modifyCard(Card card) {
        cardDao.modifyCard(card);
    }

    @Override
    public void deleteCard(Card card) {
        cardDao.deleteCard(card);

    }

    @Override
    public Flowable<List<Card>> getCardsForTheme(int themeId) {
        return cardDao.getCardsForTheme(themeId);
    }
}
