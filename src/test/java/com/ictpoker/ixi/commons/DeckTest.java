package com.ictpoker.ixi.commons;

import org.junit.Assert;
import org.junit.Test;

import java.util.EmptyStackException;
import java.util.NoSuchElementException;

public class DeckTest {

    @Test
    public void testDeck() {
        Assert.assertEquals(new Deck().size(), 52);
    }

    @Test
    public void testEmptyDeck() {
        final Deck deck = new Deck();

        for (int i=0; i<Rank.values().length*Suit.values().length; i++) {
            deck.draw();
        }

        try {
            deck.draw();
            Assert.fail("An empty stack should fail to pop!");
        } catch (NoSuchElementException e) {
            // Intended exception thrown, an empty stack should throw an error on pop()
        }
    }
}
