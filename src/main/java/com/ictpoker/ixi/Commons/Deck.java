package com.ictpoker.ixi.Commons;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Stack;

public class Deck {

    private final static Logger LOGGER = LogManager.getLogger(Deck.class);
    public final static int DECK_SIZE = 52;
    private final Stack<Card> deck = new Stack<>();

    public Deck() {
        for (final Rank rank : Rank.values()) {
            for (final Suit suit : Suit.values()) {
                deck.add(new Card(rank, suit));
            }
        }

        shuffle();
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
