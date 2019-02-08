package com.ictpoker.ixi.eval;

import java.util.ArrayList;
import com.ictpoker.ixi.Commons.*;

public final class Hand {
    private long bitmask = 0x0L;
    private ArrayList<Card> cards = new ArrayList<Card>();

    public Hand(ArrayList<Card> cards) throws Exception {
        if (cards.size() != Constants.SIZE_HAND)
            throw new Exception("A hand for evaluation must consist of exactly 7 cards.");

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