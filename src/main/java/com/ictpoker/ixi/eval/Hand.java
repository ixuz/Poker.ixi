package com.ictpoker.ixi.eval;

import java.util.ArrayList;
import java.util.List;

import com.ictpoker.ixi.commons.*;
import com.ictpoker.ixi.eval.exception.HandException;

public final class Hand {

    private long bitmask = 0x0L;
    private final List<Card> cards;

    public Hand(List<Card> cards) throws HandException {
        if (cards.size() != Constants.SIZE_HAND)
            throw new HandException("A hand for evaluation must consist of exactly 7 cards.");

        for (int i = 0; i < cards.size(); i++) {
            bitmask |= cards.get(i).getBitmask();
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