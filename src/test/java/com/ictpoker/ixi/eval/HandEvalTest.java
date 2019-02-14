package com.ictpoker.ixi.eval;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.ArrayList;
import com.ictpoker.ixi.commons.*;

public class HandEvalTest {

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.println("\nStarting test: " + description.getMethodName());
        }
    };

    @Test
    public void testGetBitmask() throws Exception {

        final ArrayList<Card> cards = new ArrayList<Card>();

        cards.add(new Card(Rank.TEN, Suit.HEARTS));
        cards.add(new Card(Rank.KING, Suit.HEARTS));
        cards.add(new Card(Rank.NINE, Suit.DIAMONDS));
        cards.add(new Card(Rank.QUEEN, Suit.CLUBS));
        cards.add(new Card(Rank.ACE, Suit.CLUBS));
        cards.add(new Card(Rank.TWO, Suit.SPADES));
        cards.add(new Card(Rank.JACK, Suit.SPADES));

        final Hand hand = new Hand(cards);
        // reminder: this is how suits are ordered from a binary perspective: s|c|d|h
        Assert.assertEquals(282368330959104L, hand.getBitmask()); // 1000000001101000000000000000100000000100100000000
    }

    @Test
    public void testSizeAndReset() throws Exception {

        final ArrayList<Card> cards = new ArrayList<Card>();

        cards.add(new Card(Rank.TEN, Suit.HEARTS));
        cards.add(new Card(Rank.KING, Suit.HEARTS));
        cards.add(new Card(Rank.NINE, Suit.DIAMONDS));
        cards.add(new Card(Rank.QUEEN, Suit.CLUBS));
        cards.add(new Card(Rank.ACE, Suit.CLUBS));
        cards.add(new Card(Rank.TWO, Suit.SPADES));
        cards.add(new Card(Rank.JACK, Suit.SPADES));

        final Hand hand = new Hand(cards);

        Assert.assertEquals(7, hand.size());

        hand.reset();

        Assert.assertEquals(0, hand.size());
        Assert.assertEquals(0L, hand.getBitmask());

    }
}
