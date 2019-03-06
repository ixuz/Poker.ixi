package com.ictpoker.ixi.engine.commons;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class EvaluationTest {

    @Test
    public void testEvaluation() {
        final Hand hand1 = new Hand(Arrays.asList(new Card(Rank.TWO, Suit.CLUBS),
                new Card(Rank.THREE, Suit.CLUBS),
                new Card(Rank.JACK, Suit.DIAMONDS),
                new Card(Rank.SIX, Suit.DIAMONDS),
                new Card(Rank.JACK, Suit.HEARTS),
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.ACE, Suit.SPADES)));

        final Hand hand2 = new Hand(Arrays.asList(new Card(Rank.TWO, Suit.CLUBS),
                new Card(Rank.THREE, Suit.CLUBS),
                new Card(Rank.JACK, Suit.CLUBS),
                new Card(Rank.SIX, Suit.DIAMONDS),
                new Card(Rank.JACK, Suit.HEARTS),
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.ACE, Suit.SPADES)));

        final Evaluation evaluation1 = new Evaluation(hand1);
        final Evaluation evaluation2 = new Evaluation(hand2);

        Assert.assertEquals(evaluation1, evaluation2);
        Assert.assertEquals(evaluation1.hashCode(), evaluation2.hashCode());
    }

    @Test(expected = IllegalStateException.class)
    public void testEvaluation2() {
        final Hand hand1 = new Hand(Arrays.asList(new Card(Rank.TWO, Suit.CLUBS),
                new Card(Rank.THREE, Suit.CLUBS),
                new Card(Rank.JACK, Suit.DIAMONDS),
                new Card(Rank.SIX, Suit.DIAMONDS),
                new Card(Rank.JACK, Suit.HEARTS),
                new Card(Rank.KING, Suit.HEARTS)));

        final Hand hand2 = new Hand(Arrays.asList(new Card(Rank.TWO, Suit.CLUBS),
                new Card(Rank.THREE, Suit.CLUBS),
                new Card(Rank.JACK, Suit.CLUBS),
                new Card(Rank.SIX, Suit.DIAMONDS),
                new Card(Rank.JACK, Suit.HEARTS),
                new Card(Rank.KING, Suit.HEARTS)));

        final Evaluation evaluation1 = new Evaluation(hand1);
        final Evaluation evaluation2 = new Evaluation(hand2);

        Assert.assertEquals(evaluation1, evaluation2);
        Assert.assertEquals(evaluation1.hashCode(), evaluation2.hashCode());
    }
}
