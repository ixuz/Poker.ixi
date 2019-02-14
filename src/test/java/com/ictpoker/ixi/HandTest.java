package com.ictpoker.ixi;

import com.ictpoker.ixi.commons.Card;
import com.ictpoker.ixi.commons.Deck;
import com.ictpoker.ixi.commons.Rank;
import com.ictpoker.ixi.commons.Suit;
import com.ictpoker.ixi.table.Table;
import com.ictpoker.ixi.table.exception.TableException;
import com.ictpoker.ixi.table.event.action.*;
import com.ictpoker.ixi.table.event.info.*;
import com.ictpoker.ixi.table.event.action.JoinEvent;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.Arrays;

public class HandTest {

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.println("\nStarting test: " + description.getMethodName());
        }
    };

    /**
     * Three player pot and all players see the flop without raising.
     * All players check on all streets leading up to the showdown.
     * Carry Davis and Eric Flores both have the strongest hand and splits the pot.
     */
    @Test
    public void testHand1()
            throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new SetSeatEvent("Adam Broker", 1000, 0));
        table.addEventLast(new SetSeatEvent("Carry Davis", 1000, 1));
        table.addEventLast(new SetSeatEvent("Eric Flores", 1000, 2));

        // Fixed deck for test
        table.addEventLast(new NewDeckEvent(new Deck(Arrays.asList(
                new Card(Rank.QUEEN, Suit.SPADES), // Carry Davis
                new Card(Rank.QUEEN, Suit.CLUBS), // Eric Flores
                new Card(Rank.FOUR, Suit.CLUBS), // Adam Broker
                new Card(Rank.NINE, Suit.DIAMONDS), // Carry Davis
                new Card(Rank.JACK, Suit.HEARTS), // Eric Flores
                new Card(Rank.FOUR, Suit.HEARTS), // Adam Broker
                new Card(Rank.SEVEN, Suit.HEARTS), // Flop
                new Card(Rank.TEN, Suit.HEARTS), // Flop
                new Card(Rank.KING, Suit.DIAMONDS), // Flop
                new Card(Rank.JACK, Suit.SPADES), // Turn
                new Card(Rank.ACE, Suit.HEARTS) // River
        ))));

        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Carry Davis"));
        table.addEventLast(new PostBigBlindEvent("Eric Flores"));
        table.addEventLast(new DealEvent());
        table.addEventLast(new CallEvent("Adam Broker"));
        table.addEventLast(new CallEvent("Carry Davis"));
        table.addEventLast(new CheckEvent("Eric Flores"));
        // Flop
        table.addEventLast(new CheckEvent("Carry Davis"));
        table.addEventLast(new CheckEvent("Eric Flores"));
        table.addEventLast(new CheckEvent("Adam Broker"));
        // Turn
        table.addEventLast(new CheckEvent("Carry Davis"));
        table.addEventLast(new CheckEvent("Eric Flores"));
        table.addEventLast(new CheckEvent("Adam Broker"));
        // River
        table.addEventLast(new CheckEvent("Carry Davis"));
        table.addEventLast(new CheckEvent("Eric Flores"));
        table.addEventLast(new CheckEvent("Adam Broker"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(990, table.getSeats().get(0).getStack());
        Assert.assertEquals(1005, table.getSeats().get(1).getStack());
        Assert.assertEquals(1005, table.getSeats().get(2).getStack());
    }

    /**
     * Three player pot and Adam Broker puts in a typical sized raise pre-flop.
     * Both other players fold their hands.
     * The pot is won by Adam Broker without showdown.
     */
    @Test
    public void testHand2()
            throws TableException {

        final Table table = new Table(500, 1000, 5, 10);

        table.addEventLast(new JoinEvent("Adam Broker", 1000, 0));
        table.addEventLast(new JoinEvent("Carry Davis", 1000, 1));
        table.addEventLast(new JoinEvent("Eric Flores", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Carry Davis"));
        table.addEventLast(new PostBigBlindEvent("Eric Flores"));
        table.addEventLast(new DealEvent());
        table.addEventLast(new RaiseEvent("Adam Broker", 40));
        table.addEventLast(new FoldEvent("Carry Davis"));
        table.addEventLast(new FoldEvent("Eric Flores"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(1015, table.getSeats().get(0).getStack());
        Assert.assertEquals(995, table.getSeats().get(1).getStack());
        Assert.assertEquals(990, table.getSeats().get(2).getStack());
    }

    /**
     * Three player pot and Adam Broker raises all-in immediately pre-flop.
     * Both remaining players calls all-in.
     * The pot is split 2-way since both callers have the best hand, Ace-high straight.
     * Adam Broker loses the hand with just pocket fours.
     */
    @Test
    public void testHand3()
            throws TableException {

        final Table table = new Table(500, 1000, 5, 10);

        table.addEventLast(new JoinEvent("Adam Broker", 1000, 0));
        table.addEventLast(new JoinEvent("Carry Davis", 500, 1));
        table.addEventLast(new JoinEvent("Eric Flores", 500, 2));
        table.addEventLast(new MoveButtonEvent(0));

        // Fixed deck for test
        table.addEventLast(new NewDeckEvent(new Deck(Arrays.asList(
                new Card(Rank.QUEEN, Suit.SPADES), // Carry Davis
                new Card(Rank.QUEEN, Suit.CLUBS), // Eric Flores
                new Card(Rank.FOUR, Suit.CLUBS), // Adam Broker
                new Card(Rank.NINE, Suit.DIAMONDS), // Carry Davis
                new Card(Rank.JACK, Suit.HEARTS), // Eric Flores
                new Card(Rank.FOUR, Suit.HEARTS), // Adam Broker
                new Card(Rank.SEVEN, Suit.HEARTS), // Flop
                new Card(Rank.TEN, Suit.HEARTS), // Flop
                new Card(Rank.KING, Suit.DIAMONDS), // Flop
                new Card(Rank.JACK, Suit.SPADES), // Turn
                new Card(Rank.ACE, Suit.HEARTS) // River
        ))));

        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Carry Davis"));
        table.addEventLast(new PostBigBlindEvent("Eric Flores"));
        table.addEventLast(new DealEvent());
        table.addEventLast(new RaiseEvent("Adam Broker", 950));
        table.addEventLast(new CallEvent("Carry Davis"));
        table.addEventLast(new CallEvent("Eric Flores"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(500, table.getSeats().get(0).getStack());
        Assert.assertEquals(750, table.getSeats().get(1).getStack());
        Assert.assertEquals(750, table.getSeats().get(2).getStack());
    }

    /**
     * Heads-up pot with small-blind immediately folding his hand.
     * Adam Broker wins just the small-blind.
     **/
    @Test
    public void testHand4()
            throws TableException {

        final Table table = new Table(500, 1000, 5, 10);

        table.addEventLast(new JoinEvent("Adam Broker", 1000, 0));
        table.addEventLast(new JoinEvent("Carry Davis", 500, 1));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Carry Davis"));
        table.addEventLast(new PostBigBlindEvent("Adam Broker"));
        table.addEventLast(new DealEvent());
        table.addEventLast(new FoldEvent("Carry Davis"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(1005, table.getSeats().get(0).getStack());
        Assert.assertEquals(495, table.getSeats().get(1).getStack());
    }

    /**
     * Heads-up pot with action pre-flop, Carry Davis min-raises and Adam Broker calls.
     * Carry Davis leads out with a min-bet on the flop but gets re-raised by Adam.
     * Carry Davis folds his hand.
     * The pot is won by Adam Broker without showdown.
     */
    @Test
    public void testHand5()
            throws TableException {

        final Table table = new Table(500, 1000, 5, 10);

        table.addEventLast(new JoinEvent("Adam Broker", 1000, 0));
        table.addEventLast(new JoinEvent("Carry Davis", 500, 1));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Carry Davis"));
        table.addEventLast(new PostBigBlindEvent("Adam Broker"));
        table.addEventLast(new DealEvent());
        table.addEventLast(new RaiseEvent("Carry Davis", 20));
        table.addEventLast(new CallEvent("Adam Broker"));
        // Flop
        table.addEventLast(new BetEvent("Carry Davis", 20));
        table.addEventLast(new RaiseEvent("Adam Broker", 100));
        table.addEventLast(new FoldEvent("Carry Davis"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(1040, table.getSeats().get(0).getStack());
        Assert.assertEquals(460, table.getSeats().get(1).getStack());
    }

    /**
     * All three players see the flop with no bets pre-flop.
     * Carry Davis bets on the flop pushing both Eric and Adam off their hands.
     * The pot is won by Carry Davis without showdown.
     */
    @Test
    public void testHand6()
            throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new JoinEvent("Adam Broker", 1000, 0));
        table.addEventLast(new JoinEvent("Carry Davis", 1000, 1));
        table.addEventLast(new JoinEvent("Eric Flores", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Carry Davis"));
        table.addEventLast(new PostBigBlindEvent("Eric Flores"));
        table.addEventLast(new DealEvent());
        table.addEventLast(new CallEvent("Adam Broker"));
        table.addEventLast(new CallEvent("Carry Davis"));
        table.addEventLast(new CheckEvent("Eric Flores"));
        // Flop
        table.addEventLast(new BetEvent("Carry Davis", 50));
        table.addEventLast(new FoldEvent("Eric Flores"));
        table.addEventLast(new FoldEvent("Adam Broker"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(990, table.getSeats().get(0).getStack());
        Assert.assertEquals(1020, table.getSeats().get(1).getStack());
        Assert.assertEquals(990, table.getSeats().get(2).getStack());
    }

    /**
     * All three players see the flop with no bets pre-flop.
     * Carry Davis leads out with a typical sized bet, Eric folds and Adam calls.
     * Carry Davis continuation-bets on the flop pushing Eric off his hand.
     * The pot is won by Carry Davis without showdown.
     */
    @Test
    public void testHand7()
            throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new JoinEvent("Adam Broker", 1000, 0));
        table.addEventLast(new JoinEvent("Carry Davis", 1000, 1));
        table.addEventLast(new JoinEvent("Eric Flores", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Carry Davis"));
        table.addEventLast(new PostBigBlindEvent("Eric Flores"));
        table.addEventLast(new DealEvent());
        table.addEventLast(new CallEvent("Adam Broker"));
        table.addEventLast(new CallEvent("Carry Davis"));
        table.addEventLast(new CheckEvent("Eric Flores"));
        // Flop
        table.addEventLast(new BetEvent("Carry Davis", 50));
        table.addEventLast(new FoldEvent("Eric Flores"));
        table.addEventLast(new CallEvent("Adam Broker"));
        // Turn
        table.addEventLast(new BetEvent("Carry Davis", 100));
        table.addEventLast(new FoldEvent("Adam Broker"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(940, table.getSeats().get(0).getStack());
        Assert.assertEquals(1070, table.getSeats().get(1).getStack());
        Assert.assertEquals(990, table.getSeats().get(2).getStack());
    }

    /**
     * Three player pot with no bets pre-flop.
     * Carry Davis fires bullets on all streets bluffing.
     * Eric folds his hand on the flop.
     * Adam Broker calls two streets and finally raises all-in on the River.
     * Carry Davis has to fold his bluffing hand.
     */
    @Test
    public void testHand8()
            throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new JoinEvent("Adam Broker", 1000, 0));
        table.addEventLast(new JoinEvent("Carry Davis", 1000, 1));
        table.addEventLast(new JoinEvent("Eric Flores", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Carry Davis"));
        table.addEventLast(new PostBigBlindEvent("Eric Flores"));
        table.addEventLast(new DealEvent());
        table.addEventLast(new CallEvent("Adam Broker"));
        table.addEventLast(new CallEvent("Carry Davis"));
        table.addEventLast(new CheckEvent("Eric Flores"));
        // Flop
        table.addEventLast(new BetEvent("Carry Davis", 50));
        table.addEventLast(new FoldEvent("Eric Flores"));
        table.addEventLast(new CallEvent("Adam Broker"));
        // Turn
        table.addEventLast(new BetEvent("Carry Davis", 100));
        table.addEventLast(new CallEvent("Adam Broker"));
        // River
        table.addEventLast(new BetEvent("Carry Davis", 260));
        table.addEventLast(new RaiseEvent("Adam Broker", 840));
        table.addEventLast(new FoldEvent("Carry Davis"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(1430, table.getSeats().get(0).getStack());
        Assert.assertEquals(580, table.getSeats().get(1).getStack());
        Assert.assertEquals(990, table.getSeats().get(2).getStack());
    }

    /**
     * Three players competing for the pot.
     * First bet occurs on the flop Adam Broker and both other players calls out-of-position.
     * Adam Broker bets again on turn and but Eric check-raises him back, all players call.
     * On the River Eric Flores pushes all-in and Adam Broker calls and Carry Davis folds his hand.
     * Eric wins full pot with Three-of-a-kind Jacks over Adam's Two-pair Aces and Kings.
     */
    @Test
    public void testHand9()
            throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new JoinEvent("Adam Broker", 1000, 0));
        table.addEventLast(new JoinEvent("Carry Davis", 1000, 1));
        table.addEventLast(new JoinEvent("Eric Flores", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));

        // Fixed deck for test
        table.addEventLast(new NewDeckEvent(new Deck(Arrays.asList(
                new Card(Rank.TEN, Suit.SPADES), // Carry Davis
                new Card(Rank.JACK, Suit.CLUBS), // Eric Flores
                new Card(Rank.KING, Suit.CLUBS), // Adam Broker
                new Card(Rank.TEN, Suit.DIAMONDS), // Carry Davis
                new Card(Rank.JACK, Suit.HEARTS), // Eric Flores
                new Card(Rank.ACE, Suit.HEARTS), // Adam Broker
                new Card(Rank.SEVEN, Suit.HEARTS), // Flop
                new Card(Rank.EIGHT, Suit.HEARTS), // Flop
                new Card(Rank.KING, Suit.DIAMONDS), // Flop
                new Card(Rank.JACK, Suit.SPADES), // Turn
                new Card(Rank.ACE, Suit.HEARTS) // River
        ))));

        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Carry Davis"));
        table.addEventLast(new PostBigBlindEvent("Eric Flores"));
        table.addEventLast(new DealEvent());
        table.addEventLast(new CallEvent("Adam Broker"));
        table.addEventLast(new CallEvent("Carry Davis"));
        table.addEventLast(new CheckEvent("Eric Flores"));
        // Flop
        table.addEventLast(new CheckEvent("Carry Davis"));
        table.addEventLast(new CheckEvent("Eric Flores"));
        table.addEventLast(new BetEvent("Adam Broker", 25));
        table.addEventLast(new CallEvent("Carry Davis"));
        table.addEventLast(new CallEvent("Eric Flores"));
        // Turn
        table.addEventLast(new CheckEvent("Carry Davis"));
        table.addEventLast(new CheckEvent("Eric Flores"));
        table.addEventLast(new BetEvent("Adam Broker", 85));
        table.addEventLast(new CallEvent("Carry Davis"));
        table.addEventLast(new RaiseEvent("Eric Flores", 170));
        table.addEventLast(new CallEvent("Adam Broker"));
        table.addEventLast(new CallEvent("Carry Davis"));
        // River
        table.addEventLast(new CheckEvent("Carry Davis"));
        table.addEventLast(new BetEvent("Eric Flores", 795));
        table.addEventLast(new CallEvent("Adam Broker"));
        table.addEventLast(new FoldEvent("Carry Davis"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(0, table.getSeats().get(0).getStack());
        Assert.assertEquals(795, table.getSeats().get(1).getStack());
        Assert.assertEquals(2205, table.getSeats().get(2).getStack());
    }

    /**
     * Heads-up pot where both players end up going all-in pre-flop.
     * Adam Broker wins pot with a Jack-high flush.
     */
    @Test
    public void testHand10()
            throws TableException {

        final Table table = new Table(500, 1000, 5, 10);

        table.addEventLast(new JoinEvent("Adam Broker", 600, 0));
        table.addEventLast(new JoinEvent("Carry Davis", 500, 1));
        table.addEventLast(new MoveButtonEvent(0));

        // Fixed deck for test
        table.addEventLast(new NewDeckEvent(new Deck(Arrays.asList(
                new Card(Rank.KING, Suit.SPADES), // Carry Davis
                new Card(Rank.NINE, Suit.CLUBS), // Adam Broker
                new Card(Rank.ACE, Suit.SPADES), // Carry Davis
                new Card(Rank.TEN, Suit.CLUBS), // Adam Broker
                new Card(Rank.EIGHT, Suit.CLUBS), // Flop
                new Card(Rank.TWO, Suit.HEARTS), // Flop
                new Card(Rank.FIVE, Suit.CLUBS), // Flop
                new Card(Rank.FOUR, Suit.DIAMONDS), // Turn
                new Card(Rank.JACK, Suit.CLUBS) // River
        ))));

        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Carry Davis"));
        table.addEventLast(new PostBigBlindEvent("Adam Broker"));
        table.addEventLast(new DealEvent());
        table.addEventLast(new RaiseEvent("Carry Davis", 200));
        table.addEventLast(new RaiseEvent("Adam Broker", 550));
        table.addEventLast(new CallEvent("Carry Davis"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(1100, table.getSeats().get(0).getStack());
        Assert.assertEquals(0, table.getSeats().get(1).getStack());
    }

    /**
     * Two player pot and no bets pre-flop.
     * Adam broker check-calls on flop and turn.
     * No bets on the river and both players have the best hand, Jack high straight.
     * The showdown pot is split evenly between both players.
     */
    @Test
    public void testHand11()
            throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new JoinEvent("Adam Broker", 1000, 0));
        table.addEventLast(new JoinEvent("Carry Davis", 1000, 1));
        table.addEventLast(new JoinEvent("Eric Flores", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        table.addEventLast(new SitOutEvent("Carry Davis", true));

        // Fixed deck for test
        table.addEventLast(new NewDeckEvent(new Deck(Arrays.asList(
                new Card(Rank.SEVEN, Suit.SPADES), // Carry Davis
                new Card(Rank.SEVEN, Suit.DIAMONDS), // Adam Broker
                new Card(Rank.EIGHT, Suit.SPADES), // Carry Davis
                new Card(Rank.EIGHT, Suit.DIAMONDS), // Adam Broker
                new Card(Rank.EIGHT, Suit.CLUBS), // Flop
                new Card(Rank.NINE, Suit.HEARTS), // Flop
                new Card(Rank.TEN, Suit.CLUBS), // Flop
                new Card(Rank.JACK, Suit.DIAMONDS), // Turn
                new Card(Rank.JACK, Suit.HEARTS) // River
        ))));

        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Eric Flores"));
        table.addEventLast(new PostBigBlindEvent("Adam Broker"));
        table.addEventLast(new DealEvent());
        table.addEventLast(new CallEvent("Eric Flores"));
        table.addEventLast(new CheckEvent("Adam Broker"));
        // Flop
        table.addEventLast(new CheckEvent("Eric Flores"));
        table.addEventLast(new BetEvent("Adam Broker", 15));
        table.addEventLast(new CallEvent("Eric Flores"));
        // Turn
        table.addEventLast(new CheckEvent("Eric Flores"));
        table.addEventLast(new BetEvent("Adam Broker", 40));
        table.addEventLast(new CallEvent("Eric Flores"));
        // River
        table.addEventLast(new CheckEvent("Eric Flores"));
        table.addEventLast(new CheckEvent("Adam Broker"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(1000, table.getSeats().get(0).getStack());
        Assert.assertEquals(1000, table.getSeats().get(1).getStack());
        Assert.assertEquals(1000, table.getSeats().get(2).getStack());
    }

    /**
     * Too few players at the table.
     * The hand must should not begin.
     */
    @Test
    public void testHand12()
            throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new JoinEvent("Adam Broker", 1000, 0));
        table.addEventLast(new JoinEvent("Carry Davis", 1000, 1));
        table.addEventLast(new JoinEvent("Eric Flores", 1000, 2));
        table.addEventLast(new SitOutEvent("Carry Davis", true));
        table.addEventLast(new SitOutEvent("Eric Flores", true));

        table.handleEventQueue();

        try {
            table.addEventLast(new MoveButtonEvent(0));
            table.addEventLast(new DealEvent());
            table.handleEventQueue();
            Assert.fail("Too few players to start a hand");
        } catch (TableException e) {
            // Intended exception thrown, there's too few players to start a new hand
        }

        System.out.println(table.toString());

        Assert.assertFalse(table.getSeats().get(0).isSittingOut());
        Assert.assertTrue(table.getSeats().get(1).isSittingOut());
        Assert.assertTrue(table.getSeats().get(2).isSittingOut());
    }

    /**
     * Two players competing for the pot while the third sits out.
     * Carry Davis raises all-in pre-flop and makes Adam Broker fold his hand.
     */
    @Test
    public void testHand13()
            throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new JoinEvent("Adam Broker", 1000, 0));
        table.addEventLast(new JoinEvent("Carry Davis", 1000, 1));
        table.addEventLast(new JoinEvent("Eric Flores", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        table.addEventLast(new SitOutEvent("Carry Davis", true));
        table.addEventLast(new SitOutEvent("Eric Flores", true));
        table.addEventLast(new SitOutEvent("Carry Davis", false));
        table.addEventLast(new PostSmallBlindEvent("Carry Davis"));
        table.addEventLast(new PostBigBlindEvent("Adam Broker"));
        table.addEventLast(new DealEvent());
        // Pre-flop
        table.addEventLast(new RaiseEvent("Carry Davis", 995));
        table.addEventLast(new FoldEvent("Adam Broker"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(990, table.getSeats().get(0).getStack());
        Assert.assertEquals(1010, table.getSeats().get(1).getStack());
        Assert.assertEquals(1000, table.getSeats().get(2).getStack());
    }

    /**
     * The smallest stacked player wins with the hand and triples his stack in a 3-way all-in showdown.
     * The remaining two players both have the second best hand and split the remaining side-pot.
     */
    @Test
    public void testHand14()
            throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new JoinEvent("Adam Broker", 1000, 0));
        table.addEventLast(new JoinEvent("Carry Davis", 750, 1));
        table.addEventLast(new JoinEvent("Eric Flores", 500, 2));
        table.addEventLast(new MoveButtonEvent(0));

        // Fixed deck for test
        table.addEventLast(new NewDeckEvent(new Deck(Arrays.asList(
                new Card(Rank.SEVEN, Suit.SPADES), // Carry Davis
                new Card(Rank.NINE, Suit.HEARTS), // Eric Flores
                new Card(Rank.SEVEN, Suit.DIAMONDS), // Adam Broker
                new Card(Rank.EIGHT, Suit.SPADES), // Carry Davis
                new Card(Rank.NINE, Suit.CLUBS), // Eric Flores
                new Card(Rank.EIGHT, Suit.DIAMONDS), // Adam Broker
                new Card(Rank.EIGHT, Suit.CLUBS), // Flop
                new Card(Rank.NINE, Suit.HEARTS), // Flop
                new Card(Rank.TEN, Suit.CLUBS), // Flop
                new Card(Rank.JACK, Suit.DIAMONDS), // Turn
                new Card(Rank.JACK, Suit.HEARTS) // River
        ))));

        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Carry Davis"));
        table.addEventLast(new PostBigBlindEvent("Eric Flores"));
        table.addEventLast(new DealEvent());

        table.addEventLast(new RaiseEvent("Adam Broker", 1000));
        table.addEventLast(new CallEvent("Carry Davis"));
        table.addEventLast(new CallEvent("Eric Flores"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(500, table.getSeats().get(0).getStack());
        Assert.assertEquals(250, table.getSeats().get(1).getStack());
        Assert.assertEquals(1500, table.getSeats().get(2).getStack());
    }

    /**
     * Massive split pot of all six players going all-in.
     * All players have different stack sizes and therefore there are six split-pots to distribute.
     */
    @Test
    public void testHand15()
            throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new JoinEvent("Adam Broker", 550, 0));
        table.addEventLast(new JoinEvent("Carry Davis", 530, 1));
        table.addEventLast(new JoinEvent("Eric Flores", 540, 2));
        table.addEventLast(new JoinEvent("Gordon Hughes", 510, 3));
        table.addEventLast(new JoinEvent("Irvin Jones", 520, 4));
        table.addEventLast(new JoinEvent("Kevin Lee", 500, 5));
        table.addEventLast(new MoveButtonEvent(0));

        // Fixed deck for test
        table.addEventLast(new NewDeckEvent(new Deck(Arrays.asList(
                new Card(Rank.TWO, Suit.SPADES), // Carry Davis
                new Card(Rank.TWO, Suit.CLUBS), // Eric Flores
                new Card(Rank.TWO, Suit.DIAMONDS), // Gorgon Hughes
                new Card(Rank.TWO, Suit.HEARTS), // Irvin Jones
                new Card(Rank.THREE, Suit.SPADES), // Kevin Lee
                new Card(Rank.THREE, Suit.CLUBS), // Adam Broker
                new Card(Rank.THREE, Suit.DIAMONDS), // Carry Davis
                new Card(Rank.THREE, Suit.HEARTS), // Eric Flores
                new Card(Rank.FOUR, Suit.SPADES), // Gorgon Hughes
                new Card(Rank.FOUR, Suit.CLUBS), // Irvin Jones
                new Card(Rank.FOUR, Suit.DIAMONDS), // Kevin Lee
                new Card(Rank.FOUR, Suit.HEARTS), // Adam Broker
                new Card(Rank.TEN, Suit.HEARTS), // Flop
                new Card(Rank.JACK, Suit.HEARTS), // Flop
                new Card(Rank.QUEEN, Suit.HEARTS), // Flop
                new Card(Rank.KING, Suit.HEARTS), // Turn
                new Card(Rank.ACE, Suit.HEARTS) // River
        ))));

        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Carry Davis"));
        table.addEventLast(new PostBigBlindEvent("Eric Flores"));
        table.addEventLast(new DealEvent());

        table.addEventLast(new RaiseEvent("Gordon Hughes", 510));
        table.addEventLast(new CallEvent("Irvin Jones"));
        table.addEventLast(new CallEvent("Kevin Lee"));
        table.addEventLast(new RaiseEvent("Adam Broker", 550));
        table.addEventLast(new CallEvent("Carry Davis"));
        table.addEventLast(new CallEvent("Eric Flores"));
        table.addEventLast(new CallEvent("Irvin Jones"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(550, table.getSeats().get(0).getStack());
        Assert.assertEquals(530, table.getSeats().get(1).getStack());
        Assert.assertEquals(540, table.getSeats().get(2).getStack());
        Assert.assertEquals(510, table.getSeats().get(3).getStack());
        Assert.assertEquals(520, table.getSeats().get(4).getStack());
        Assert.assertEquals(500, table.getSeats().get(5).getStack());
    }

    /**
     * Testing scenario where a pot can't be perfectly split evenly.
     * One player must receive the remaining indivisible chip.
     * Eric Flores wins the odd chip because he's the first player on the left of the dealer.
     */
    @Test
    public void testHand16()
            throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new JoinEvent("Adam Broker", 500, 0));
        table.addEventLast(new JoinEvent("Carry Davis", 500, 1));
        table.addEventLast(new JoinEvent("Eric Flores", 500, 2));
        table.addEventLast(new MoveButtonEvent(0));

        // Fixed deck for test
        table.addEventLast(new NewDeckEvent(new Deck(Arrays.asList(
                new Card(Rank.TWO, Suit.SPADES), // Carry Davis
                new Card(Rank.FIVE, Suit.SPADES), // Eric Flores
                new Card(Rank.FIVE, Suit.SPADES), // Adam Broker
                new Card(Rank.THREE, Suit.CLUBS), // Carry Davis
                new Card(Rank.SIX, Suit.CLUBS), // Eric Flores
                new Card(Rank.SIX, Suit.CLUBS), // Adam Broker
                new Card(Rank.FOUR, Suit.HEARTS), // Flop
                new Card(Rank.SEVEN, Suit.HEARTS), // Flop
                new Card(Rank.QUEEN, Suit.HEARTS), // Flop
                new Card(Rank.KING, Suit.HEARTS), // Turn
                new Card(Rank.ACE, Suit.DIAMONDS) // River
        ))));

        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Carry Davis"));
        table.addEventLast(new PostBigBlindEvent("Eric Flores"));
        table.addEventLast(new DealEvent());
        table.addEventLast(new CallEvent("Adam Broker"));
        table.addEventLast(new FoldEvent("Carry Davis"));
        table.addEventLast(new CheckEvent("Eric Flores"));
        // Flop
        table.addEventLast(new CheckEvent("Eric Flores"));
        table.addEventLast(new CheckEvent("Adam Broker"));
        // Turn
        table.addEventLast(new CheckEvent("Eric Flores"));
        table.addEventLast(new CheckEvent("Adam Broker"));
        // River
        table.addEventLast(new CheckEvent("Eric Flores"));
        table.addEventLast(new CheckEvent("Adam Broker"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(502, table.getSeats().get(0).getStack());
        Assert.assertEquals(495, table.getSeats().get(1).getStack());
        Assert.assertEquals(503, table.getSeats().get(2).getStack());
    }
}
