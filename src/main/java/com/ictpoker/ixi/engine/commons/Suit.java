package com.ictpoker.ixi.engine.commons;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public enum Suit {

    HEARTS, DIAMONDS, CLUBS, SPADES;

    private static final Map<Suit, String> SUIT_NOTATION = ImmutableMap.<Suit, String>builder()
            .put(HEARTS, "h")
            .put(DIAMONDS, "d")
            .put(CLUBS, "c")
            .put(SPADES, "s")
            .build();

    @Override
    public String toString() {
        return SUIT_NOTATION.get(this);
    }
}
