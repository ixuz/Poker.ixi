package com.ictpoker.ixi;

import com.sun.istack.internal.NotNull;
import org.junit.Assert;
import org.junit.Test;

public class DealerTest {

    @Test
    public void testPlayerJoin() {

        try {
            final Table table = new Table(4);
            final TexasHoldemDealer dealer = new TexasHoldemDealer(table,
                    TexasHoldemDealer.DEFAULT_DEALER_SPEED,
                    1000);
            dealer.start();

            final Player player = new Player("Adam Broker", 1000);

            final Object waitForCallback = new Object();

            dealer.pushPlayerEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN, e -> {
                synchronized (waitForCallback) {
                    waitForCallback.notify();
                    Assert.assertNull("The player should be able to join a table", e);
                }
            }));

            synchronized (waitForCallback) {
                try {
                    waitForCallback.wait();
                } catch (InterruptedException e) {
                    Assert.fail("Interrupted before playerEventCallback received");
                }
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        }
    }

    @Test
    public void testPlayerLeave() {

        try {
            final Table table = new Table(4);
            final TexasHoldemDealer dealer = new TexasHoldemDealer(table,
                    TexasHoldemDealer.DEFAULT_DEALER_SPEED,
                    1000);
            dealer.start();

            final Player player = new Player("Adam Broker", 1000);

            {
                final Object waitForCallback = new Object();

                dealer.pushPlayerEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN, e -> {
                    synchronized (waitForCallback) {
                        waitForCallback.notify();
                        Assert.assertNull("The player should be able to join a table", e);
                    }
                }));

                synchronized (waitForCallback) {
                    try {
                        waitForCallback.wait();
                    } catch (InterruptedException e) {
                        Assert.fail("Interrupted before playerEventCallback received");
                    }
                }
            }

            {
                final Object waitForCallback = new Object();

                dealer.pushPlayerEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.LEAVE, e -> {
                    synchronized (waitForCallback) {
                        waitForCallback.notify();
                        Assert.assertNull("The player should be able to leave a table", e);
                    }
                }));

                synchronized (waitForCallback) {
                    try {
                        waitForCallback.wait();
                    } catch (InterruptedException e) {
                        Assert.fail("Interrupted before playerEventCallback received");
                    }
                }
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        }
    }

    @Test
    public void testPlayerDoubleJoin() {

        try {
            final Table table = new Table(4);
            final TexasHoldemDealer dealer = new TexasHoldemDealer(table,
                    TexasHoldemDealer.DEFAULT_DEALER_SPEED,
                    1000);
            dealer.start();

            final Player player = new Player("Adam Broker", 1000);

            {
                final Object waitForCallback = new Object();

                dealer.pushPlayerEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN, e -> {
                    synchronized (waitForCallback) {
                        waitForCallback.notify();
                        Assert.assertNull("The player should be able to join a table", e);
                    }
                }));

                synchronized (waitForCallback) {
                    try {
                        waitForCallback.wait();
                    } catch (InterruptedException e) {
                        Assert.fail("Interrupted before playerEventCallback received");
                    }
                }
            }

            {
                final Object waitForCallback = new Object();

                dealer.pushPlayerEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN, e -> {
                    synchronized (waitForCallback) {
                        waitForCallback.notify();
                        Assert.assertNotNull("The player has already joined the table, " +
                                "therefore the player can't join again", e);
                    }
                }));

                synchronized (waitForCallback) {
                    try {
                        waitForCallback.wait();
                    } catch (InterruptedException e) {
                        Assert.fail("Interrupted before playerEventCallback received");
                    }
                }
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        }
    }

    @Test
    public void testPlayerDoubleLeave() {

        try {
            final Table table = new Table(4);
            final TexasHoldemDealer dealer = new TexasHoldemDealer(table,
                    TexasHoldemDealer.DEFAULT_DEALER_SPEED,
                    1000);
            dealer.start();

            final Player player = new Player("Adam Broker", 1000);

            {
                final Object waitForCallback = new Object();

                dealer.pushPlayerEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN, e -> {
                    synchronized (waitForCallback) {
                        waitForCallback.notify();
                        Assert.assertNull("The player should be able to join a table", e);
                    }
                }));

                synchronized (waitForCallback) {
                    try {
                        waitForCallback.wait();
                    } catch (InterruptedException e) {
                        Assert.fail("Interrupted before playerEventCallback received");
                    }
                }
            }

            {
                final Object waitForCallback = new Object();

                dealer.pushPlayerEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.LEAVE, e -> {
                    synchronized (waitForCallback) {
                        waitForCallback.notify();
                        Assert.assertNull("The player should be able to leave a table", e);
                    }
                }));

                synchronized (waitForCallback) {
                    try {
                        waitForCallback.wait();
                    } catch (InterruptedException e) {
                        Assert.fail("Interrupted before playerEventCallback received");
                    }
                }
            }

            {
                final Object waitForCallback = new Object();

                dealer.pushPlayerEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.LEAVE, e -> {
                    synchronized (waitForCallback) {
                        waitForCallback.notify();
                        Assert.assertNotNull("The player has already left the table, " +
                                "therefore the player can't leave again.", e);
                    }
                }));

                synchronized (waitForCallback) {
                    try {
                        waitForCallback.wait();
                    } catch (InterruptedException e) {
                        Assert.fail("Interrupted before playerEventCallback received");
                    }
                }
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        }
    }

    @Test
    public void testPlayerInsufficientFunds() {

        try {
            final Table table = new Table(4);
            final TexasHoldemDealer dealer = new TexasHoldemDealer(table,
                    TexasHoldemDealer.DEFAULT_DEALER_SPEED,
                    1000);
            dealer.start();

            final Player player = new Player("Adam Broker", 200);

            {
                final Object waitForCallback = new Object();

                dealer.pushPlayerEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN, e -> {
                    synchronized (waitForCallback) {
                        waitForCallback.notify();
                        Assert.assertNotNull("The player should not be able to join this table " +
                                "due to insufficient balance", e);
                    }
                }));

                synchronized (waitForCallback) {
                    try {
                        waitForCallback.wait();
                    } catch (InterruptedException e) {
                        Assert.fail("Interrupted before playerEventCallback received");
                    }
                }
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        }
    }

    @Test
    public void testDeal() {

        try {
            final Table table = new Table(4);
            final TexasHoldemDealer dealer = new TexasHoldemDealer(table,
                    TexasHoldemDealer.DEFAULT_DEALER_SPEED,
                    1000);
            dealer.start();

            final Player player = new Player("Adam Broker", 1000);

            final Object waitForCallback = new Object();

            dealer.pushPlayerEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN, e -> {
                synchronized (waitForCallback) {
                    waitForCallback.notify();
                    Assert.assertNull("The player should be able to join a table", e);
                }
            }));

            synchronized (waitForCallback) {
                try {
                    waitForCallback.wait();
                } catch (InterruptedException e) {
                    Assert.fail("Interrupted before playerEventCallback received");
                }
            }

            dealer.deal();

            try {
                final Seat seat = dealer.getTable().getPlayerSeat(player);
                Assert.assertNotNull(seat);
                Assert.assertEquals(seat.getCards().size(), TexasHoldemDealer.DEFAULT_CARDS_PER_SEAT);
                Assert.assertEquals(dealer.getDeck().size(), Deck.DECK_SIZE - dealer.getTable().getNumberOfSeatedPlayers() * TexasHoldemDealer.DEFAULT_CARDS_PER_SEAT);
            } catch (Table.PlayerNotSeatedException e) {
                Assert.fail("The player should be sitting at the table already");
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        }
    }
}
