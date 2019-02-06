package com.ictpoker.ixi.Commons;

import org.junit.Assert;
import org.junit.Test;

public class SuitTest {

    @Test
    public void testSuit() {
        Assert.assertEquals(Suit.HEARTS.toString(), "h");
        Assert.assertEquals(Suit.DIAMONDS.toString(), "d");
        Assert.assertEquals(Suit.CLUBS.toString(), "c");
        Assert.assertEquals(Suit.SPADES.toString(), "s");
    }
}
