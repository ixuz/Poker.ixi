package com.ictpoker.ixi.table.event.action;

import com.ictpoker.ixi.table.event.info.SetTableDetailsEvent;
import com.ictpoker.ixi.table.exception.TableException;
import com.ictpoker.ixi.table.Table;
import com.ictpoker.ixi.table.event.info.MoveButtonEvent;
import com.ictpoker.ixi.table.event.info.SetSeatEvent;
import org.junit.Assert;
import org.junit.Test;

public class CheckEventTest {

    /**
     * player A tries to check before the small blind has been posted.
     * It's not his turn to act.
     * @throws TableException
     */
    @Test
    public void testNegative1() throws TableException {

        final Table table = new Table(500, 1000, 5,10);
        table.addEventLast(new SetTableDetailsEvent("A table name", 6, 0));

        table.addEventLast(new SetSeatEvent("player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("player C", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        table.handleEventQueue();
        // Pre-flop
        table.addEventLast(new CheckEvent("player A"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * player B tries to check instead of posting the small blind.
     * player must post small blind to play
     * @throws TableException
     */
    @Test
    public void testNegative2() throws TableException {

        final Table table = new Table(500, 1000, 5,10);
        table.addEventLast(new SetTableDetailsEvent("A table name", 6, 0));

        table.addEventLast(new SetSeatEvent("player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("player C", 1000, 2));
        // Pre-flop
        table.addEventLast(new MoveButtonEvent(0));
        table.handleEventQueue();
        table.addEventLast(new CheckEvent("player B"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * player C tries to check instead of posting the big blind.
     * player must post big blind to play
     * @throws TableException
     */
    @Test
    public void testNegative3() throws TableException {

        final Table table = new Table(500, 1000, 5,10);
        table.addEventLast(new SetTableDetailsEvent("A table name", 6, 0));

        table.addEventLast(new SetSeatEvent("player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("player C", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("player B"));
        table.handleEventQueue();
        table.addEventLast(new CheckEvent("player C"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * player A tries to check pre-flop after both small & big blinds was posted.
     * player must at least call big blind to play.
     * @throws TableException
     */
    @Test
    public void testNegative4() throws TableException {

        final Table table = new Table(500, 1000, 5,10);
        table.addEventLast(new SetTableDetailsEvent("A table name", 6, 0));

        table.addEventLast(new SetSeatEvent("player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("player C", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("player B"));
        table.addEventLast(new PostBigBlindEvent("player C"));
        table.handleEventQueue();
        table.addEventLast(new CheckEvent("player A"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * player B tries to check pre-flop after posting the small blind.
     * player must at least call to match the big blind.
     * @throws TableException
     */
    @Test
    public void testNegative5() throws TableException {

        final Table table = new Table(500, 1000, 5,10);
        table.addEventLast(new SetTableDetailsEvent("A table name", 6, 0));

        table.addEventLast(new SetSeatEvent("player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("player C", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("player B"));
        table.addEventLast(new PostBigBlindEvent("player C"));
        table.addEventLast(new CallEvent("player A"));
        table.handleEventQueue();
        table.addEventLast(new CheckEvent("player B"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * player B tries to check after blinds and a raise occurred.
     * player must at least call to match the raise.
     * @throws TableException
     */
    @Test
    public void testNegative6() throws TableException {

        final Table table = new Table(500, 1000, 5,10);
        table.addEventLast(new SetTableDetailsEvent("A table name", 6, 0));

        table.addEventLast(new SetSeatEvent("player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("player C", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("player B"));
        table.addEventLast(new PostBigBlindEvent("player C"));
        table.addEventLast(new RaiseEvent("player A", 20));
        table.handleEventQueue();
        table.addEventLast(new CheckEvent("player B"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * player C tries to check after blinds, a raise and re-raise occurred.
     * player must at least call to match the raise.
     * @throws TableException
     */
    @Test
    public void testNegative7() throws TableException {

        final Table table = new Table(500, 1000, 5,10);
        table.addEventLast(new SetTableDetailsEvent("A table name", 6, 0));

        table.addEventLast(new SetSeatEvent("player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("player C", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("player B"));
        table.addEventLast(new PostBigBlindEvent("player C"));
        table.addEventLast(new RaiseEvent("player A", 20));
        table.addEventLast(new RaiseEvent("player B", 50));
        table.handleEventQueue();
        table.addEventLast(new CheckEvent("player C"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * player C tries to check after a bet occurred on the flop
     * player must at least call to match the bet.
     * @throws TableException
     */
    @Test
    public void testNegative8() throws TableException {

        final Table table = new Table(500, 1000, 5,10);
        table.addEventLast(new SetTableDetailsEvent("A table name", 6, 0));

        table.addEventLast(new SetSeatEvent("player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("player C", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("player B"));
        table.addEventLast(new PostBigBlindEvent("player C"));
        table.addEventLast(new CallEvent("player A"));
        table.addEventLast(new CallEvent("player B"));
        table.addEventLast(new CheckEvent("player C"));
        table.handleEventQueue();
        // Flop
        table.addEventLast(new BetEvent("player B", 10));
        table.addEventLast(new CheckEvent("player C"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * player A tries to check after a bet and re-raise occurred on the flop
     * player must at least call to match the bet.
     * @throws TableException
     */
    @Test
    public void testNegative9() throws TableException {

        final Table table = new Table(500, 1000, 5,10);
        table.addEventLast(new SetTableDetailsEvent("A table name", 6, 0));

        table.addEventLast(new SetSeatEvent("player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("player C", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("player B"));
        table.addEventLast(new PostBigBlindEvent("player C"));
        table.addEventLast(new CallEvent("player A"));
        table.addEventLast(new CallEvent("player B"));
        table.addEventLast(new CheckEvent("player C"));
        // Flop
        table.addEventLast(new BetEvent("player B", 10));
        table.addEventLast(new RaiseEvent("player C", 20));
        table.handleEventQueue();
        table.addEventLast(new CheckEvent("player A"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }
}
