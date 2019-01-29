package com.ictpoker.ixi.Commons;

import lombok.Data;

@Data
public class Card {

    private final Rank rank;
    private final Suit suit;

    @Override
    public String toString() {
        return String.format("%s%s", getRank(), getSuit());
    }
}
