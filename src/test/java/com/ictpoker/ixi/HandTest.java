package com.ictpoker.ixi;

import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Exception.TableStateException;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.Exception.TableException;
import com.ictpoker.ixi.Table.TableEvent.*;
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
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(500, 1000, 5,10);
        final Player adamBroker = new Player("Adam Broker", 2000);
        final Player carryDavis = new Player("Carry Davis", 1000);
        final Player ericFlores = new Player("Eric Flores", 1000);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 1000, 1));
        table.pushEvent(new JoinEvent(ericFlores, 1000, 2));
        table.pushEvent(new DealEvent(0));
        table.pushEvent(new CommitEvent(carryDavis, 5));
        table.pushEvent(new CommitEvent(ericFlores, 10));
        table.pushEvent(new CommitEvent(adamBroker, 10));
        table.pushEvent(new CommitEvent(carryDavis, 5));
        table.pushEvent(new CommitEvent(ericFlores, 0));
        table.pushEvent(new CommitEvent(carryDavis, 0));
        table.pushEvent(new CommitEvent(ericFlores, 0));
        table.pushEvent(new CommitEvent(adamBroker, 0));
        table.pushEvent(new CommitEvent(carryDavis, 0));
        table.pushEvent(new CommitEvent(ericFlores, 0));
        table.pushEvent(new CommitEvent(adamBroker, 0));
        table.pushEvent(new CommitEvent(carryDavis, 0));
        table.pushEvent(new CommitEvent(ericFlores, 0));
        table.pushEvent(new CommitEvent(adamBroker, 0));

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
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(500, 1000, 5, 10);
        final Player adamBroker = new Player("Adam Broker", 2000);
        final Player carryDavis = new Player("Carry Davis", 1000);
        final Player ericFlores = new Player("Eric Flores", 1000);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 1000, 1));
        table.pushEvent(new JoinEvent(ericFlores, 1000, 2));
        table.pushEvent(new DealEvent(0));
        table.pushEvent(new CommitEvent(carryDavis, 5));
        table.pushEvent(new CommitEvent(ericFlores, 10));
        table.pushEvent(new CommitEvent(adamBroker, 40)); // Bet
        table.pushEvent(new FoldEvent(carryDavis));
        table.pushEvent(new FoldEvent(ericFlores));

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
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(500, 1000, 5, 10);
        final Player adamBroker = new Player("Adam Broker", 1000);
        final Player carryDavis = new Player("Carry Davis", 500);
        final Player ericFlores = new Player("Eric Flores", 500);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 500, 1));
        table.pushEvent(new JoinEvent(ericFlores, 500, 2));
        table.pushEvent(new DealEvent(0));
        table.pushEvent(new CommitEvent(carryDavis, 5));
        table.pushEvent(new CommitEvent(ericFlores, 10));
        table.pushEvent(new CommitEvent(adamBroker, 950));
        table.pushEvent(new CommitEvent(carryDavis, 495));
        table.pushEvent(new CommitEvent(ericFlores, 490));

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
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(500, 1000, 5, 10);
        final Player adamBroker = new Player("Adam Broker", 1000);
        final Player carryDavis = new Player("Carry Davis", 500);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 500, 1));
        table.pushEvent(new DealEvent(0));
        table.pushEvent(new CommitEvent(carryDavis, 5));
        table.pushEvent(new CommitEvent(adamBroker, 10));
        table.pushEvent(new FoldEvent(carryDavis));

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
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(500, 1000, 5, 10);
        final Player adamBroker = new Player("Adam Broker", 1000);
        final Player carryDavis = new Player("Carry Davis", 500);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 500, 1));
        table.pushEvent(new DealEvent(0));
        table.pushEvent(new CommitEvent(carryDavis, 5));
        table.pushEvent(new CommitEvent(adamBroker, 10));
        table.pushEvent(new CommitEvent(carryDavis, 15));
        table.pushEvent(new CommitEvent(adamBroker, 20));
        table.pushEvent(new CommitEvent(carryDavis, 20));
        table.pushEvent(new CommitEvent(adamBroker, 100));
        table.pushEvent(new FoldEvent(carryDavis));

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
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(500, 1000, 5,10);
        final Player adamBroker = new Player("Adam Broker", 2000);
        final Player carryDavis = new Player("Carry Davis", 1000);
        final Player ericFlores = new Player("Eric Flores", 1000);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 1000, 1));
        table.pushEvent(new JoinEvent(ericFlores, 1000, 2));
        table.pushEvent(new DealEvent(0));
        table.pushEvent(new CommitEvent(carryDavis, 5));
        table.pushEvent(new CommitEvent(ericFlores, 10));
        table.pushEvent(new CommitEvent(adamBroker, 10));
        table.pushEvent(new CommitEvent(carryDavis, 5));
        table.pushEvent(new CommitEvent(ericFlores, 0));
        table.pushEvent(new CommitEvent(carryDavis, 50));
        table.pushEvent(new FoldEvent(ericFlores));
        table.pushEvent(new FoldEvent(adamBroker));

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
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(500, 1000, 5,10);
        final Player adamBroker = new Player("Adam Broker", 2000);
        final Player carryDavis = new Player("Carry Davis", 1000);
        final Player ericFlores = new Player("Eric Flores", 1000);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 1000, 1));
        table.pushEvent(new JoinEvent(ericFlores, 1000, 2));
        table.pushEvent(new DealEvent(0));
        table.pushEvent(new CommitEvent(carryDavis, 5));
        table.pushEvent(new CommitEvent(ericFlores, 10));
        table.pushEvent(new CommitEvent(adamBroker, 10));
        table.pushEvent(new CommitEvent(carryDavis, 5));
        table.pushEvent(new CommitEvent(ericFlores, 0));
        table.pushEvent(new CommitEvent(carryDavis, 50));
        table.pushEvent(new FoldEvent(ericFlores));
        table.pushEvent(new CommitEvent(adamBroker, 50));
        table.pushEvent(new CommitEvent(carryDavis, 100));
        table.pushEvent(new FoldEvent(adamBroker));

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
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(500, 1000, 5,10);
        final Player adamBroker = new Player("Adam Broker", 2000);
        final Player carryDavis = new Player("Carry Davis", 1000);
        final Player ericFlores = new Player("Eric Flores", 1000);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 1000, 1));
        table.pushEvent(new JoinEvent(ericFlores, 1000, 2));
        table.pushEvent(new DealEvent(0));
        table.pushEvent(new CommitEvent(carryDavis, 5));
        table.pushEvent(new CommitEvent(ericFlores, 10));
        table.pushEvent(new CommitEvent(adamBroker, 10));
        table.pushEvent(new CommitEvent(carryDavis, 5));
        table.pushEvent(new CommitEvent(ericFlores, 0));
        table.pushEvent(new CommitEvent(carryDavis, 50));
        table.pushEvent(new FoldEvent(ericFlores));
        table.pushEvent(new CommitEvent(adamBroker, 50));
        table.pushEvent(new CommitEvent(carryDavis, 100));
        table.pushEvent(new CommitEvent(adamBroker, 100));
        table.pushEvent(new CommitEvent(carryDavis, 260));
        table.pushEvent(new CommitEvent(adamBroker, 840));
        table.pushEvent(new FoldEvent(carryDavis));

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
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(500, 1000, 5,10);
        final Player adamBroker = new Player("Adam Broker", 2000);
        final Player carryDavis = new Player("Carry Davis", 1000);
        final Player ericFlores = new Player("Eric Flores", 1000);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 1000, 1));
        table.pushEvent(new JoinEvent(ericFlores, 1000, 2));
        table.pushEvent(new DealEvent(0));
        table.pushEvent(new CommitEvent(carryDavis, 5));
        table.pushEvent(new CommitEvent(ericFlores, 10));
        table.pushEvent(new CommitEvent(adamBroker, 10));
        table.pushEvent(new CommitEvent(carryDavis, 5));
        table.pushEvent(new CommitEvent(ericFlores, 0));
        table.pushEvent(new CommitEvent(carryDavis, 0));
        table.pushEvent(new CommitEvent(ericFlores, 0));
        table.pushEvent(new CommitEvent(adamBroker, 25));
        table.pushEvent(new CommitEvent(carryDavis, 25));
        table.pushEvent(new CommitEvent(ericFlores, 25));
        table.pushEvent(new CommitEvent(carryDavis, 0));
        table.pushEvent(new CommitEvent(ericFlores, 0));
        table.pushEvent(new CommitEvent(adamBroker, 85));
        table.pushEvent(new CommitEvent(carryDavis, 85));
        table.pushEvent(new CommitEvent(ericFlores, 170));
        table.pushEvent(new CommitEvent(adamBroker, 85));
        table.pushEvent(new CommitEvent(carryDavis, 85));
        table.pushEvent(new CommitEvent(carryDavis, 0));
        table.pushEvent(new CommitEvent(ericFlores, 795));
        table.pushEvent(new CommitEvent(adamBroker, 795));
        table.pushEvent(new FoldEvent(carryDavis));

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
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(500, 1000, 5, 10);
        final Player adamBroker = new Player("Adam Broker", 2000);
        final Player carryDavis = new Player("Carry Davis", 1000);

        table.pushEvent(new JoinEvent(adamBroker, 600, 0));
        table.pushEvent(new JoinEvent(carryDavis, 500, 1));
        table.pushEvent(new DealEvent(0));
        table.pushEvent(new CommitEvent(carryDavis, 5));
        table.pushEvent(new CommitEvent(adamBroker, 10));
        table.pushEvent(new CommitEvent(carryDavis, 200));
        table.pushEvent(new CommitEvent(adamBroker, 550));
        table.pushEvent(new CommitEvent(carryDavis, 295));

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
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(500, 1000, 5,10);
        final Player adamBroker = new Player("Adam Broker", 2000);
        final Player carryDavis = new Player("Carry Davis", 1000);
        final Player ericFlores = new Player("Eric Flores", 1000);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 1000, 1));
        table.pushEvent(new JoinEvent(ericFlores, 1000, 2));
        table.pushEvent(new SitOutEvent(carryDavis, true));
        table.pushEvent(new DealEvent(0));
        table.pushEvent(new CommitEvent(ericFlores, 5));
        table.pushEvent(new CommitEvent(adamBroker, 10));
        table.pushEvent(new CommitEvent(ericFlores, 5));
        table.pushEvent(new CommitEvent(adamBroker, 0));
        table.pushEvent(new CommitEvent(ericFlores, 0));
        table.pushEvent(new CommitEvent(adamBroker, 15));
        table.pushEvent(new CommitEvent(ericFlores, 15));
        table.pushEvent(new CommitEvent(ericFlores, 0));
        table.pushEvent(new CommitEvent(adamBroker, 40));
        table.pushEvent(new CommitEvent(ericFlores, 40));
        table.pushEvent(new CommitEvent(ericFlores, 0));
        table.pushEvent(new CommitEvent(adamBroker, 0));

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
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(500, 1000, 5,10);
        final Player adamBroker = new Player("Adam Broker", 2000);
        final Player carryDavis = new Player("Carry Davis", 1000);
        final Player ericFlores = new Player("Eric Flores", 1000);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 1000, 1));
        table.pushEvent(new JoinEvent(ericFlores, 1000, 2));
        table.pushEvent(new SitOutEvent(carryDavis, true));
        table.pushEvent(new SitOutEvent(ericFlores, true));

        table.handleEventQueue();

        try {
            table.pushEvent(new DealEvent(0));
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
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(500, 1000, 5,10);
        final Player adamBroker = new Player("Adam Broker", 2000);
        final Player carryDavis = new Player("Carry Davis", 1000);
        final Player ericFlores = new Player("Eric Flores", 1000);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 1000, 1));
        table.pushEvent(new JoinEvent(ericFlores, 1000, 2));
        table.pushEvent(new SitOutEvent(carryDavis, true));
        table.pushEvent(new SitOutEvent(ericFlores, true));
        table.pushEvent(new SitOutEvent(carryDavis, false));
        table.pushEvent(new DealEvent(0));
        table.pushEvent(new CommitEvent(carryDavis, 5));
        table.pushEvent(new CommitEvent(adamBroker, 10));
        table.pushEvent(new CommitEvent(carryDavis, 995));
        table.pushEvent(new FoldEvent(adamBroker));

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
