package com.ictpoker.ixi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Stack;

public class Deck extends Stack<Card> {

    public final static int DECK_SIZE = 52;
    private final static Logger LOGGER = LogManager.getLogger(Deck.class);

    public Deck() {
        for (Rank rank : Rank.values()) {
            for (Suit suit : Suit.values()) {
                push(new Card(rank, suit));
            }
        }

        shuffle();
    }

    public void shuffle() {

        Collections.shuffle(this);
    }
}
