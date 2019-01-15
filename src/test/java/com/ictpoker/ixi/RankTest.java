package com.ictpoker.ixi;

import org.junit.Assert;
import org.junit.Test;

public class RankTest {

    @Test
    public void testRank() {
        Assert.assertEquals(Rank.TWO.toString(), "2");
        Assert.assertEquals(Rank.THREE.toString(), "3");
        Assert.assertEquals(Rank.FOUR.toString(), "4");
        Assert.assertEquals(Rank.FIVE.toString(), "5");
        Assert.assertEquals(Rank.SIX.toString(), "6");
        Assert.assertEquals(Rank.SEVEN.toString(), "7");
        Assert.assertEquals(Rank.EIGHT.toString(), "8");
        Assert.assertEquals(Rank.NINE.toString(), "9");
        Assert.assertEquals(Rank.TEN.toString(), "T");
        Assert.assertEquals(Rank.JACK.toString(), "J");
        Assert.assertEquals(Rank.QUEEN.toString(), "Q");
        Assert.assertEquals(Rank.KING.toString(), "K");
        Assert.assertEquals(Rank.ACE.toString(), "A");
    }
}
