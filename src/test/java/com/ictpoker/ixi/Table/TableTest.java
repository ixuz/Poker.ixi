package com.ictpoker.ixi.Table;

import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Table.Exception.*;
import com.ictpoker.ixi.Table.TableEvent.JoinEvent;
import com.ictpoker.ixi.Table.TableEvent.LeaveEvent;
import org.junit.Assert;
import org.junit.Test;

public class TableTest {

    @Test
    public void testJoin() throws TableException, TableStateException, TableEventException {

        final Table table = new Table(500, 1000, 5, 10);

        final Player playerA = new Player("Adam Broker", 1000);
        table.pushEvent(new JoinEvent(playerA, 1000, 0));

        table.handleEventQueue();

        Assert.assertEquals(0, table.getPlayerSeatIndex(playerA));
    }

    @Test
    public void testDoubleJoin() throws TableStateException, TableEventException {

        try {
            final Table table = new Table(500, 1000, 5, 10);

            final Player playerA = new Player("Adam Broker", 1000);

            table.pushEvent(new JoinEvent(playerA, 500, 0));
            table.pushEvent(new JoinEvent(playerA, 500, 0));

            table.handleEventQueue();
        } catch (TableException e) {
            // Inteded exception, the player should not be able to join a table twice.
        }
    }

    @Test
    public void testFullTable() throws TableException, TableStateException, TableEventException {

        final Table table = new Table(500, 1000, 5, 10);

        final Player playerA = new Player("Adam Broker", 1000);
        final Player playerB = new Player("Carry Davis", 1000);

        table.pushEvent(new JoinEvent(playerA, 1000, 0));
        table.pushEvent(new JoinEvent(playerB, 1000, 1));

        table.handleEventQueue();

        Assert.assertEquals(0, table.getPlayerSeatIndex(playerA));
        Assert.assertEquals(1, table.getPlayerSeatIndex(playerB));

        try {
            final Player playerC = new Player("Eric Flores", 1000);
            table.pushEvent(new JoinEvent(playerC, 1000, 0));
            table.handleEventQueue();
        } catch (TableException e) {
            // Intended exception, the player should not be able to join an already occupied seat.
        }

        try {
            final Player playerC = new Player("Eric Flores", 1000);
            table.pushEvent(new JoinEvent(playerC, 1000, 1));
            table.handleEventQueue();
        } catch (TableException e) {
            // Intended exception, the player should not be able to join an already occupied seat.
        }

        try {
            final Player playerC = new Player("Eric Flores", 1000);
            table.pushEvent(new JoinEvent(playerC, 1000, 2));
            table.handleEventQueue();
        } catch (TableException e) {
            // Intended exception, the player should not be able to join a non-existant seat.
        }
    }

    @Test
    public void testJoinLeave() throws TableException, TableStateException, TableEventException {

        final Table table = new Table(500, 1000, 5, 10);

        final Player playerA = new Player("Adam Broker", 1000);
        table.pushEvent(new JoinEvent(playerA, 1000, 0));
        table.pushEvent(new LeaveEvent(playerA));

        table.handleEventQueue();

        if (table.getPlayerSeatIndex(playerA) != -1) {
            Assert.fail("The player should already have left the table and therefore no index should be found");
        }
    }

    @Test
    public void testInsufficientBalance() throws TableStateException, TableException {

        final Table table = new Table(500, 1000, 5, 10);

        final Player playerA = new Player("Adam Broker", 250);

        try {
            table.pushEvent(new JoinEvent(playerA, 1000, 0));
        } catch (TableEventException e) {
            // Intended exception thrown, the player should not have enough balance to join this table.
        }

        table.handleEventQueue();
    }

    @Test
    public void testInvalidBuyIn() throws TableStateException, TableEventException {

        final Table table = new Table(500, 1000, 5, 10);

        try {
            final Player playerA = new Player("Adam Broker", 5000);
            table.pushEvent(new JoinEvent(playerA, 200, 0));

            table.handleEventQueue();
            Assert.fail("The buy in is too small for this table");
        } catch (TableException e) {
            // Intended exception thrown, the player must buy in with an acceptable amount.
        }

        try {
            final Player playerB = new Player("Carry Davis", 5000);
            table.pushEvent(new JoinEvent(playerB, 3000, 0));

            table.handleEventQueue();
            Assert.fail("The buy in is too big for this table");
        } catch (TableException e) {
            // Intended exception thrown, the player must buy in with an acceptable amount.
        }
    }
}
