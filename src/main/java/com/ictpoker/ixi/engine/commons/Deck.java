package com.ictpoker.ixi.engine.commons;

import com.google.common.collect.Lists;

import java.util.*;

public class Deck {

    private final Deque<Card> cards = new LinkedList<>();

    public Deck() {
        List<Card> temp = new LinkedList<>();
        for (final Rank rank : Rank.values()) {
            for (final Suit suit : Suit.values()) {
                temp.add(new Card(rank, suit));
            }
        }
        Collections.shuffle(temp);
        cards.addAll(temp);
    }

    public Deck(final List<Card> fixedCardOrder) {
        cards.addAll(Lists.reverse(fixedCardOrder));
    }

    public Card draw() {
        return cards.removeLast();
    }

    public int size() {
        return cards.size();
    }
}
