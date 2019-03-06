package com.ictpoker.ixi.engine.commons;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public enum Rank {

    TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;

    private static final Map<Rank, String> RANK_NOTATION = ImmutableMap.<Rank, String>builder()
            .put(TWO, "2")
            .put(THREE, "3")
            .put(FOUR, "4")
            .put(FIVE, "5")
            .put(SIX, "6")
            .put(SEVEN, "7")
            .put(EIGHT, "8")
            .put(NINE, "9")
            .put(TEN, "T")
            .put(JACK, "J")
            .put(QUEEN, "Q")
            .put(KING, "K")
            .put(ACE, "A")
            .build();

    @Override
    public String toString() {
        return RANK_NOTATION.get(this);
    }
}
