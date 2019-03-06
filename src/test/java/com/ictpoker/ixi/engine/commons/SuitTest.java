package com.ictpoker.ixi.engine.commons;

import org.junit.Assert;
import org.junit.Test;

public class SuitTest {

    @Test
    public void testSuit() {
        Assert.assertEquals("h", Suit.HEARTS.toString());
        Assert.assertEquals("d", Suit.DIAMONDS.toString());
        Assert.assertEquals("c", Suit.CLUBS.toString());
        Assert.assertEquals("s", Suit.SPADES.toString());
    }
}
