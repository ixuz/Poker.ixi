package com.ictpoker.ixi.commons;

import org.junit.Assert;
import org.junit.Test;

public class CardTest {

    @Test
    public void testCard() {
        Assert.assertEquals("2h", new Card(Rank.TWO, Suit.HEARTS).toString());
        Assert.assertEquals("3h", new Card(Rank.THREE, Suit.HEARTS).toString());
        Assert.assertEquals("4h", new Card(Rank.FOUR, Suit.HEARTS).toString());
        Assert.assertEquals("5h", new Card(Rank.FIVE, Suit.HEARTS).toString());
        Assert.assertEquals("6h", new Card(Rank.SIX, Suit.HEARTS).toString());
        Assert.assertEquals("7h", new Card(Rank.SEVEN, Suit.HEARTS).toString());
        Assert.assertEquals("8h", new Card(Rank.EIGHT, Suit.HEARTS).toString());
        Assert.assertEquals("9h", new Card(Rank.NINE, Suit.HEARTS).toString());
        Assert.assertEquals("Th", new Card(Rank.TEN, Suit.HEARTS).toString());
        Assert.assertEquals("Jh", new Card(Rank.JACK, Suit.HEARTS).toString());
        Assert.assertEquals("Qh", new Card(Rank.QUEEN, Suit.HEARTS).toString());
        Assert.assertEquals("Kh", new Card(Rank.KING, Suit.HEARTS).toString());
        Assert.assertEquals("Ah", new Card(Rank.ACE, Suit.HEARTS).toString());
        Assert.assertEquals("2d", new Card(Rank.TWO, Suit.DIAMONDS).toString());
        Assert.assertEquals("3d", new Card(Rank.THREE, Suit.DIAMONDS).toString());
        Assert.assertEquals("4d", new Card(Rank.FOUR, Suit.DIAMONDS).toString());
        Assert.assertEquals("5d", new Card(Rank.FIVE, Suit.DIAMONDS).toString());
        Assert.assertEquals("6d", new Card(Rank.SIX, Suit.DIAMONDS).toString());
        Assert.assertEquals("7d", new Card(Rank.SEVEN, Suit.DIAMONDS).toString());
        Assert.assertEquals("8d", new Card(Rank.EIGHT, Suit.DIAMONDS).toString());
        Assert.assertEquals("9d", new Card(Rank.NINE, Suit.DIAMONDS).toString());
        Assert.assertEquals("Td", new Card(Rank.TEN, Suit.DIAMONDS).toString());
        Assert.assertEquals("Jd", new Card(Rank.JACK, Suit.DIAMONDS).toString());
        Assert.assertEquals("Qd", new Card(Rank.QUEEN, Suit.DIAMONDS).toString());
        Assert.assertEquals("Kd", new Card(Rank.KING, Suit.DIAMONDS).toString());
        Assert.assertEquals("Ad", new Card(Rank.ACE, Suit.DIAMONDS).toString());
        Assert.assertEquals("2c", new Card(Rank.TWO, Suit.CLUBS).toString());
        Assert.assertEquals("3c", new Card(Rank.THREE, Suit.CLUBS).toString());
        Assert.assertEquals("4c", new Card(Rank.FOUR, Suit.CLUBS).toString());
        Assert.assertEquals("5c", new Card(Rank.FIVE, Suit.CLUBS).toString());
        Assert.assertEquals("6c", new Card(Rank.SIX, Suit.CLUBS).toString());
        Assert.assertEquals("7c", new Card(Rank.SEVEN, Suit.CLUBS).toString());
        Assert.assertEquals("8c", new Card(Rank.EIGHT, Suit.CLUBS).toString());
        Assert.assertEquals("9c", new Card(Rank.NINE, Suit.CLUBS).toString());
        Assert.assertEquals("Tc", new Card(Rank.TEN, Suit.CLUBS).toString());
        Assert.assertEquals("Jc", new Card(Rank.JACK, Suit.CLUBS).toString());
        Assert.assertEquals("Qc", new Card(Rank.QUEEN, Suit.CLUBS).toString());
        Assert.assertEquals("Kc", new Card(Rank.KING, Suit.CLUBS).toString());
        Assert.assertEquals("Ac", new Card(Rank.ACE, Suit.CLUBS).toString());
        Assert.assertEquals("2s", new Card(Rank.TWO, Suit.SPADES).toString());
        Assert.assertEquals("3s", new Card(Rank.THREE, Suit.SPADES).toString());
        Assert.assertEquals("4s", new Card(Rank.FOUR, Suit.SPADES).toString());
        Assert.assertEquals("5s", new Card(Rank.FIVE, Suit.SPADES).toString());
        Assert.assertEquals("6s", new Card(Rank.SIX, Suit.SPADES).toString());
        Assert.assertEquals("7s", new Card(Rank.SEVEN, Suit.SPADES).toString());
        Assert.assertEquals("8s", new Card(Rank.EIGHT, Suit.SPADES).toString());
        Assert.assertEquals("9s", new Card(Rank.NINE, Suit.SPADES).toString());
        Assert.assertEquals("Ts", new Card(Rank.TEN, Suit.SPADES).toString());
        Assert.assertEquals("Js", new Card(Rank.JACK, Suit.SPADES).toString());
        Assert.assertEquals("Qs", new Card(Rank.QUEEN, Suit.SPADES).toString());
        Assert.assertEquals("Ks", new Card(Rank.KING, Suit.SPADES).toString());
        Assert.assertEquals("As", new Card(Rank.ACE, Suit.SPADES).toString());
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
