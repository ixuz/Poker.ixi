package com.ictpoker.ixi;

import org.junit.Assert;
import org.junit.Test;

public class DealerTest {

    @Test
    public void testPlayerJoin() {

        try {
            final Table table = new Table(4, new TexasHoldemDealer(Dealer.DEFAULT_DEALER_SPEED, 1000));

            final Player player = new Player("Adam Broker", 1000);

            table.pushEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN, 1000));

            try {
                table.handleEventQueue();
            } catch (PlayerEventException e) {
                Assert.fail();
            }

            try {
                final Seat seat = table.getPlayerSeat(player);
                Assert.assertNotNull(seat);
            } catch (Table.PlayerNotSeatedException e) {
                Assert.fail("The player should be able to join a table");
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (Dealer.DealerException e) {
            Assert.fail("Unexpectedly failed to initialize Dealer");
        } catch (PlayerEventException e) {
            Assert.fail("Unexpectedly failed to create player events");
        }
    }

    @Test
    public void testPlayerLeave() {

        try {
            final Table table = new Table(4, new TexasHoldemDealer(Dealer.DEFAULT_DEALER_SPEED, 1000));

            final Player player = new Player("Adam Broker", 1000);

            table.pushEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN, 1000));
            table.pushEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.LEAVE, 0));

            try {
                table.handleEventQueue();
            } catch (PlayerEventException e) {
                Assert.fail("The player should be able to join and leave a table");
            }

            try {
                final Seat seat = table.getPlayerSeat(player);
                Assert.fail("No seat should be found for a player that has already left the table");
            } catch (Table.PlayerNotSeatedException e) {
                // Intended exception thrown, the player has already left the table
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (Dealer.DealerException e) {
            Assert.fail("Unexpectedly failed to initialize Dealer");
        } catch (PlayerEventException e) {
            Assert.fail("Unexpectedly failed to create player events");
        }
    }

    @Test
    public void testPlayerDoubleJoin() {

        try {
            final Table table = new Table(4, new TexasHoldemDealer(Dealer.DEFAULT_DEALER_SPEED, 1000));

            final Player player = new Player("Adam Broker", 1000);

            table.pushEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN, 500));
            table.pushEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN, 500));

            try {
                table.handleEventQueue();
                Assert.fail("The player should not be able to join a table twice");
            } catch (PlayerEventException e) {
                // Intended exception thrown, the player has already joined the table
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (Dealer.DealerException e) {
            Assert.fail("Unexpectedly failed to initialize Dealer");
        } catch (PlayerEventException e) {
            Assert.fail("Unexpectedly failed to create player events");
        }
    }

    @Test
    public void testPlayerDoubleLeave() {

        try {
            final Table table = new Table(4, new TexasHoldemDealer(Dealer.DEFAULT_DEALER_SPEED, 1000));

            final Player player = new Player("Adam Broker", 1000);

            table.pushEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN, 1000));
            table.pushEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.LEAVE, 0));
            table.pushEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.LEAVE, 0));

            try {
                table.handleEventQueue();
                Assert.fail("The player should not be able to leave a table twice");
            } catch (PlayerEventException e) {
                // Intended exception thrown, the player has already left the table
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (Dealer.DealerException e) {
            Assert.fail("Unexpectedly failed to initialize Dealer");
        } catch (PlayerEventException e) {
            Assert.fail("Unexpectedly failed to create player events");
        }
    }

    @Test
    public void testPlayerInsufficientFunds() throws InterruptedException {

        try {
            final Table table = new Table(4, new TexasHoldemDealer(Dealer.DEFAULT_DEALER_SPEED, 1000));

            final Player player = new Player("Adam Broker", 200);

            try {
                table.pushEvent(new PlayerEvent(player, PlayerEvent.PlayerAction.JOIN, 1000));
            } catch (PlayerEventException e) {
                // Intended exception thrown, the player has insufficient funds
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (Dealer.DealerException e) {
            Assert.fail("Unexpectedly failed to initialize Dealer");
        }
    }

    @Test
    public void testDealHoleCards() {

        try {
            final Table table = new Table(4, new TexasHoldemDealer(Dealer.DEFAULT_DEALER_SPEED, 1000));

            final Player playerA = new Player("Adam Broker", 1000);
            final Player playerB = new Player("Carry Davis", 1000);
            final Player playerC = new Player("Eric Flores", 1000);

            table.pushEvent(new PlayerEvent(playerA, PlayerEvent.PlayerAction.JOIN, 1000));
            table.pushEvent(new PlayerEvent(playerB, PlayerEvent.PlayerAction.JOIN, 1000));
            table.pushEvent(new PlayerEvent(playerC, PlayerEvent.PlayerAction.JOIN, 1000));

            try {
                table.handleEventQueue();
            } catch (PlayerEventException e) {
                Assert.fail("Three distinct players should be able to join a table");
            }

            table.getDealer().dealHoleCards(table);

            try {
                Assert.assertEquals(table.getPlayerSeat(playerA).getCards().size(), TexasHoldemDealer.CARDS_PER_SEAT);
                Assert.assertEquals(table.getPlayerSeat(playerB).getCards().size(), TexasHoldemDealer.CARDS_PER_SEAT);
                Assert.assertEquals(table.getPlayerSeat(playerC).getCards().size(), TexasHoldemDealer.CARDS_PER_SEAT);
            } catch (Table.PlayerNotSeatedException e) {
                Assert.fail("Failed to verify that each player got the correct amount of cards");
            }

            Assert.assertEquals("Incorrect amount of cards remains in deck after dealHoleCards",
                    table.getDealer().getDeck().size(),
                    Deck.DECK_SIZE - table.getNumberOfSeatedPlayers() * TexasHoldemDealer.CARDS_PER_SEAT);
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (Dealer.DealerException e) {
            Assert.fail("Unexpectedly failed to initialize Dealer");
        } catch (PlayerEventException e) {
            Assert.fail("Unexpectedly failed to create player events");
        }
    }

    @Test
    public void testDealFlopTurnRiver() {

        try {
            final Table table = new Table(4, new TexasHoldemDealer(Dealer.DEFAULT_DEALER_SPEED, 1000));

            Assert.assertEquals(table.getDealer().getDeck().size(), Deck.DECK_SIZE);

            table.getDealer().dealFlop(table);
            Assert.assertEquals(table.getDealer().getDeck().size(),
                    Deck.DECK_SIZE - TexasHoldemDealer.CARDS_FLOP);

            table.getDealer().dealTurn(table);
            Assert.assertEquals(table.getDealer().getDeck().size(),
                    Deck.DECK_SIZE - TexasHoldemDealer.CARDS_FLOP - TexasHoldemDealer.CARDS_TURN);

            table.getDealer().dealRiver(table);
            Assert.assertEquals(table.getDealer().getDeck().size(),
                    Deck.DECK_SIZE - TexasHoldemDealer.CARDS_FLOP - TexasHoldemDealer.CARDS_TURN - TexasHoldemDealer.CARDS_RIVER);

            table.getDealer().cleanUp(table);
            Assert.assertEquals(table.getDealer().getDeck().size(), Deck.DECK_SIZE);

        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (Dealer.DealerException e) {
            Assert.fail("Unexpectedly failed to initialize Dealer");
        }
    }

    @Test
    public void testDealPlayersFlopTurnRiver() {

        try {
            final Table table = new Table(4, new TexasHoldemDealer(Dealer.DEFAULT_DEALER_SPEED, 1000));

            final Player playerA = new Player("Adam Broker", 1000);
            final Player playerB = new Player("Carry Davis", 1000);
            final Player playerC = new Player("Eric Flores", 1000);

            table.pushEvent(new PlayerEvent(playerA, PlayerEvent.PlayerAction.JOIN, 1000));
            table.pushEvent(new PlayerEvent(playerB, PlayerEvent.PlayerAction.JOIN, 1000));
            table.pushEvent(new PlayerEvent(playerC, PlayerEvent.PlayerAction.JOIN, 1000));

            try {
                table.handleEventQueue();
            } catch (PlayerEventException e) {
                Assert.fail("Three distinct players should be able to join a table");
            }

            table.getDealer().dealHoleCards(table);

            Assert.assertEquals("Incorrect amount of cards remains in deck after dealHoleCards",
                    table.getDealer().getDeck().size(),
                    Deck.DECK_SIZE - table.getNumberOfSeatedPlayers() * TexasHoldemDealer.CARDS_PER_SEAT);

            Assert.assertEquals(table.getDealer().getDeck().size(),
                    Deck.DECK_SIZE - table.getNumberOfSeatedPlayers() * TexasHoldemDealer.CARDS_PER_SEAT);

            table.getDealer().dealFlop(table);
            Assert.assertEquals(table.getDealer().getDeck().size(),
                    Deck.DECK_SIZE - table.getNumberOfSeatedPlayers() * TexasHoldemDealer.CARDS_PER_SEAT - TexasHoldemDealer.CARDS_FLOP);

            table.getDealer().dealTurn(table);
            Assert.assertEquals(table.getDealer().getDeck().size(),
                    Deck.DECK_SIZE - table.getNumberOfSeatedPlayers() * TexasHoldemDealer.CARDS_PER_SEAT - TexasHoldemDealer.CARDS_FLOP - TexasHoldemDealer.CARDS_TURN);

            table.getDealer().dealRiver(table);
            Assert.assertEquals(table.getDealer().getDeck().size(),
                    Deck.DECK_SIZE - table.getNumberOfSeatedPlayers() * TexasHoldemDealer.CARDS_PER_SEAT - TexasHoldemDealer.CARDS_FLOP - TexasHoldemDealer.CARDS_TURN - TexasHoldemDealer.CARDS_RIVER);

            table.getDealer().cleanUp(table);
            Assert.assertEquals(table.getDealer().getDeck().size(), Deck.DECK_SIZE);
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (Dealer.DealerException e) {
            Assert.fail("Unexpectedly failed to initialize Dealer");
        } catch (PlayerEventException e) {
            Assert.fail("Unexpectedly failed to create player events");
        }
    }
}
