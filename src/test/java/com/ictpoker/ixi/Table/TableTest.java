package com.ictpoker.ixi.Table;

import com.ictpoker.ixi.Table.Exception.InvalidSeatCountException;
import com.ictpoker.ixi.Table.Exception.PlayerNotSeatedException;
import com.ictpoker.ixi.Table.Exception.TableException;
import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Table.TableEvent.JoinEvent;
import com.ictpoker.ixi.Table.TableEvent.LeaveEvent;
import com.ictpoker.ixi.Player.PlayerEvent.PlayerEventException;
import org.junit.Assert;
import org.junit.Test;

public class TableTest {

    @Test
    public void testInvalidSeatCounts() {

        try {
            new Table(0, 500, 1000, 5, 10);
            Assert.fail("A table with zero seats is not allowed");
        } catch (InvalidSeatCountException e) {
            // Intended exception thrown, a table with zero seats is not allowed
        } catch (TableException e) {
            Assert.fail("Unexpectedly failed to create table");
        }

        try {
            new Table(1, 500, 1000, 5, 10);
            Assert.fail("A table with one seat is not allowed");
        } catch (InvalidSeatCountException e) {
            // Intended exception thrown, a table with zero seats is not allowed
        } catch (TableException e) {
            Assert.fail("Unexpectedly failed to create table");
        }

        try {
            new Table(Table.MAXIMUM_SEATS+1, 500, 1000, 5, 10);
            Assert.fail("A table with more seats than the maximum amount is not allowed");
        } catch (InvalidSeatCountException e) {
            // Intended exception thrown, a table with more seats than the maximum amount is not allowed
        } catch (TableException e) {
            Assert.fail("Unexpectedly failed to create table");
        }

        try {
            new Table(-1, 500, 1000, 5, 10);
            Assert.fail("A table with negative seat count doesn't make sense");
        } catch (InvalidSeatCountException e) {
            // Intended exception thrown, a table with negative seats doesn't make sense
        } catch (TableException e) {
            Assert.fail("Unexpectedly failed to create table");
        }
    }

    @Test
    public void testJoin() {

        try {
            final Table table = new Table(2, 500, 1000, 5, 10);

            try {
                final Player playerA = new Player("Adam Broker", 1000);
                table.pushEvent(new JoinEvent(playerA, 1000, 0));

                table.update();

                try {
                    Assert.assertEquals(0, table.getPlayerSeatIndex(playerA));
                } catch (PlayerNotSeatedException e) {
                    e.printStackTrace();
                    Assert.fail("Player should already be seated");
                }
            } catch (PlayerEventException e) {
                e.printStackTrace();
                Assert.fail("Unexpectedly failed to handle player event");
            }
        } catch (InvalidSeatCountException e) {
            e.printStackTrace();
            Assert.fail("Unexpectedly failed to create table");
        } catch (TableException e) {
            e.printStackTrace();
            Assert.fail("Unexpected table exception");
        }
    }

    @Test
    public void testDoubleJoin() {

        try {
            final Table table = new Table(2, 500, 1000, 5, 10);

            final Player playerA = new Player("Adam Broker", 1000);

            table.pushEvent(new JoinEvent(playerA, 500, 0));
            table.pushEvent(new JoinEvent(playerA, 500, 0));

            table.update();
        } catch (InvalidSeatCountException e) {
            e.printStackTrace();
            Assert.fail("Unexpectedly failed to create table");
        } catch (PlayerEventException e) {
            e.printStackTrace();
            Assert.fail("Unexpected dealer exception");
        } catch (TableException e) {
            // Inteded exception, the player should not be able to join a table twice.
        }
    }

