package com.ictpoker.ixi.Commons;

import java.util.HashMap;
import java.util.Map;

public enum Suit {

    HEARTS, DIAMONDS, CLUBS, SPADES;

    private static final Map<Suit, String> SUIT_NOTATION = new HashMap<>();

    static {
        SUIT_NOTATION.put(HEARTS, "h");
        SUIT_NOTATION.put(DIAMONDS, "d");
        SUIT_NOTATION.put(CLUBS, "c");
        SUIT_NOTATION.put(SPADES, "s");
    }

    @Override
    public String toString() {
        return SUIT_NOTATION.get(this);
    }
}
