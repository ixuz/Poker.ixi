package com.ictpoker.ixi;

import org.junit.Assert;
import org.junit.Test;

public class DealerTest {

    @Test
    public void testPlayerJoin() {

        try {
            final Table table = new Table(4);
            final TexasHoldemDealer dealer = new TexasHoldemDealer(table, TexasHoldemDealer.DEFAULT_DEALER_SPEED);
            final Player player = new Player("Adam Broker", 1000);
            dealer.run();

            dealer.pushPlayerEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN, e -> {
                System.out.println("Callback called");
                Assert.assertNull(e);
            }));
/*
            synchronized (waitForCallback) {
                try {
                    waitForCallback.wait();
                } catch (InterruptedException e) {
                    Assert.fail("Interrupted before playerEventCallback received");
                }
            }*/
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        }
    }

    @Test
    public void testPlayerLeave() {

        try {
            final Table table = new Table(4);
            final TexasHoldemDealer dealer = new TexasHoldemDealer(table, TexasHoldemDealer.DEFAULT_DEALER_SPEED);
            final Player player = new Player("Adam Broker", 1000);

            try {
                dealer.handlePlayerEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN));
                dealer.handlePlayerEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.LEAVE));
            } catch (PlayerEventException e) {
                Assert.fail("The player should be able to both join and leave a table.");
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        }
    }

    @Test
    public void testPlayerDoubleJoin() {

        try {
            final Table table = new Table(4);
            final TexasHoldemDealer dealer = new TexasHoldemDealer(table, TexasHoldemDealer.DEFAULT_DEALER_SPEED);
            final Player player = new Player("Adam Broker", 1000);

            try {
                dealer.handlePlayerEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN));
            } catch (PlayerEventException e) {
                Assert.fail("The player has should be able to join a table.");
            }

            try {
                dealer.handlePlayerEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN));
                Assert.fail("The player has already joined the table, therefore the player can't join again.");
            } catch (PlayerEventException e) {
                // Intended exception thrown, player should not be able to leave the table twice.
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        }
    }

    @Test
    public void testPlayerDoubleLeave() {

        try {
            final Table table = new Table(4);
            final TexasHoldemDealer dealer = new TexasHoldemDealer(table, TexasHoldemDealer.DEFAULT_DEALER_SPEED);
            final Player player = new Player("Adam Broker", 1000);

            try {
                dealer.handlePlayerEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN));
                dealer.handlePlayerEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.LEAVE));
            } catch (PlayerEventException e) {
                Assert.fail("The player should be able to both join and leave a table.");
            }

            try {
                dealer.handlePlayerEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.LEAVE));
                Assert.fail("The player has already left the table, therefore the player can't leave again.");
            } catch (PlayerEventException e) {
                // Intended exception thrown, player should not be able to leave the table twice.
            }

        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        }
    }
}
