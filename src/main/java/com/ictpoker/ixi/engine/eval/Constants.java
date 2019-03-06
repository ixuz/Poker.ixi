package com.ictpoker.ixi.engine.eval;

public class Constants {

    // general
    public static final int NUM_RANKS = 13;
    public static final int SIZE_HAND = 7;

    // bitmask related
    public static final int MASK_SUIT = 0x1FFF;
    public static final int OFFSET_CLUBS = 0;
    public static final int OFFSET_DIAMONDS = 13;
    public static final int OFFSET_HEARTS = 26;
    public static final int OFFSET_SPADES = 39;
    public static final int OFFSET_TYPE = 24;
    public static final int OFFSET_MAJOR = 20;
    public static final int OFFSET_MINOR = 16;
    public static final int OFFSET_KICKER = 0;
    public static final int NULL = 0xF;

    // hand types
    public static final int HIGHCARD = 0;
    public static final int PAIR = 1;
    public static final int TWO_PAIR = 2;
    public static final int TRIPS = 3;
    public static final int STRAIGHT = 4;
    public static final int FLUSH = 5;
    public static final int FULLHOUSE = 6;
    public static final int QUADS = 7;
    public static final int STRAIGHT_FLUSH = 8;
    public static final int ROYAL_FLUSH = 9;

    // Holds all the possible hand types.
    private static final String[] TYPES = { "Highcard", "Pair", "Two-Pair", "Three-of-a-kind", "Straight", "Flush",
            "Full-house", "Four-of-a-kind", "Straight-flush", "Royal-flush" };

    // Holds the corresponding bitmasks for all ranks (TWO=0x0001, ..., ACE=0x1000)
    private static final int[] BITMASK_RANK = { 0x0001, 0x0002, 0x0004, 0x0008, 0x0010, 0x0020, 0x0040, 0x0080,
            0x0100, 0x0200, 0x0400, 0x0800, 0x1000 };

    private Constants() {

    }

    public static String types(int index) {
        return TYPES[index];
    }

    public static int bitmaskRank(int index) {
        return BITMASK_RANK[index];
    }
}