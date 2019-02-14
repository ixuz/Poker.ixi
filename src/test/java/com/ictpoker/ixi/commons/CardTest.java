package com.ictpoker.ixi.commons;

import org.junit.Assert;
import org.junit.Test;

public class CardTest {

    @Test
    public void testCard() {
        Assert.assertEquals(new Card(Rank.TWO, Suit.HEARTS).toString(), "2h");
        Assert.assertEquals(new Card(Rank.THREE, Suit.HEARTS).toString(), "3h");
        Assert.assertEquals(new Card(Rank.FOUR, Suit.HEARTS).toString(), "4h");
        Assert.assertEquals(new Card(Rank.FIVE, Suit.HEARTS).toString(), "5h");
        Assert.assertEquals(new Card(Rank.SIX, Suit.HEARTS).toString(), "6h");
        Assert.assertEquals(new Card(Rank.SEVEN, Suit.HEARTS).toString(), "7h");
        Assert.assertEquals(new Card(Rank.EIGHT, Suit.HEARTS).toString(), "8h");
        Assert.assertEquals(new Card(Rank.NINE, Suit.HEARTS).toString(), "9h");
        Assert.assertEquals(new Card(Rank.TEN, Suit.HEARTS).toString(), "Th");
        Assert.assertEquals(new Card(Rank.JACK, Suit.HEARTS).toString(), "Jh");
        Assert.assertEquals(new Card(Rank.QUEEN, Suit.HEARTS).toString(), "Qh");
        Assert.assertEquals(new Card(Rank.KING, Suit.HEARTS).toString(), "Kh");
        Assert.assertEquals(new Card(Rank.ACE, Suit.HEARTS).toString(), "Ah");
        Assert.assertEquals(new Card(Rank.TWO, Suit.DIAMONDS).toString(), "2d");
        Assert.assertEquals(new Card(Rank.THREE, Suit.DIAMONDS).toString(), "3d");
        Assert.assertEquals(new Card(Rank.FOUR, Suit.DIAMONDS).toString(), "4d");
        Assert.assertEquals(new Card(Rank.FIVE, Suit.DIAMONDS).toString(), "5d");
        Assert.assertEquals(new Card(Rank.SIX, Suit.DIAMONDS).toString(), "6d");
        Assert.assertEquals(new Card(Rank.SEVEN, Suit.DIAMONDS).toString(), "7d");
        Assert.assertEquals(new Card(Rank.EIGHT, Suit.DIAMONDS).toString(), "8d");
        Assert.assertEquals(new Card(Rank.NINE, Suit.DIAMONDS).toString(), "9d");
        Assert.assertEquals(new Card(Rank.TEN, Suit.DIAMONDS).toString(), "Td");
        Assert.assertEquals(new Card(Rank.JACK, Suit.DIAMONDS).toString(), "Jd");
        Assert.assertEquals(new Card(Rank.QUEEN, Suit.DIAMONDS).toString(), "Qd");
        Assert.assertEquals(new Card(Rank.KING, Suit.DIAMONDS).toString(), "Kd");
        Assert.assertEquals(new Card(Rank.ACE, Suit.DIAMONDS).toString(), "Ad");
        Assert.assertEquals(new Card(Rank.TWO, Suit.CLUBS).toString(), "2c");
        Assert.assertEquals(new Card(Rank.THREE, Suit.CLUBS).toString(), "3c");
        Assert.assertEquals(new Card(Rank.FOUR, Suit.CLUBS).toString(), "4c");
        Assert.assertEquals(new Card(Rank.FIVE, Suit.CLUBS).toString(), "5c");
        Assert.assertEquals(new Card(Rank.SIX, Suit.CLUBS).toString(), "6c");
        Assert.assertEquals(new Card(Rank.SEVEN, Suit.CLUBS).toString(), "7c");
        Assert.assertEquals(new Card(Rank.EIGHT, Suit.CLUBS).toString(), "8c");
        Assert.assertEquals(new Card(Rank.NINE, Suit.CLUBS).toString(), "9c");
        Assert.assertEquals(new Card(Rank.TEN, Suit.CLUBS).toString(), "Tc");
        Assert.assertEquals(new Card(Rank.JACK, Suit.CLUBS).toString(), "Jc");
        Assert.assertEquals(new Card(Rank.QUEEN, Suit.CLUBS).toString(), "Qc");
        Assert.assertEquals(new Card(Rank.KING, Suit.CLUBS).toString(), "Kc");
        Assert.assertEquals(new Card(Rank.ACE, Suit.CLUBS).toString(), "Ac");
        Assert.assertEquals(new Card(Rank.TWO, Suit.SPADES).toString(), "2s");
        Assert.assertEquals(new Card(Rank.THREE, Suit.SPADES).toString(), "3s");
        Assert.assertEquals(new Card(Rank.FOUR, Suit.SPADES).toString(), "4s");
        Assert.assertEquals(new Card(Rank.FIVE, Suit.SPADES).toString(), "5s");
        Assert.assertEquals(new Card(Rank.SIX, Suit.SPADES).toString(), "6s");
        Assert.assertEquals(new Card(Rank.SEVEN, Suit.SPADES).toString(), "7s");
        Assert.assertEquals(new Card(Rank.EIGHT, Suit.SPADES).toString(), "8s");
        Assert.assertEquals(new Card(Rank.NINE, Suit.SPADES).toString(), "9s");
        Assert.assertEquals(new Card(Rank.TEN, Suit.SPADES).toString(), "Ts");
        Assert.assertEquals(new Card(Rank.JACK, Suit.SPADES).toString(), "Js");
        Assert.assertEquals(new Card(Rank.QUEEN, Suit.SPADES).toString(), "Qs");
        Assert.assertEquals(new Card(Rank.KING, Suit.SPADES).toString(), "Ks");
        Assert.assertEquals(new Card(Rank.ACE, Suit.SPADES).toString(), "As");
    }

    @Test
    public void testIndex() {
        Assert.assertEquals(13, new Card(Rank.TWO, Suit.DIAMONDS).getIndex());
        Assert.assertEquals(47, new Card(Rank.TEN, Suit.SPADES).getIndex());
    }

    @Test
    public void testGetBitmask() {
        Assert.assertEquals(8192L, new Card(Rank.TWO, Suit.DIAMONDS).getBitmask()); // 2^13
        Assert.assertEquals(140737488355328L, new Card(Rank.TEN, Suit.SPADES).getBitmask()); // 2^47
    }
}
