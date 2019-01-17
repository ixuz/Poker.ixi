package com.ictpoker.ixi.Table;

import com.ictpoker.ixi.Dealer.Dealer;
import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Player.PlayerEvent.PlayerEventException;
import com.ictpoker.ixi.Table.TableException.InvalidSeatCountException;
import com.ictpoker.ixi.Table.TableException.NoSeatAvailableException;
import com.ictpoker.ixi.Table.TableException.PlayerAlreadySeatedException;
import com.ictpoker.ixi.Table.TableException.PlayerNotSeatedException;
import org.junit.Assert;
import org.junit.Test;

public class TableTest {

    @Test
    public void testInvalidSeatCounts() {

        try {
            new Table(0, 500, 1000, new Dealer(Dealer.DEFAULT_DEALER_SPEED));
            Assert.fail("A table with zero seats is not allowed");
        } catch (InvalidSeatCountException e) {
            // Intended exception thrown, a table with zero seats is not allowed
        } catch (PlayerEventException e) {
            Assert.fail("Unexpected dealer exception");
        }

        try {
            new Table(TableState.MAXIMUM_SEATS+1, 500, 1000, new Dealer(Dealer.DEFAULT_DEALER_SPEED));
            Assert.fail("A table with more seats than the maximum amount is not allowed");
        } catch (InvalidSeatCountException e) {
            // Intended exception thrown, a table with more seats than the maximum amount is not allowed
        } catch (PlayerEventException e) {
            Assert.fail("Unexpected dealer exception");
        }

        try {
            new Table(-1, 500, 1000, new Dealer(Dealer.DEFAULT_DEALER_SPEED));
            Assert.fail("A table with negative seat count doesn't make sense");
        } catch (InvalidSeatCountException e) {
            // Intended exception thrown, a table with negative seats doesn't make sense
        } catch (PlayerEventException e) {
            Assert.fail("Unexpected dealer exception");
        }
    }

    @Test
    public void testSingleJoin() {

        try {
            final Table table = new Table(2, 500, 1000, new Dealer(Dealer.DEFAULT_DEALER_SPEED));

            try {
                table.join(new Player("Adam Broker", 1000), 1000);
            } catch (Exception e) {
                Assert.fail("Player unexpectedly failed to join table");
            }
        } catch (InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (PlayerEventException e) {
            Assert.fail("Unexpected dealer exception");
        }
    }

    @Test
    public void testDoubleJoin() {

        try {
            final Table table = new Table(2, 500, 1000, new Dealer(Dealer.DEFAULT_DEALER_SPEED));

            final Player playerA = new Player("Adam Broker", 1000);

            try {
                table.join(playerA, 1000);
            } catch (Exception e) {
                Assert.fail("Player unexpectedly failed to join table");
            }

            try {
                table.join(playerA, 2000);
                Assert.fail("Player can't join same table twice");
            } catch (PlayerAlreadySeatedException e) {
                // Intended exception thrown, player should not be able to join the same table twice.
            } catch (Exception e) {
                Assert.fail("Incorrect exception thrown");
            }
        } catch (InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (PlayerEventException e) {
            Assert.fail("Unexpected dealer exception");
        }
    }

    @Test
    public void testFullTable() {

        try {
            final Table table = new Table(2, 500, 1000, new Dealer(Dealer.DEFAULT_DEALER_SPEED));

            try {
                table.join(new Player("Adam Broker", 1000), 1000);
                table.join(new Player("Carry Davis", 1000), 1000);
            } catch (Exception e) {
                Assert.fail("Incorrect exception thrown");
            }

            try {
                table.join(new Player("Eric Flores", 1000), 1000);
            } catch (NoSeatAvailableException e) {
                // Intended exception thrown, the table is full
            } catch (Exception e) {
                Assert.fail("Incorrect exception thrown");
            }
        } catch (InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (PlayerEventException e) {
            Assert.fail("Unexpected dealer exception");
        }
    }

    @Test
    public void testFindPlayerSeat() {

        try {
            final Table table = new Table(2, 500, 1000, new Dealer(Dealer.DEFAULT_DEALER_SPEED));

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
            } catch (PlayerNotSeatedException e) {
                Assert.fail("Player was not found at the table");
            }

            try {
                table.getPlayerSeat(new Player("Carry Davis", 1000));
                Assert.fail("Player should not be found at the table.");
            } catch (PlayerNotSeatedException e) {
                // Intended exception thrown, the player never joined the table
            }
        } catch (InvalidSeatCountException e) {
            Assert.fail("Unexpectedly failed to create table");
        } catch (PlayerEventException e) {
            Assert.fail("Unexpected dealer exception");
        }
    }
}
