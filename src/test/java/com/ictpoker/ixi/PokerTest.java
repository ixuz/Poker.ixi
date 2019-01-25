package com.ictpoker.ixi;

import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Player.PlayerEvent.*;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.Exception.InvalidSeatCountException;
import com.ictpoker.ixi.Table.Exception.TableException;
import com.ictpoker.ixi.Table.TableEvent.*;
import org.junit.Assert;
import org.junit.Test;

public class PokerTest {

    @Test
    public void testHand1() {

        try {
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
            table.pushEvent(new CallEvent(adamBroker));
            table.pushEvent(new CallEvent(carryDavis));
            table.pushEvent(new CheckEvent(ericFlores));
            table.pushEvent(new CheckEvent(carryDavis));
            table.pushEvent(new CheckEvent(ericFlores));
            table.pushEvent(new CheckEvent(adamBroker));
            table.pushEvent(new CheckEvent(carryDavis));
            table.pushEvent(new CheckEvent(ericFlores));
            table.pushEvent(new CheckEvent(adamBroker));
            table.pushEvent(new CheckEvent(carryDavis));
            table.pushEvent(new CheckEvent(ericFlores));
            table.pushEvent(new CheckEvent(adamBroker));

            table.update();
        } catch (InvalidSeatCountException e) {
            e.printStackTrace();
            Assert.fail("Unexpectedly failed to create table");
        } catch (PlayerEventException e) {
            e.printStackTrace();
            Assert.fail("Unexpected player exception");
        } catch (TableException e) {
            e.printStackTrace();
            Assert.fail("Unexpected table exception");
        }
    }

    @Test
    public void testHand2() {

        try {
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
            table.pushEvent(new RaiseEvent(adamBroker, 40));
            table.pushEvent(new FoldEvent(carryDavis));
            table.pushEvent(new FoldEvent(ericFlores));

            table.update();
        } catch (InvalidSeatCountException e) {
            e.printStackTrace();
            Assert.fail("Unexpectedly failed to create table");
        } catch (PlayerEventException e) {
            e.printStackTrace();
            Assert.fail("Unexpected player exception");
        } catch (TableException e) {
            Assert.fail("Unexpected table exception");
        }
    }

    @Test
    public void testHand3() {

        try {
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
            table.pushEvent(new RaiseEvent(adamBroker, 950));
            table.pushEvent(new CallEvent(carryDavis));
            table.pushEvent(new CallEvent(ericFlores));

            table.update();
        } catch (InvalidSeatCountException e) {
            e.printStackTrace();
            Assert.fail("Unexpectedly failed to create table");
        } catch (PlayerEventException e) {
            e.printStackTrace();
            Assert.fail("Unexpected player exception");
        } catch (TableException e) {
            Assert.fail("Unexpected table exception");
        }
    }

    @Test
    public void testHand4()
            throws InvalidSeatCountException, TableException, PlayerEventException {

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

        table.update();
    }

    @Test
    public void testHand5()
            throws InvalidSeatCountException, TableException, PlayerEventException {

        final Table table = new Table(4, 500, 1000, 5, 10);
        final Player adamBroker = new Player("Adam Broker", 1000);
        final Player carryDavis = new Player("Carry Davis", 500);

        table.pushEvent(new JoinEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinEvent(carryDavis, 500, 1));
        table.pushEvent(new MoveButtonEvent(0));
        table.pushEvent(new DealEvent());
        table.pushEvent(new SmallBlindEvent(carryDavis));
        table.pushEvent(new BigBlindEvent(adamBroker));
        table.pushEvent(new RaiseEvent(carryDavis, 15));
        table.pushEvent(new RaiseEvent(adamBroker, 20));
        table.pushEvent(new RaiseEvent(carryDavis, 20));
        table.pushEvent(new RaiseEvent(adamBroker, 100));
        table.pushEvent(new FoldEvent(carryDavis));

        table.update();

        System.out.println(table.toString());
    }
}
