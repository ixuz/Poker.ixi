package com.ictpoker.ixi.Dealer;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Commons.Rank;
import com.ictpoker.ixi.Commons.Suit;
import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck {

    public final static int DECK_SIZE = 52;
    private final static Logger LOGGER = LogManager.getLogger(Deck.class);
    private final Stack<Card> deck = new Stack<>();

    public Deck() throws DuplicateCardException {
        for (final Rank rank : Rank.values()) {
            for (final Suit suit : Suit.values()) {
                add(new Card(rank, suit));
            }
        }

        shuffle();
    }

    public void shuffle() {

        Collections.shuffle(deck);
    }

    public boolean add(@NotNull final Card card)
            throws DuplicateCardException {

        for (final Card c : deck) {
            if (card.equals(c) || (card.getRank().equals(c.getRank()) && card.getSuit().equals(c.getSuit()))) {
                throw new DuplicateCardException();
            }
        }

        return deck.add(card);
    }

    public Card draw() {

        return deck.pop();
    }

    public int size() {

        return deck.size();
    }

    public boolean addAll(@NotNull final List<Card> cards) {

        return deck.addAll(cards);
    }

    public class DuplicateCardException extends Exception {}
}
