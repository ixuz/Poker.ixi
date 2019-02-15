package com.ictpoker.ixi.commons;

import org.junit.Assert;
import org.junit.Test;

public class RankTest {

    @Test
    public void testRank() {
        Assert.assertEquals("2", Rank.TWO.toString());
        Assert.assertEquals("3", Rank.THREE.toString());
        Assert.assertEquals("4", Rank.FOUR.toString());
        Assert.assertEquals("5", Rank.FIVE.toString());
        Assert.assertEquals("6", Rank.SIX.toString());
        Assert.assertEquals("7", Rank.SEVEN.toString());
        Assert.assertEquals("8", Rank.EIGHT.toString());
        Assert.assertEquals("9", Rank.NINE.toString());
        Assert.assertEquals("T", Rank.TEN.toString());
        Assert.assertEquals("J", Rank.JACK.toString());
        Assert.assertEquals("Q", Rank.QUEEN.toString());
        Assert.assertEquals("K", Rank.KING.toString());
        Assert.assertEquals("A", Rank.ACE.toString());
    }
}
