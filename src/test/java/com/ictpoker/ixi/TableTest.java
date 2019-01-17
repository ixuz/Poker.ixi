package com.ictpoker.ixi;

import org.junit.Assert;
import org.junit.Test;

public class TableTest {

    @Test
    public void testInvalidSeatCounts() {

        try {
            new Table(0, new TexasHoldemDealer(Dealer.DEFAULT_DEALER_SPEED, 1000));
            Assert.fail("A table with zero seats is not allowed");
        } catch (Table.InvalidSeatCountException e) {
            // Intended exception thrown, a table with zero seats is not allowed
        } catch (Dealer.DealerException e) {
            Assert.fail("Unexpectedly failed to initialize Dealer");
        }

        try {
            new Table(Table.MAXIMUM_SEATS+1, new TexasHoldemDealer(Dealer.DEFAULT_DEALER_SPEED, 1000));
            Assert.fail("A table with more seats than the maximum amount is not allowed");
        } catch (Table.InvalidSeatCountException e) {
            // Intended exception thrown, a table with more seats than the maximum amount is not allowed
        } catch (Dealer.DealerException e) {
            Assert.fail("Unexpectedly failed to initialize Dealer");
        }

        try {
            new Table(-1, new TexasHoldemDealer(Dealer.DEFAULT_DEALER_SPEED, 1000));
            Assert.fail("A table with negative seat count doesn't make sense");
        } catch (Table.InvalidSeatCountException e) {
            // Intended exception thrown, a table with negative seats doesn't make sense
        } catch (Dealer.DealerException e) {
            Assert.fail("Unexpectedly failed to initialize Dealer");
        }
    }

    @Test
    public void testSingleJoin() {

        try {
            final Table table = new Table(2, new TexasHoldemDealer(Dealer.DEFAULT_DEALER_SPEED, 1000));

            try {
                table.join(new Player("Adam Broker", 1000), 1000);
            } catch (Exception e) {
                Assert.fail("Player unexpectedly failed to join table");
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (Dealer.DealerException e) {
            Assert.fail("Unexpectedly failed to initialize Dealer");
        }
    }

    @Test
    public void testDoubleJoin() {

        try {
            final Table table = new Table(2, new TexasHoldemDealer(Dealer.DEFAULT_DEALER_SPEED, 1000));

            final Player playerA = new Player("Adam Broker", 1000);

            try {
                table.join(playerA, 1000);
            } catch (Exception e) {
                Assert.fail("Player unexpectedly failed to join table");
            }

            try {
                table.join(playerA, 2000);
                Assert.fail("Player can't join same table twice");
            } catch (Table.PlayerAlreadySeatedException e) {
                // Intended exception thrown, player should not be able to join the same table twice.
            } catch (Exception e) {
                Assert.fail("Incorrect exception thrown");
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (Dealer.DealerException e) {
            Assert.fail("Unexpectedly failed to initialize Dealer");
        }
    }

    @Test
    public void testFullTable() {

        try {
            final Table table = new Table(2, new TexasHoldemDealer(Dealer.DEFAULT_DEALER_SPEED, 1000));

            try {
                table.join(new Player("Adam Broker", 1000), 1000);
                table.join(new Player("Carry Davis", 1000), 1000);
            } catch (Exception e) {
                Assert.fail("Incorrect exception thrown");
            }

            try {
                table.join(new Player("Eric Flores", 1000), 1000);
            } catch (Table.NoSeatAvailableException e) {
                // Intended exception thrown, the table is full
            } catch (Exception e) {
                Assert.fail("Incorrect exception thrown");
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (Dealer.DealerException e) {
            Assert.fail("Unexpectedly failed to initialize Dealer");
        }
    }

    @Test
    public void testFindPlayerSeat() {

        try {
            final Table table = new Table(2, new TexasHoldemDealer(Dealer.DEFAULT_DEALER_SPEED, 1000));

            final Player playerA = new Player("Adam Broker", 1000);

            if (table.isPlayerSeated(playerA)) {
                Assert.fail("Player has not joined the table yet");
            }

            try {
                table.join(playerA, 1000);

                if (!table.isPlayerSeated(playerA)) {
                    Assert.fail("Player has joined the table");
                }

            } catch (Exception e) {
                Assert.fail("Player unexpectedly failed to join table");
            }

            try {
                table.getPlayerSeat(playerA);
            } catch (Table.PlayerNotSeatedException e) {
                Assert.fail("Player was not found at the table");
            }

            try {
                table.getPlayerSeat(new Player("Carry Davis", 1000));
                Assert.fail("Player should not be found at the table.");
            } catch (Table.PlayerNotSeatedException e) {
                // Intended exception thrown, the player never joined the table
            }
        } catch (Table.InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (Dealer.DealerException e) {
            Assert.fail("Unexpectedly failed to initialize Dealer");
        }
    }
}
