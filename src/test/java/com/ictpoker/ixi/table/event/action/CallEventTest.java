package com.ictpoker.ixi.table.event.action;

import com.ictpoker.ixi.table.exception.TableException;
import com.ictpoker.ixi.table.Table;
import com.ictpoker.ixi.table.event.info.MoveButtonEvent;
import com.ictpoker.ixi.table.event.info.SetSeatEvent;
import org.junit.Assert;
import org.junit.Test;

public class CallEventTest {

    /**
     * player A tries to call before the small blind has been posted.
     * It's not his turn to act.
     * @throws TableException
     */
    @Test
    public void testNegative1() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new SetSeatEvent("player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("player C", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        table.handleEventQueue();
        // Pre-flop
        table.addEventLast(new CallEvent("player A"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * player B tries to call instead of posting the small blind.
     * player must post small blind to play
     * @throws TableException
     */
    @Test
    public void testNegative2() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new SetSeatEvent("player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("player C", 1000, 2));
        // Pre-flop
        table.addEventLast(new MoveButtonEvent(0));
        table.handleEventQueue();
        table.addEventLast(new CallEvent("player B"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * player C tries to call instead of posting the big blind.
     * player must post big blind to play
     * @throws TableException
     */
    @Test
    public void testNegative3() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new SetSeatEvent("player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("player C", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("player B"));
        table.handleEventQueue();
        table.addEventLast(new CallEvent("player C"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * player B tries to call immediately after flop has been dealt.
     * player can't call, no bet to call.
     * @throws TableException
     */
    @Test
    public void testNegative4() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

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
        table.addEventLast(new CallEvent("player B"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }
}
