package com.ictpoker.ixi;

import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.Exception.TableException;
import com.ictpoker.ixi.Table.TableEvent.Action.*;
import com.ictpoker.ixi.Table.TableEvent.Info.DealEvent;
import com.ictpoker.ixi.Table.TableEvent.Action.JoinEvent;
import com.ictpoker.ixi.Table.TableEvent.Info.MoveButtonEvent;
import com.ictpoker.ixi.Table.TableEvent.Info.SetSeatEvent;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class HandTest {

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.println("\nStarting test: " + description.getMethodName());
        }
    };

    @Test
    public void testHand1()
            throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.pushEvent(new SetSeatEvent("Adam Broker", 1000, 0));
        table.pushEvent(new SetSeatEvent("Carry Davis", 1000, 1));
        table.pushEvent(new SetSeatEvent("Eric Flores", 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        // Pre-flop
        table.pushEvent(new PostSmallBlindEvent("Carry Davis"));
        table.pushEvent(new PostBigBlindEvent("Eric Flores"));
        table.pushEvent(new DealEvent());
        table.pushEvent(new CallEvent("Adam Broker"));
        table.pushEvent(new CallEvent("Carry Davis"));
        table.pushEvent(new CheckEvent("Eric Flores"));
        // Flop
        table.pushEvent(new CheckEvent("Carry Davis"));
        table.pushEvent(new CheckEvent("Eric Flores"));
        table.pushEvent(new CheckEvent("Adam Broker"));
        // Turn
        table.pushEvent(new CheckEvent("Carry Davis"));
        table.pushEvent(new CheckEvent("Eric Flores"));
        table.pushEvent(new CheckEvent("Adam Broker"));
        // River
        table.pushEvent(new CheckEvent("Carry Davis"));
        table.pushEvent(new CheckEvent("Eric Flores"));
        table.pushEvent(new CheckEvent("Adam Broker"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(990, table.getSeats().get(0).getStack());
        Assert.assertEquals(990, table.getSeats().get(1).getStack());
        Assert.assertEquals(990, table.getSeats().get(2).getStack());
        Assert.assertEquals(10, table.getSeats().get(0).getCollected());
        Assert.assertEquals(10, table.getSeats().get(1).getCollected());
        Assert.assertEquals(10, table.getSeats().get(2).getCollected());
        Assert.assertEquals(30, table.getTotalPot());
        Assert.assertFalse(table.getSeats().get(0).isSittingOut());
        Assert.assertFalse(table.getSeats().get(1).isSittingOut());
        Assert.assertFalse(table.getSeats().get(2).isSittingOut());
    }

    @Test
    public void testHand2()
            throws TableException {

        final Table table = new Table(500, 1000, 5, 10);

        table.pushEvent(new JoinEvent("Adam Broker", 1000, 0));
        table.pushEvent(new JoinEvent("Carry Davis", 1000, 1));
        table.pushEvent(new JoinEvent("Eric Flores", 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        // Pre-flop
        table.pushEvent(new PostSmallBlindEvent("Carry Davis"));
        table.pushEvent(new PostBigBlindEvent("Eric Flores"));
        table.pushEvent(new DealEvent());
        table.pushEvent(new RaiseEvent("Adam Broker", 40));
        table.pushEvent(new FoldEvent("Carry Davis"));
        table.pushEvent(new FoldEvent("Eric Flores"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(990, table.getSeats().get(0).getStack());
        Assert.assertEquals(995, table.getSeats().get(1).getStack());
        Assert.assertEquals(990, table.getSeats().get(2).getStack());
        Assert.assertEquals(10, table.getSeats().get(0).getCollected());
        Assert.assertEquals(5, table.getSeats().get(1).getCollected());
        Assert.assertEquals(10, table.getSeats().get(2).getCollected());
        Assert.assertEquals(25, table.getTotalPot());
        Assert.assertFalse(table.getSeats().get(0).isSittingOut());
        Assert.assertFalse(table.getSeats().get(1).isSittingOut());
        Assert.assertFalse(table.getSeats().get(2).isSittingOut());
    }

    @Test
    public void testHand3()
            throws TableException {

        final Table table = new Table(500, 1000, 5, 10);

        table.pushEvent(new JoinEvent("Adam Broker", 1000, 0));
        table.pushEvent(new JoinEvent("Carry Davis", 500, 1));
        table.pushEvent(new JoinEvent("Eric Flores", 500, 2));
        table.pushEvent(new MoveButtonEvent(0));
        // Pre-flop
        table.pushEvent(new PostSmallBlindEvent("Carry Davis"));
        table.pushEvent(new PostBigBlindEvent("Eric Flores"));
        table.pushEvent(new DealEvent());
        table.pushEvent(new RaiseEvent("Adam Broker", 950));
        table.pushEvent(new CallEvent("Carry Davis"));
        table.pushEvent(new CallEvent("Eric Flores"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(500, table.getSeats().get(0).getStack());
        Assert.assertEquals(0, table.getSeats().get(1).getStack());
        Assert.assertEquals(0, table.getSeats().get(2).getStack());
        Assert.assertEquals(500, table.getSeats().get(0).getCollected());
        Assert.assertEquals(500, table.getSeats().get(1).getCollected());
        Assert.assertEquals(500, table.getSeats().get(2).getCollected());
        Assert.assertEquals(1500, table.getTotalPot());
        Assert.assertFalse(table.getSeats().get(0).isSittingOut());
        Assert.assertFalse(table.getSeats().get(1).isSittingOut());
        Assert.assertFalse(table.getSeats().get(2).isSittingOut());
    }

    @Test
    public void testHand4()
            throws TableException {

        final Table table = new Table(500, 1000, 5, 10);

        table.pushEvent(new JoinEvent("Adam Broker", 1000, 0));
        table.pushEvent(new JoinEvent("Carry Davis", 500, 1));
        table.pushEvent(new MoveButtonEvent(0));
        // Pre-flop
        table.pushEvent(new PostSmallBlindEvent("Carry Davis"));
        table.pushEvent(new PostBigBlindEvent("Adam Broker"));
        table.pushEvent(new DealEvent());
        table.pushEvent(new FoldEvent("Carry Davis"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(995, table.getSeats().get(0).getStack());
        Assert.assertEquals(495, table.getSeats().get(1).getStack());
        Assert.assertEquals(5, table.getSeats().get(0).getCollected());
        Assert.assertEquals(5, table.getSeats().get(1).getCollected());
        Assert.assertEquals(10, table.getTotalPot());
        Assert.assertFalse(table.getSeats().get(0).isSittingOut());
        Assert.assertFalse(table.getSeats().get(1).isSittingOut());
    }

    @Test
    public void testHand5()
            throws TableException {

        final Table table = new Table(500, 1000, 5, 10);

        table.pushEvent(new JoinEvent("Adam Broker", 1000, 0));
        table.pushEvent(new JoinEvent("Carry Davis", 500, 1));
        table.pushEvent(new MoveButtonEvent(0));
        // Pre-flop
        table.pushEvent(new PostSmallBlindEvent("Carry Davis"));
        table.pushEvent(new PostBigBlindEvent("Adam Broker"));
        table.pushEvent(new DealEvent());
        table.pushEvent(new RaiseEvent("Carry Davis", 20));
        table.pushEvent(new CallEvent("Adam Broker"));
        // Flop
        table.pushEvent(new BetEvent("Carry Davis", 20));
        table.pushEvent(new RaiseEvent("Adam Broker", 100));
        table.pushEvent(new FoldEvent("Carry Davis"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(960, table.getSeats().get(0).getStack());
        Assert.assertEquals(460, table.getSeats().get(1).getStack());
        Assert.assertEquals(40, table.getSeats().get(0).getCollected());
        Assert.assertEquals(40, table.getSeats().get(1).getCollected());
        Assert.assertEquals(80, table.getTotalPot());
        Assert.assertFalse(table.getSeats().get(0).isSittingOut());
        Assert.assertFalse(table.getSeats().get(1).isSittingOut());
    }

    @Test
    public void testHand6()
            throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.pushEvent(new JoinEvent("Adam Broker", 1000, 0));
        table.pushEvent(new JoinEvent("Carry Davis", 1000, 1));
        table.pushEvent(new JoinEvent("Eric Flores", 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        // Pre-flop
        table.pushEvent(new PostSmallBlindEvent("Carry Davis"));
        table.pushEvent(new PostBigBlindEvent("Eric Flores"));
        table.pushEvent(new DealEvent());
        table.pushEvent(new CallEvent("Adam Broker"));
        table.pushEvent(new CallEvent("Carry Davis"));
        table.pushEvent(new CheckEvent("Eric Flores"));
        // Flop
        table.pushEvent(new BetEvent("Carry Davis", 50));
        table.pushEvent(new FoldEvent("Eric Flores"));
        table.pushEvent(new FoldEvent("Adam Broker"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(990, table.getSeats().get(0).getStack());
        Assert.assertEquals(990, table.getSeats().get(1).getStack());
        Assert.assertEquals(990, table.getSeats().get(2).getStack());
        Assert.assertEquals(10, table.getSeats().get(0).getCollected());
        Assert.assertEquals(10, table.getSeats().get(1).getCollected());
        Assert.assertEquals(10, table.getSeats().get(2).getCollected());
        Assert.assertEquals(30, table.getTotalPot());
        Assert.assertFalse(table.getSeats().get(0).isSittingOut());
        Assert.assertFalse(table.getSeats().get(1).isSittingOut());
        Assert.assertFalse(table.getSeats().get(2).isSittingOut());
    }

    @Test
    public void testHand7()
            throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.pushEvent(new JoinEvent("Adam Broker", 1000, 0));
        table.pushEvent(new JoinEvent("Carry Davis", 1000, 1));
        table.pushEvent(new JoinEvent("Eric Flores", 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        // Pre-flop
        table.pushEvent(new PostSmallBlindEvent("Carry Davis"));
        table.pushEvent(new PostBigBlindEvent("Eric Flores"));
        table.pushEvent(new DealEvent());
        table.pushEvent(new CallEvent("Adam Broker"));
        table.pushEvent(new CallEvent("Carry Davis"));
        table.pushEvent(new CheckEvent("Eric Flores"));
        // Flop
        table.pushEvent(new BetEvent("Carry Davis", 50));
        table.pushEvent(new FoldEvent("Eric Flores"));
        table.pushEvent(new CallEvent("Adam Broker"));
        // Turn
        table.pushEvent(new BetEvent("Carry Davis", 100));
        table.pushEvent(new FoldEvent("Adam Broker"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(940, table.getSeats().get(0).getStack());
        Assert.assertEquals(940, table.getSeats().get(1).getStack());
        Assert.assertEquals(990, table.getSeats().get(2).getStack());
        Assert.assertEquals(60, table.getSeats().get(0).getCollected());
        Assert.assertEquals(60, table.getSeats().get(1).getCollected());
        Assert.assertEquals(10, table.getSeats().get(2).getCollected());
        Assert.assertEquals(130, table.getTotalPot());
        Assert.assertFalse(table.getSeats().get(0).isSittingOut());
        Assert.assertFalse(table.getSeats().get(1).isSittingOut());
        Assert.assertFalse(table.getSeats().get(2).isSittingOut());
    }

    @Test
    public void testHand8()
            throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.pushEvent(new JoinEvent("Adam Broker", 1000, 0));
        table.pushEvent(new JoinEvent("Carry Davis", 1000, 1));
        table.pushEvent(new JoinEvent("Eric Flores", 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        // Pre-flop
        table.pushEvent(new PostSmallBlindEvent("Carry Davis"));
        table.pushEvent(new PostBigBlindEvent("Eric Flores"));
        table.pushEvent(new DealEvent());
        table.pushEvent(new CallEvent("Adam Broker"));
        table.pushEvent(new CallEvent("Carry Davis"));
        table.pushEvent(new CheckEvent("Eric Flores"));
        // Flop
        table.pushEvent(new BetEvent("Carry Davis", 50));
        table.pushEvent(new FoldEvent("Eric Flores"));
        table.pushEvent(new CallEvent("Adam Broker"));
        // Turn
        table.pushEvent(new BetEvent("Carry Davis", 100));
        table.pushEvent(new CallEvent("Adam Broker"));
        // River
        table.pushEvent(new BetEvent("Carry Davis", 260));
        table.pushEvent(new RaiseEvent("Adam Broker", 840));
        table.pushEvent(new FoldEvent("Carry Davis"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(580, table.getSeats().get(0).getStack());
        Assert.assertEquals(580, table.getSeats().get(1).getStack());
        Assert.assertEquals(990, table.getSeats().get(2).getStack());
        Assert.assertEquals(420, table.getSeats().get(0).getCollected());
        Assert.assertEquals(420, table.getSeats().get(1).getCollected());
        Assert.assertEquals(10, table.getSeats().get(2).getCollected());
        Assert.assertEquals(850, table.getTotalPot());
        Assert.assertFalse(table.getSeats().get(0).isSittingOut());
        Assert.assertFalse(table.getSeats().get(1).isSittingOut());
        Assert.assertFalse(table.getSeats().get(2).isSittingOut());
    }

    @Test
    public void testHand9()
            throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.pushEvent(new JoinEvent("Adam Broker", 1000, 0));
        table.pushEvent(new JoinEvent("Carry Davis", 1000, 1));
        table.pushEvent(new JoinEvent("Eric Flores", 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        // Pre-flop
        table.pushEvent(new PostSmallBlindEvent("Carry Davis"));
        table.pushEvent(new PostBigBlindEvent("Eric Flores"));
        table.pushEvent(new DealEvent());
        table.pushEvent(new CallEvent("Adam Broker"));
        table.pushEvent(new CallEvent("Carry Davis"));
        table.pushEvent(new CheckEvent("Eric Flores"));
        // Flop
        table.pushEvent(new CheckEvent("Carry Davis"));
        table.pushEvent(new CheckEvent("Eric Flores"));
        table.pushEvent(new BetEvent("Adam Broker", 25));
        table.pushEvent(new CallEvent("Carry Davis"));
        table.pushEvent(new CallEvent("Eric Flores"));
        // Turn
        table.pushEvent(new CheckEvent("Carry Davis"));
        table.pushEvent(new CheckEvent("Eric Flores"));
        table.pushEvent(new BetEvent("Adam Broker", 85));
        table.pushEvent(new CallEvent("Carry Davis"));
        table.pushEvent(new RaiseEvent("Eric Flores", 170));
        table.pushEvent(new CallEvent("Adam Broker"));
        table.pushEvent(new CallEvent("Carry Davis"));
        // River
        table.pushEvent(new CheckEvent("Carry Davis"));
        table.pushEvent(new BetEvent("Eric Flores", 795));
        table.pushEvent(new CallEvent("Adam Broker"));
        table.pushEvent(new FoldEvent("Carry Davis"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(0, table.getSeats().get(0).getStack());
        Assert.assertEquals(795, table.getSeats().get(1).getStack());
        Assert.assertEquals(0, table.getSeats().get(2).getStack());
        Assert.assertEquals(1000, table.getSeats().get(0).getCollected());
        Assert.assertEquals(205, table.getSeats().get(1).getCollected());
        Assert.assertEquals(1000, table.getSeats().get(2).getCollected());
        Assert.assertEquals(2205, table.getTotalPot());
        Assert.assertFalse(table.getSeats().get(0).isSittingOut());
        Assert.assertFalse(table.getSeats().get(1).isSittingOut());
        Assert.assertFalse(table.getSeats().get(2).isSittingOut());
    }

    @Test
    public void testHand10()
            throws TableException {

        final Table table = new Table(500, 1000, 5, 10);

        table.pushEvent(new JoinEvent("Adam Broker", 600, 0));
        table.pushEvent(new JoinEvent("Carry Davis", 500, 1));
        table.pushEvent(new MoveButtonEvent(0));
        // Pre-flop
        table.pushEvent(new PostSmallBlindEvent("Carry Davis"));
        table.pushEvent(new PostBigBlindEvent("Adam Broker"));
        table.pushEvent(new DealEvent());
        table.pushEvent(new RaiseEvent("Carry Davis", 200));
        table.pushEvent(new RaiseEvent("Adam Broker", 550));
        table.pushEvent(new CallEvent("Carry Davis"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(100, table.getSeats().get(0).getStack());
        Assert.assertEquals(0, table.getSeats().get(1).getStack());
        Assert.assertEquals(500, table.getSeats().get(0).getCollected());
        Assert.assertEquals(500, table.getSeats().get(1).getCollected());
        Assert.assertEquals(1000, table.getTotalPot());
        Assert.assertFalse(table.getSeats().get(0).isSittingOut());
        Assert.assertFalse(table.getSeats().get(1).isSittingOut());
    }

    @Test
    public void testHand11()
            throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.pushEvent(new JoinEvent("Adam Broker", 1000, 0));
        table.pushEvent(new JoinEvent("Carry Davis", 1000, 1));
        table.pushEvent(new JoinEvent("Eric Flores", 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        // Pre-flop
        table.pushEvent(new SitOutEvent("Carry Davis", true));
        table.pushEvent(new PostSmallBlindEvent("Eric Flores"));
        table.pushEvent(new PostBigBlindEvent("Adam Broker"));
        table.pushEvent(new DealEvent());
        table.pushEvent(new CallEvent("Eric Flores"));
        table.pushEvent(new CheckEvent("Adam Broker"));
        // Flop
        table.pushEvent(new CheckEvent("Eric Flores"));
        table.pushEvent(new BetEvent("Adam Broker", 15));
        table.pushEvent(new CallEvent("Eric Flores"));
        // Turn
        table.pushEvent(new CheckEvent("Eric Flores"));
        table.pushEvent(new BetEvent("Adam Broker", 40));
        table.pushEvent(new CallEvent("Eric Flores"));
        // River
        table.pushEvent(new CheckEvent("Eric Flores"));
        table.pushEvent(new CheckEvent("Adam Broker"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(935, table.getSeats().get(0).getStack());
        Assert.assertEquals(1000, table.getSeats().get(1).getStack());
        Assert.assertEquals(935, table.getSeats().get(2).getStack());
        Assert.assertEquals(65, table.getSeats().get(0).getCollected());
        Assert.assertEquals(0, table.getSeats().get(1).getCollected());
        Assert.assertEquals(65, table.getSeats().get(2).getCollected());
        Assert.assertEquals(130, table.getTotalPot());
        Assert.assertFalse(table.getSeats().get(0).isSittingOut());
        Assert.assertTrue(table.getSeats().get(1).isSittingOut());
        Assert.assertFalse(table.getSeats().get(2).isSittingOut());
    }

    @Test
    public void testHand12()
            throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.pushEvent(new JoinEvent("Adam Broker", 1000, 0));
        table.pushEvent(new JoinEvent("Carry Davis", 1000, 1));
        table.pushEvent(new JoinEvent("Eric Flores", 1000, 2));
        // Pre-flop
        table.pushEvent(new SitOutEvent("Carry Davis", true));
        table.pushEvent(new SitOutEvent("Eric Flores", true));

        table.handleEventQueue();

        try {
            table.pushEvent(new MoveButtonEvent(0));
            table.pushEvent(new DealEvent());
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

    @Test
    public void testHand13()
            throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.pushEvent(new JoinEvent("Adam Broker", 1000, 0));
        table.pushEvent(new JoinEvent("Carry Davis", 1000, 1));
        table.pushEvent(new JoinEvent("Eric Flores", 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        // Pre-flop
        table.pushEvent(new SitOutEvent("Carry Davis", true));
        table.pushEvent(new SitOutEvent("Eric Flores", true));
        table.pushEvent(new SitOutEvent("Carry Davis", false));
        table.pushEvent(new PostSmallBlindEvent("Carry Davis"));
        table.pushEvent(new PostBigBlindEvent("Adam Broker"));
        table.pushEvent(new DealEvent());
        table.pushEvent(new RaiseEvent("Carry Davis", 995));
        table.pushEvent(new FoldEvent("Adam Broker"));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(990, table.getSeats().get(0).getStack());
        Assert.assertEquals(990, table.getSeats().get(1).getStack());
        Assert.assertEquals(1000, table.getSeats().get(2).getStack());
        Assert.assertEquals(10, table.getSeats().get(0).getCollected());
        Assert.assertEquals(10, table.getSeats().get(1).getCollected());
        Assert.assertEquals(0, table.getSeats().get(2).getCollected());
        Assert.assertEquals(20, table.getTotalPot());
        Assert.assertFalse(table.getSeats().get(0).isSittingOut());
        Assert.assertFalse(table.getSeats().get(1).isSittingOut());
        Assert.assertTrue(table.getSeats().get(2).isSittingOut());
    }
}
