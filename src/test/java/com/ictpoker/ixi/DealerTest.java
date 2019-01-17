package com.ictpoker.ixi;

import com.ictpoker.ixi.DealerEvent.DealerEventException;
import org.junit.Assert;
import org.junit.Test;

public class DealerTest {

    @Test
    public void testPlayerJoin() {

        try {
            final Table table = new Table(4, 500, 1000, new Dealer(Dealer.DEFAULT_DEALER_SPEED));

            final Player player = new Player("Adam Broker", 1000);

            table.pushEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN, 1000));

            table.handleEventQueue();

            try {
                final Seat seat = table.getPlayerSeat(player);
                Assert.assertNotNull(seat);
            } catch (Table.PlayerNotSeatedException e) {
                Assert.fail("The player should be able to join a table");
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (PlayerEventException e) {
            Assert.fail("Unexpectedly failed to create player events");
        } catch (DealerEventException e) {
            Assert.fail("Unexpected dealer exception");
        }
    }

    @Test
    public void testPlayerLeave() {

        try {
            final Table table = new Table(4, 500, 1000, new Dealer(Dealer.DEFAULT_DEALER_SPEED));

            final Player player = new Player("Adam Broker", 1000);

            table.pushEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN, 1000));
            table.pushEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.LEAVE, 0));

            table.handleEventQueue();

            try {
                final Seat seat = table.getPlayerSeat(player);
                Assert.fail("No seat should be found for a player that has already left the table");
            } catch (Table.PlayerNotSeatedException e) {
                // Intended exception thrown, the player has already left the table
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (PlayerEventException e) {
            Assert.fail("Unexpectedly failed to create player events");
        } catch (DealerEventException e) {
            Assert.fail("Unexpected dealer exception");
        }
    }

    @Test
    public void testPlayerDoubleJoin() {

        try {
            final Table table = new Table(4, 500, 1000, new Dealer(Dealer.DEFAULT_DEALER_SPEED));

            final Player player = new Player("Adam Broker", 1000);

            table.pushEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN, 500));
            table.pushEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN, 500));

            try {
                table.handleEventQueue();
                Assert.fail("The player should not be able to join a table twice");
            } catch (com.ictpoker.ixi.PlayerEventException e) {
                // Intended exception thrown, the player has already joined the table
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (PlayerEventException e) {
            Assert.fail("Unexpectedly failed to create player events");
        } catch (DealerEventException e) {
            Assert.fail("Unexpected dealer exception");
        }
    }

    @Test
    public void testPlayerDoubleLeave() {

        try {
            final Table table = new Table(4, 500, 1000, new Dealer(Dealer.DEFAULT_DEALER_SPEED));

            final Player player = new Player("Adam Broker", 1000);

            table.pushEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN, 1000));
            table.pushEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.LEAVE, 0));
            table.pushEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.LEAVE, 0));

            try {
                table.handleEventQueue();
                Assert.fail("The player should not be able to leave a table twice");
            } catch (com.ictpoker.ixi.PlayerEventException e) {
                // Intended exception thrown, the player has already left the table
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (PlayerEventException e) {
            Assert.fail("Unexpectedly failed to create player events");
        } catch (DealerEventException e) {
            Assert.fail("Unexpected dealer exception");
        }
    }

    @Test
    public void testPlayerInsufficientFunds() {

        try {
            final Table table = new Table(4, 500, 1000, new Dealer(Dealer.DEFAULT_DEALER_SPEED));

            final Player player = new Player("Adam Broker", 200);

            try {
                table.pushEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN, 1000));
            } catch (com.ictpoker.ixi.PlayerEventException e) {
                // Intended exception thrown, the player has insufficient funds
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (PlayerEventException e) {
            Assert.fail("Unexpected dealer exception");
        }
    }

    @Test
    public void testDealHoleCards() {

        try {
            final Table table = new Table(4, 500, 1000, new Dealer(Dealer.DEFAULT_DEALER_SPEED));

            final Player playerA = new Player("Adam Broker", 1000);
            final Player playerB = new Player("Carry Davis", 1000);
            final Player playerC = new Player("Eric Flores", 1000);

            table.pushEvent(new PlayerEvent(playerA, PlayerEvent.PlayerAction.JOIN, 1000));
            table.pushEvent(new PlayerEvent(playerB, PlayerEvent.PlayerAction.JOIN, 1000));
            table.pushEvent(new PlayerEvent(playerC, PlayerEvent.PlayerAction.JOIN, 1000));

            table.handleEventQueue();

            table.getDealer().dealHoleCards(table);

            try {
                Assert.assertEquals(table.getPlayerSeat(playerA).getCards().size(), Dealer.CARDS_PER_SEAT);
                Assert.assertEquals(table.getPlayerSeat(playerB).getCards().size(), Dealer.CARDS_PER_SEAT);
                Assert.assertEquals(table.getPlayerSeat(playerC).getCards().size(), Dealer.CARDS_PER_SEAT);
            } catch (Table.PlayerNotSeatedException e) {
                Assert.fail("Failed to verify that each player got the correct amount of cards");
            }

            Assert.assertEquals("Incorrect amount of cards remains in deck after dealHoleCards",
                    table.getDealer().getDeck().size(),
                    Deck.DECK_SIZE - table.getNumberOfSeatedPlayers() * Dealer.CARDS_PER_SEAT);
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (PlayerEventException e) {
            Assert.fail("Unexpectedly failed to create player events");
        } catch (DealerEventException e) {
            Assert.fail("Unexpected dealer exception");
        }
    }

    @Test
    public void testDealFlopTurnRiver() {

        try {
            final Table table = new Table(4, 500, 1000, new Dealer(Dealer.DEFAULT_DEALER_SPEED));

            Assert.assertEquals(table.getDealer().getDeck().size(), Deck.DECK_SIZE);

            table.getDealer().dealFlop(table);
            Assert.assertEquals(Deck.DECK_SIZE - Dealer.CARDS_FLOP,
                    table.getDealer().getDeck().size());

            table.getDealer().dealTurn(table);
            Assert.assertEquals(Deck.DECK_SIZE - Dealer.CARDS_FLOP - Dealer.CARDS_TURN,
                    table.getDealer().getDeck().size());

            table.getDealer().dealRiver(table);
            Assert.assertEquals(Deck.DECK_SIZE - Dealer.CARDS_FLOP - Dealer.CARDS_TURN - Dealer.CARDS_RIVER,
                    table.getDealer().getDeck().size());

            table.getDealer().cleanUp(table);
            Assert.assertEquals(table.getDealer().getDeck().size(), Deck.DECK_SIZE);

        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (PlayerEventException e) {
            Assert.fail("Unexpectedly failed to create player events");
        }
    }

    @Test
    public void testDealPlayersFlopTurnRiver() {

        try {
            final Table table = new Table(4, 500, 1000, new Dealer(Dealer.DEFAULT_DEALER_SPEED));

            final Player playerA = new Player("Adam Broker", 1000);
            final Player playerB = new Player("Carry Davis", 1000);
            final Player playerC = new Player("Eric Flores", 1000);

            table.pushEvent(new PlayerEvent(playerA, PlayerEvent.PlayerAction.JOIN, 1000));
            table.pushEvent(new PlayerEvent(playerB, PlayerEvent.PlayerAction.JOIN, 1000));
            table.pushEvent(new PlayerEvent(playerC, PlayerEvent.PlayerAction.JOIN, 1000));

            table.handleEventQueue();

            table.getDealer().dealHoleCards(table);

            Assert.assertEquals("Incorrect amount of cards remains in deck after dealHoleCards",
                    table.getDealer().getDeck().size(),
                    Deck.DECK_SIZE - table.getNumberOfSeatedPlayers() * Dealer.CARDS_PER_SEAT);

            Assert.assertEquals(table.getDealer().getDeck().size(),
                    Deck.DECK_SIZE - table.getNumberOfSeatedPlayers() * Dealer.CARDS_PER_SEAT);

            table.getDealer().dealFlop(table);
            Assert.assertEquals(table.getDealer().getDeck().size(),
                    Deck.DECK_SIZE - table.getNumberOfSeatedPlayers() * Dealer.CARDS_PER_SEAT - Dealer.CARDS_FLOP);

            table.getDealer().dealTurn(table);
            Assert.assertEquals(table.getDealer().getDeck().size(),
                    Deck.DECK_SIZE - table.getNumberOfSeatedPlayers() * Dealer.CARDS_PER_SEAT - Dealer.CARDS_FLOP - Dealer.CARDS_TURN);

            table.getDealer().dealRiver(table);
            Assert.assertEquals(table.getDealer().getDeck().size(),
                    Deck.DECK_SIZE - table.getNumberOfSeatedPlayers() * Dealer.CARDS_PER_SEAT - Dealer.CARDS_FLOP - Dealer.CARDS_TURN - Dealer.CARDS_RIVER);

            table.getDealer().cleanUp(table);
            Assert.assertEquals(table.getDealer().getDeck().size(), Deck.DECK_SIZE);
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (PlayerEventException e) {
            Assert.fail("Unexpectedly failed to create player events");
        } catch (DealerEventException e) {
            Assert.fail("Unexpected dealer exception");
        }
    }
}
