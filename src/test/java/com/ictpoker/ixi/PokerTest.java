package com.ictpoker.ixi;

import com.ictpoker.ixi.Dealer.DealerEvent.*;
import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Player.PlayerEvent.*;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableException.InvalidSeatCountException;
import com.ictpoker.ixi.Table.TableException.TableException;
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

            table.pushEvent(new JoinPlayerEvent(adamBroker, 1000, 0));
            table.pushEvent(new JoinPlayerEvent(carryDavis, 1000, 1));
            table.pushEvent(new JoinPlayerEvent(ericFlores, 1000, 2));
            table.pushEvent(new MoveButtonDealerEvent(0));
            table.pushEvent(new DealDealerEvent());
            table.pushEvent(new SmallBlindPlayerEvent(carryDavis));
            table.pushEvent(new BigBlindPlayerEvent(ericFlores));
            table.pushEvent(new CallPlayerEvent(adamBroker));
            table.pushEvent(new CallPlayerEvent(carryDavis));
            table.pushEvent(new CheckPlayerEvent(ericFlores));
            table.pushEvent(new CheckPlayerEvent(carryDavis));
            table.pushEvent(new CheckPlayerEvent(ericFlores));
            table.pushEvent(new CheckPlayerEvent(adamBroker));
            table.pushEvent(new CheckPlayerEvent(carryDavis));
            table.pushEvent(new CheckPlayerEvent(ericFlores));
            table.pushEvent(new CheckPlayerEvent(adamBroker));
            table.pushEvent(new CheckPlayerEvent(carryDavis));
            table.pushEvent(new CheckPlayerEvent(ericFlores));
            table.pushEvent(new CheckPlayerEvent(adamBroker));

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

            table.pushEvent(new JoinPlayerEvent(adamBroker, 1000, 0));
            table.pushEvent(new JoinPlayerEvent(carryDavis, 1000, 1));
            table.pushEvent(new JoinPlayerEvent(ericFlores, 1000, 2));
            table.pushEvent(new MoveButtonDealerEvent(0));
            table.pushEvent(new DealDealerEvent());
            table.pushEvent(new SmallBlindPlayerEvent(carryDavis));
            table.pushEvent(new BigBlindPlayerEvent(ericFlores));
            table.pushEvent(new RaisePlayerEvent(adamBroker, 40));
            table.pushEvent(new FoldPlayerEvent(carryDavis));
            table.pushEvent(new FoldPlayerEvent(ericFlores));

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

            table.pushEvent(new JoinPlayerEvent(adamBroker, 1000, 0));
            table.pushEvent(new JoinPlayerEvent(carryDavis, 500, 1));
            table.pushEvent(new JoinPlayerEvent(ericFlores, 500, 2));
            table.pushEvent(new MoveButtonDealerEvent(0));
            table.pushEvent(new DealDealerEvent());
            table.pushEvent(new SmallBlindPlayerEvent(carryDavis));
            table.pushEvent(new BigBlindPlayerEvent(ericFlores));
            table.pushEvent(new RaisePlayerEvent(adamBroker, 950));
            table.pushEvent(new CallPlayerEvent(carryDavis));
            table.pushEvent(new CallPlayerEvent(ericFlores));

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

        table.pushEvent(new JoinPlayerEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinPlayerEvent(carryDavis, 500, 1));
        table.pushEvent(new MoveButtonDealerEvent(0));
        table.pushEvent(new DealDealerEvent());
        table.pushEvent(new SmallBlindPlayerEvent(carryDavis));
        table.pushEvent(new BigBlindPlayerEvent(adamBroker));
        table.pushEvent(new FoldPlayerEvent(carryDavis));

        table.update();
    }

    @Test
    public void testHand5()
            throws InvalidSeatCountException, TableException, PlayerEventException {

        final Table table = new Table(4, 500, 1000, 5, 10);
        final Player adamBroker = new Player("Adam Broker", 1000);
        final Player carryDavis = new Player("Carry Davis", 500);

        table.pushEvent(new JoinPlayerEvent(adamBroker, 1000, 0));
        table.pushEvent(new JoinPlayerEvent(carryDavis, 500, 1));
        table.pushEvent(new MoveButtonDealerEvent(0));
        table.pushEvent(new DealDealerEvent());
        table.pushEvent(new SmallBlindPlayerEvent(carryDavis));
        table.pushEvent(new BigBlindPlayerEvent(adamBroker));
        table.pushEvent(new RaisePlayerEvent(carryDavis, 15));
        table.pushEvent(new RaisePlayerEvent(adamBroker, 20));
        table.pushEvent(new RaisePlayerEvent(carryDavis, 20));
        table.pushEvent(new RaisePlayerEvent(adamBroker, 100));
        table.pushEvent(new FoldPlayerEvent(carryDavis));

        table.update();

        System.out.println(table.toString());
    }
}