    @Test
    public void testFullTable() {

        try {
            final Table table = new Table(2, 500, 1000, 5, 10);

            final Player playerA = new Player("Adam Broker", 1000);
            final Player playerB = new Player("Carry Davis", 1000);

            table.pushEvent(new JoinEvent(playerA, 1000, 0));
            table.pushEvent(new JoinEvent(playerB, 1000, 1));

            table.update();

            try {
                Assert.assertEquals(0, table.getPlayerSeatIndex(playerA));
                Assert.assertEquals(1, table.getPlayerSeatIndex(playerB));
            } catch (PlayerNotSeatedException e) {
                e.printStackTrace();
                Assert.fail("Two players should already be seated");
            }

            try {
                final Player playerC = new Player("Eric Flores", 1000);
                table.pushEvent(new JoinEvent(playerC, 1000, 0));
                table.update();
            } catch (TableException e) {
                // Intended exception, the player should not be able to join an already occupied seat.
            }

            try {
                final Player playerC = new Player("Eric Flores", 1000);
                table.pushEvent(new JoinEvent(playerC, 1000, 1));
                table.update();
            } catch (TableException e) {
                // Intended exception, the player should not be able to join an already occupied seat.
            }

            try {
                final Player playerC = new Player("Eric Flores", 1000);
                table.pushEvent(new JoinEvent(playerC, 1000, 2));
                table.update();
            } catch (TableException e) {
                // Intended exception, the player should not be able to join a non-existant seat.
            }
        } catch (InvalidSeatCountException e) {
            e.printStackTrace();
            Assert.fail("Unexpectedly failed to create table");
        } catch (PlayerEventException e) {
            e.printStackTrace();
            Assert.fail("Unexpected dealer exception");
        } catch (TableException e) {
            e.printStackTrace();
            Assert.fail("Unexpected table exception");
        }
    }

    @Test
    public void testJoinLeave() {

        try {
            final Table table = new Table(2, 500, 1000, 5, 10);

            try {
                final Player playerA = new Player("Adam Broker", 1000);
                table.pushEvent(new JoinEvent(playerA, 1000, 0));
                table.pushEvent(new LeaveEvent(playerA));

                table.update();

                try {
                    table.getPlayerSeatIndex(playerA);
                    Assert.fail("Two player has already left the table and should therefore not be seated anymore");
                } catch (PlayerNotSeatedException e) {
                    // Intended exception thrown, the player should already have left the table.
                }
            } catch (PlayerEventException e) {
                e.printStackTrace();
                Assert.fail("Unexpectedly failed to handle player event");
            }
        } catch (InvalidSeatCountException e) {
            e.printStackTrace();
            Assert.fail("Unexpectedly failed to create table");
        } catch (TableException e) {
            e.printStackTrace();
            Assert.fail("Unexpected table exception");
        }
    }

    @Test
    public void testInsufficientBalance() {

        try {
            final Table table = new Table(2, 500, 1000, 5, 10);

            try {
                final Player playerA = new Player("Adam Broker", 250);
                table.pushEvent(new JoinEvent(playerA, 1000, 0));

                table.update();
                Assert.fail("The player doesn't have enough balance to join this table.");
            } catch (PlayerEventException e) {
                // Intended exception thrown, the player should not have enough balance to join this table.
            }
        } catch (InvalidSeatCountException e) {
            e.printStackTrace();
            Assert.fail("Unexpectedly failed to create table");
        } catch (TableException e) {
            e.printStackTrace();
            Assert.fail("Unexpected table exception");
        }
    }

    @Test
    public void testInvalidBuyIn() {

        try {
            final Table table = new Table(2, 500, 1000, 5, 10);

            try {
                final Player playerA = new Player("Adam Broker", 5000);
                table.pushEvent(new JoinEvent(playerA, 200, 0));

                table.update();
                Assert.fail("The buy in is too small for this table");
            } catch (TableException e) {
                // Intended exception thrown, the player must buy in with an acceptable amount.
            }

            try {
                final Player playerB = new Player("Carry Davis", 5000);
                table.pushEvent(new JoinEvent(playerB, 3000, 0));

                table.update();
                Assert.fail("The buy in is too big for this table");
            } catch (TableException e) {
                // Intended exception thrown, the player must buy in with an acceptable amount.
            }
        } catch (InvalidSeatCountException e) {
            e.printStackTrace();
            Assert.fail("Unexpectedly failed to create table");
        } catch (PlayerEventException e) {
            e.printStackTrace();
            Assert.fail("Unexpectedly failed to handle player event");
        } catch (TableException e) {
            Assert.fail("Unexpectedly failed to create table");
        }
    }
}
