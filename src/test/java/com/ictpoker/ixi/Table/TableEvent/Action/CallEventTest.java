package com.ictpoker.ixi.Table.TableEvent.Action;

import com.ictpoker.ixi.Table.Exception.TableException;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.Info.MoveButtonEvent;
import com.ictpoker.ixi.Table.TableEvent.Info.SetSeatEvent;
import org.junit.Assert;
import org.junit.Test;

public class CallEventTest {

    /**
     * Player A tries to call before the small blind has been posted.
     * It's not his turn to act.
     * @throws TableException
     */
    @Test
    public void testNegative1() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new SetSeatEvent("Player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("Player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("Player C", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        table.handleEventQueue();
        // Pre-flop
        table.addEventLast(new CallEvent("Player A"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player B tries to call instead of posting the small blind.
     * Player must post small blind to play
     * @throws TableException
     */
    @Test
    public void testNegative2() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new SetSeatEvent("Player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("Player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("Player C", 1000, 2));
        // Pre-flop
        table.addEventLast(new MoveButtonEvent(0));
        table.handleEventQueue();
        table.addEventLast(new CallEvent("Player B"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player C tries to call instead of posting the big blind.
     * Player must post big blind to play
     * @throws TableException
     */
    @Test
    public void testNegative3() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new SetSeatEvent("Player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("Player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("Player C", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Player B"));
        table.handleEventQueue();
        table.addEventLast(new CallEvent("Player C"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player B tries to call immediately after flop has been dealt.
     * Player can't call, no bet to call.
     * @throws TableException
     */
    @Test
    public void testNegative4() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new SetSeatEvent("Player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("Player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("Player C", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Player B"));
        table.addEventLast(new PostBigBlindEvent("Player C"));
        table.addEventLast(new CallEvent("Player A"));
        table.addEventLast(new CallEvent("Player B"));
        table.addEventLast(new CheckEvent("Player C"));
        table.handleEventQueue();
        // Flop
        table.addEventLast(new CallEvent("Player B"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }
}
