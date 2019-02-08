package com.ictpoker.ixi.Commons;

import com.ictpoker.ixi.eval.Constants;

public class Card {

    private final Rank rank;
    private final Suit suit;

    public Card(final Rank rank, final Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getIndex() {
        return suit.ordinal() * Constants.NUM_RANKS + rank.ordinal();
    }

    public long getBitmask() {
        return 0x1L << getIndex();
    }

    @Override
    public String toString() {
        return String.format("%s%s", getRank(), getSuit());
    }
}
