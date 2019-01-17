package com.ictpoker.ixi;

import com.ictpoker.ixi.Dealer.Dealer;
import com.ictpoker.ixi.Dealer.DealerEvent.*;
import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Player.PlayerEvent.PlayerEvent;
import com.ictpoker.ixi.Player.PlayerEvent.PlayerEventException;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableException.InvalidSeatCountException;
import org.junit.Assert;
import org.junit.Test;

public class PokerTest {

    @Test
    public void testHand1() {

        try {
            final Table table = new Table(4, 500, 1000, new Dealer(Dealer.DEFAULT_DEALER_SPEED));
            final Player playerA = new Player("Adam Broker", 2000);
            final Player playerB = new Player("Carry Davis", 1000);
            final Player playerC = new Player("Eric Flores", 1000);

            table.pushEvent(new PlayerEvent(playerA, PlayerEvent.PlayerAction.JOIN, 1000));
            table.pushEvent(new PlayerEvent(playerB, PlayerEvent.PlayerAction.JOIN, 1000));
            table.pushEvent(new PlayerEvent(playerC, PlayerEvent.PlayerAction.JOIN, 1000));
            table.pushEvent(new MoveButtonDealerEvent(0));
            table.pushEvent(new DealDealerEvent());
            table.pushEvent(new SmallBlindDealerEvent());
            table.pushEvent(new BigBlindDealerEvent());
            table.pushEvent(new PlayerEvent(playerA, PlayerEvent.PlayerAction.CALL, 20));
            table.pushEvent(new PlayerEvent(playerB, PlayerEvent.PlayerAction.CALL, 10));
            table.pushEvent(new PlayerEvent(playerC, PlayerEvent.PlayerAction.CHECK, 0));
            table.pushEvent(new PlayerEvent(playerB, PlayerEvent.PlayerAction.CHECK, 0));
            table.pushEvent(new PlayerEvent(playerC, PlayerEvent.PlayerAction.CHECK, 0));
            table.pushEvent(new PlayerEvent(playerA, PlayerEvent.PlayerAction.CHECK, 0));
            table.pushEvent(new PlayerEvent(playerB, PlayerEvent.PlayerAction.CHECK, 0));
            table.pushEvent(new PlayerEvent(playerC, PlayerEvent.PlayerAction.CHECK, 0));
            table.pushEvent(new PlayerEvent(playerA, PlayerEvent.PlayerAction.CHECK, 0));
            table.pushEvent(new PlayerEvent(playerB, PlayerEvent.PlayerAction.CHECK, 0));
            table.pushEvent(new PlayerEvent(playerC, PlayerEvent.PlayerAction.CHECK, 0));
            table.pushEvent(new PlayerEvent(playerA, PlayerEvent.PlayerAction.CHECK, 0));

            table.handleEventQueue();
        } catch (InvalidSeatCountException e) {
            e.printStackTrace();
            Assert.fail("Unexpectedly failed to create table");
        } catch (PlayerEventException e) {
            e.printStackTrace();
            Assert.fail("Unexpected player exception");
        } catch (DealerEventException e) {
            e.printStackTrace();
            Assert.fail("Unexpected dealer exception");
        }
    }

    @Test
    public void testHand2() {

        try {
            final Table table = new Table(4, 500, 1000, new Dealer(Dealer.DEFAULT_DEALER_SPEED));
            final Player playerA = new Player("Adam Broker", 2000);
            final Player playerB = new Player("Carry Davis", 1000);
            final Player playerC = new Player("Eric Flores", 1000);

            table.pushEvent(new PlayerEvent(playerA, PlayerEvent.PlayerAction.JOIN, 1000));
            table.pushEvent(new PlayerEvent(playerB, PlayerEvent.PlayerAction.JOIN, 1000));
            table.pushEvent(new PlayerEvent(playerC, PlayerEvent.PlayerAction.JOIN, 1000));
            table.pushEvent(new MoveButtonDealerEvent(0));
            table.pushEvent(new DealDealerEvent());
            table.pushEvent(new SmallBlindDealerEvent());
            table.pushEvent(new BigBlindDealerEvent());
            table.pushEvent(new PlayerEvent(playerA, PlayerEvent.PlayerAction.RAISE, 40));
            table.pushEvent(new PlayerEvent(playerB, PlayerEvent.PlayerAction.FOLD, 0));
            table.pushEvent(new PlayerEvent(playerC, PlayerEvent.PlayerAction.FOLD, 0));

            table.handleEventQueue();
        } catch (InvalidSeatCountException e) {
            e.printStackTrace();
            Assert.fail("Unexpectedly failed to create table");
        } catch (PlayerEventException e) {
            e.printStackTrace();
            Assert.fail("Unexpected player exception");
        } catch (DealerEventException e) {
            e.printStackTrace();
            Assert.fail("Unexpected dealer exception");
        }
    }
}
