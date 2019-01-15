package com.ictpoker.ixi;

import java.util.HashMap;
import java.util.Map;

public enum Rank {

    TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;

    private static final Map<Rank, String> RANK_NOTATION = new HashMap<>();

    static {
        RANK_NOTATION.put(TWO, "2");
        RANK_NOTATION.put(THREE, "3");
        RANK_NOTATION.put(FOUR, "4");
        RANK_NOTATION.put(FIVE, "5");
        RANK_NOTATION.put(SIX, "6");
        RANK_NOTATION.put(SEVEN, "7");
        RANK_NOTATION.put(EIGHT, "8");
        RANK_NOTATION.put(NINE, "9");
        RANK_NOTATION.put(TEN, "T");
        RANK_NOTATION.put(JACK, "J");
        RANK_NOTATION.put(QUEEN, "Q");
        RANK_NOTATION.put(KING, "K");
        RANK_NOTATION.put(ACE, "A");
    }

    @Override
    public String toString() {
        return RANK_NOTATION.get(this);
    }
}
