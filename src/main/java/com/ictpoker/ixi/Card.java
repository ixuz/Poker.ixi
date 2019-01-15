package com.ictpoker.ixi;

import com.sun.istack.internal.NotNull;

public class Card {

    private final Suit suit;
    private final Rank rank;

    public Card(@NotNull final Rank rank, @NotNull final Suit suit) {

        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {

        return suit;
    }

    public Rank getRank() {

        return rank;
    }

    @Override
    public String toString() {

        return String.format("%s%s",getRank(), getSuit());
    }
}
