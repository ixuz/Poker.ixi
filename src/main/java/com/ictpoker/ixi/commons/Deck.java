package com.ictpoker.ixi.commons;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck {

    private final Stack<Card> deck = new Stack<>();

    public Deck() {
        for (final Rank rank : Rank.values()) {
            for (final Suit suit : Suit.values()) {
                deck.add(new Card(rank, suit));
            }
        }
        shuffle();
    }

    public Deck(final List<Card> fixedCardOrder) {
        deck.addAll(Lists.reverse(fixedCardOrder));
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public Card draw() {
        return deck.pop();
    }

    public int size() {
        return deck.size();
    }
}
