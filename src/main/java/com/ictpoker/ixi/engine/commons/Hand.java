package com.ictpoker.ixi.engine.commons;

import java.util.List;

public final class Hand {

    private long bitmask = 0x0L;
    private final List<Card> cards;

    public Hand(List<Card> cards) {
        for (Card card : cards) {
            bitmask |= card.getBitmask();
        }

        this.cards = cards;
    }

    public long getBitmask() {
        return bitmask;
    }

    public void reset() {
        bitmask = 0x0L;
        cards.clear();
    }

    public int size() {
        return cards.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card.toString());
        }
        return sb.toString();
    }
}