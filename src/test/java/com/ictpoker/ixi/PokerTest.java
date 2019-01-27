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

public class PokerTest {

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.println("\nStarting test: " + description.getMethodName());
        }
    };

    @Test
    public void testHand1()
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(4, 500, 1000, 5,10);
        final Player adamBroker = new Player("Adam Broker", 2000);
        final Player carryDavis = new Player("Carry Davis", 1000);
        final Player ericFlores = new Player("Eric Flores", 1000);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 1000, 1));
        table.pushEvent(new JoinEvent(ericFlores, 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        table.pushEvent(new DealEvent());
        table.pushEvent(new SmallBlindEvent(carryDavis));
        table.pushEvent(new BigBlindEvent(ericFlores));
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
    }

    @Test
    public void testHand2()
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(4, 500, 1000, 5, 10);
        final Player adamBroker = new Player("Adam Broker", 2000);
        final Player carryDavis = new Player("Carry Davis", 1000);
        final Player ericFlores = new Player("Eric Flores", 1000);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 1000, 1));
        table.pushEvent(new JoinEvent(ericFlores, 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        table.pushEvent(new DealEvent());
        table.pushEvent(new SmallBlindEvent(carryDavis));
        table.pushEvent(new BigBlindEvent(ericFlores));
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
    }

    @Test
    public void testHand3()
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(4, 500, 1000, 5, 10);
        final Player adamBroker = new Player("Adam Broker", 1000);
        final Player carryDavis = new Player("Carry Davis", 500);
        final Player ericFlores = new Player("Eric Flores", 500);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 500, 1));
        table.pushEvent(new JoinEvent(ericFlores, 500, 2));
        table.pushEvent(new MoveButtonEvent(0));
        table.pushEvent(new DealEvent());
        table.pushEvent(new SmallBlindEvent(carryDavis));
        table.pushEvent(new BigBlindEvent(ericFlores));
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
    }

    @Test
    public void testHand4()
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(4, 500, 1000, 5, 10);
        final Player adamBroker = new Player("Adam Broker", 1000);
        final Player carryDavis = new Player("Carry Davis", 500);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 500, 1));
        table.pushEvent(new MoveButtonEvent(0));
        table.pushEvent(new DealEvent());
        table.pushEvent(new SmallBlindEvent(carryDavis));
        table.pushEvent(new BigBlindEvent(adamBroker));
        table.pushEvent(new FoldEvent(carryDavis));

        table.handleEventQueue();

        System.out.println(table.toString());

        Assert.assertEquals(995, table.getSeats().get(0).getStack());
        Assert.assertEquals(495, table.getSeats().get(1).getStack());
        Assert.assertEquals(5, table.getSeats().get(0).getCollected());
        Assert.assertEquals(5, table.getSeats().get(1).getCollected());
        Assert.assertEquals(10, table.getTotalPot());
    }

    @Test
    public void testHand5()
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(4, 500, 1000, 5, 10);
        final Player adamBroker = new Player("Adam Broker", 1000);
        final Player carryDavis = new Player("Carry Davis", 500);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 500, 1));
        table.pushEvent(new MoveButtonEvent(0));
        table.pushEvent(new DealEvent());
        table.pushEvent(new SmallBlindEvent(carryDavis));
        table.pushEvent(new BigBlindEvent(adamBroker));
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
    }

    @Test
    public void testHand6()
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(4, 500, 1000, 5,10);
        final Player adamBroker = new Player("Adam Broker", 2000);
        final Player carryDavis = new Player("Carry Davis", 1000);
        final Player ericFlores = new Player("Eric Flores", 1000);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 1000, 1));
        table.pushEvent(new JoinEvent(ericFlores, 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        table.pushEvent(new DealEvent());
        table.pushEvent(new SmallBlindEvent(carryDavis));
        table.pushEvent(new BigBlindEvent(ericFlores));
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
    }

    @Test
    public void testHand7()
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(4, 500, 1000, 5,10);
        final Player adamBroker = new Player("Adam Broker", 2000);
        final Player carryDavis = new Player("Carry Davis", 1000);
        final Player ericFlores = new Player("Eric Flores", 1000);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 1000, 1));
        table.pushEvent(new JoinEvent(ericFlores, 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        table.pushEvent(new DealEvent());
        table.pushEvent(new SmallBlindEvent(carryDavis));
        table.pushEvent(new BigBlindEvent(ericFlores));
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
    }

    @Test
    public void testHand8()
            throws TableException, TableStateException, TableEventException {

        final Table table = new Table(4, 500, 1000, 5,10);
        final Player adamBroker = new Player("Adam Broker", 2000);
        final Player carryDavis = new Player("Carry Davis", 1000);
        final Player ericFlores = new Player("Eric Flores", 1000);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 1000, 1));
        table.pushEvent(new JoinEvent(ericFlores, 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        table.pushEvent(new DealEvent());
        table.pushEvent(new SmallBlindEvent(carryDavis));
        table.pushEvent(new BigBlindEvent(ericFlores));
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
    }
}
