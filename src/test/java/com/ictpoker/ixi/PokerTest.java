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
            final Player playerA = new Player("Adam Broker", 2000);
            final Player playerB = new Player("Carry Davis", 1000);
            final Player playerC = new Player("Eric Flores", 1000);

            table.pushEvent(new JoinPlayerEvent(playerA, 1000, 0));
            table.pushEvent(new JoinPlayerEvent(playerB, 1000, 1));
            table.pushEvent(new JoinPlayerEvent(playerC, 1000, 2));
            table.pushEvent(new MoveButtonDealerEvent(0));
            table.pushEvent(new DealDealerEvent());
            table.pushEvent(new SmallBlindPlayerEvent(playerB));
            table.pushEvent(new BigBlindPlayerEvent(playerC));
            table.pushEvent(new CallPlayerEvent(playerA));
            table.pushEvent(new CallPlayerEvent(playerB));
            table.pushEvent(new CheckPlayerEvent(playerC));
            table.pushEvent(new CheckPlayerEvent(playerB));
            table.pushEvent(new CheckPlayerEvent(playerC));
            table.pushEvent(new CheckPlayerEvent(playerA));
            table.pushEvent(new CheckPlayerEvent(playerB));
            table.pushEvent(new CheckPlayerEvent(playerC));
            table.pushEvent(new CheckPlayerEvent(playerA));
            table.pushEvent(new CheckPlayerEvent(playerB));
            table.pushEvent(new CheckPlayerEvent(playerC));
            table.pushEvent(new CheckPlayerEvent(playerA));

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
            final Player playerA = new Player("Adam Broker", 2000);
            final Player playerB = new Player("Carry Davis", 1000);
            final Player playerC = new Player("Eric Flores", 1000);

            table.pushEvent(new JoinPlayerEvent(playerA, 1000, 0));
            table.pushEvent(new JoinPlayerEvent(playerB, 1000, 1));
            table.pushEvent(new JoinPlayerEvent(playerC, 1000, 2));
            table.pushEvent(new MoveButtonDealerEvent(0));
            table.pushEvent(new DealDealerEvent());
            table.pushEvent(new SmallBlindPlayerEvent(playerB));
            table.pushEvent(new BigBlindPlayerEvent(playerC));
            table.pushEvent(new RaisePlayerEvent(playerA, 40));
            table.pushEvent(new FoldPlayerEvent(playerB));
            table.pushEvent(new FoldPlayerEvent(playerC));

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
            final Player playerA = new Player("Adam Broker", 1000);
            final Player playerB = new Player("Carry Davis", 500);
            final Player playerC = new Player("Eric Flores", 500);

            table.pushEvent(new JoinPlayerEvent(playerA, 1000, 0));
            table.pushEvent(new JoinPlayerEvent(playerB, 500, 1));
            table.pushEvent(new JoinPlayerEvent(playerC, 500, 2));
            table.pushEvent(new MoveButtonDealerEvent(0));
            table.pushEvent(new DealDealerEvent());
            table.pushEvent(new SmallBlindPlayerEvent(playerB));
            table.pushEvent(new BigBlindPlayerEvent(playerC));
            table.pushEvent(new RaisePlayerEvent(playerA, 950));
            table.pushEvent(new CallPlayerEvent(playerB));
            table.pushEvent(new CallPlayerEvent(playerC));

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
}
