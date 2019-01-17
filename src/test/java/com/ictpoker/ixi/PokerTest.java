package com.ictpoker.ixi;

import org.junit.Assert;
import org.junit.Test;

public class PokerTest {

    @Test
    public void testHand1() {

        try {
            final Table table = new Table(4, new TexasHoldemDealer(Dealer.DEFAULT_DEALER_SPEED, 1000));
            final Player playerA = new Player("Adam Broker", 2000);
            final Player playerB = new Player("Carry Davis", 1000);
            final Player playerC = new Player("Eric Flores", 1000);

            table.pushEvent(new PlayerEvent(playerA, PlayerEvent.PlayerAction.JOIN, 1000));
            table.pushEvent(new PlayerEvent(playerB, PlayerEvent.PlayerAction.JOIN, 1000));
            table.pushEvent(new PlayerEvent(playerC, PlayerEvent.PlayerAction.JOIN, 1000));

            table.getState().setButtonPosition(0);

            try {
                table.handleEventQueue();
            } catch (PlayerEventException e) {
                Assert.fail("Three distinct players should be able to join a table");
            }

            table.getDealer().dealHoleCards(table);

            table.pushEvent(new PlayerEvent(playerA, PlayerEvent.PlayerAction.JOIN, 1000));


        } catch (Table.InvalidSeatCountException e) {
            e.printStackTrace();
            Assert.fail("Unexpectedly failed to create table");
        } catch (Dealer.DealerException e) {
            e.printStackTrace();
            Assert.fail("Unexpectedly failed to initialize Dealer");
        } catch (PlayerEventException e) {
            e.printStackTrace();
            Assert.fail("Unexpectedly failed to create player events");
        }
    }
}
