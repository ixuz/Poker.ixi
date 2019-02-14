package com.ictpoker.ixi.table;

import com.ictpoker.ixi.table.exception.*;
import com.ictpoker.ixi.table.event.action.JoinEvent;
import com.ictpoker.ixi.table.event.action.LeaveEvent;
import org.junit.Assert;
import org.junit.Test;

public class TableTest {

    @Test
    public void testJoin() throws TableException {

        final Table table = new Table(500, 1000, 5, 10);

        table.addEventLast(new JoinEvent("Adam Broker", 1000, 0));

        table.handleEventQueue();

        Assert.assertEquals(0, table.getSeatIndexByPlayerName("Adam Broker"));
    }

    @Test
    public void testDoubleJoin() {

        try {
            final Table table = new Table(500, 1000, 5, 10);

            table.addEventLast(new JoinEvent("Adam Broker", 500, 0));
            table.addEventLast(new JoinEvent("Adam Broker", 500, 0));

            table.handleEventQueue();
        } catch (TableException e) {
            // Intended exception, the player should not be able to join a table twice.
        }
    }

    @Test
    public void testFullTable() throws TableException {

        final Table table = new Table(500, 1000, 5, 10);

        table.addEventLast(new JoinEvent("Adam Broker", 1000, 0));
        table.addEventLast(new JoinEvent("Carry Davis", 1000, 1));

        table.handleEventQueue();

        Assert.assertEquals(0, table.getSeatIndexByPlayerName("Adam Broker"));
        Assert.assertEquals(1, table.getSeatIndexByPlayerName("Carry Davis"));

        try {
            table.addEventLast(new JoinEvent("Eric Flores", 1000, 0));
            table.handleEventQueue();
        } catch (TableException e) {
            // Intended exception, the player should not be able to join an already occupied seat.
        }

        try {
            table.addEventLast(new JoinEvent("Eric Flores", 1000, 1));
            table.handleEventQueue();
        } catch (TableException e) {
            // Intended exception, the player should not be able to join an already occupied seat.
        }

        try {
            table.addEventLast(new JoinEvent("Eric Flores", 1000, 2));
            table.handleEventQueue();
        } catch (TableException e) {
            // Intended exception, the player should not be able to join a non-existant seat.
        }
    }

    @Test
    public void testJoinLeave() throws TableException {

        final Table table = new Table(500, 1000, 5, 10);

        table.addEventLast(new JoinEvent("Adam Broker", 1000, 0));
        table.addEventLast(new LeaveEvent("Adam Broker"));

        table.handleEventQueue();

        if (table.getSeatIndexByPlayerName("Adam Broker") != -1) {
            Assert.fail("The player should already have left the table and therefore no index should be found");
        }
    }

    @Test
    public void testInvalidBuyIn() {

        final Table table = new Table(500, 1000, 5, 10);

        try {
            table.addEventLast(new JoinEvent("Adam Broker", 200, 0));

            table.handleEventQueue();
            Assert.fail("The buy in is too small for this table");
        } catch (TableException e) {
            // Intended exception thrown, the player must buy in with an acceptable amount.
        }

        try {
            table.addEventLast(new JoinEvent("Carry Davis", 3000, 0));

            table.handleEventQueue();
            Assert.fail("The buy in is too big for this table");
        } catch (TableException e) {
            // Intended exception thrown, the player must buy in with an acceptable amount.
        }
    }
}
