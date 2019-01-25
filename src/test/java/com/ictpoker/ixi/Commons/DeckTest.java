package com.ictpoker.ixi.Commons;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Commons.Rank;
import com.ictpoker.ixi.Commons.Suit;
import com.ictpoker.ixi.Commons.Deck;
import org.junit.Assert;
import org.junit.Test;

import java.util.EmptyStackException;

public class DeckTest {

    @Test
    public void testDeck() {

        try {
            Assert.assertEquals(new Deck().size(), 52);
        } catch (Deck.DuplicateCardException e) {
            Assert.fail("All cards of the deck should be unique");
        }
    }

    @Test
    public void testEmptyDeck() {

        final Deck deck;
        try {
            deck = new Deck();

            for (int i=0; i<Deck.DECK_SIZE; i++) {
                deck.draw();
            }

            try {
                deck.draw();
                Assert.fail("An empty stack should fail to pop!");
            } catch (EmptyStackException e) {
                // Intended exception thrown, an empty stack should throw an error on pop()
            }
        } catch (Deck.DuplicateCardException e) {
            Assert.fail("All cards of the deck should be unique");
        }
    }

    public void testDuplicateCard() {

        final Deck deck;
        try {
            deck = new Deck();
            deck.add(new Card(Rank.TWO, Suit.HEARTS));
            Assert.fail("Should not be able to add the card Two of Hearts again since it's already present");
        } catch (Deck.DuplicateCardException e) {
            // Intended exception thrown, the Two of Hearts can't be added twice to the deck.
        }
    }
}
