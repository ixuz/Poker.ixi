package com.ictpoker.ixi.Table.TableEvent.Action;

import com.ictpoker.ixi.Table.Exception.TableException;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.Info.MoveButtonEvent;
import com.ictpoker.ixi.Table.TableEvent.Info.SetSeatEvent;
import org.junit.Assert;
import org.junit.Test;

public class CheckEventTest {

    /**
     * Player A tries to check before the small blind has been posted.
     * It's not his turn to act.
     * @throws TableException
     */
    @Test
    public void testNegative1() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.pushEvent(new SetSeatEvent("Player A", 1000, 0));
        table.pushEvent(new SetSeatEvent("Player B", 1000, 1));
        table.pushEvent(new SetSeatEvent("Player C", 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        table.handleEventQueue();
        // Pre-flop
        table.pushEvent(new CheckEvent("Player A"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player B tries to check instead of posting the small blind.
     * Player must post small blind to play
     * @throws TableException
     */
    @Test
    public void testNegative2() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.pushEvent(new SetSeatEvent("Player A", 1000, 0));
        table.pushEvent(new SetSeatEvent("Player B", 1000, 1));
        table.pushEvent(new SetSeatEvent("Player C", 1000, 2));
        // Pre-flop
        table.pushEvent(new MoveButtonEvent(0));
        table.handleEventQueue();
        table.pushEvent(new CheckEvent("Player B"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player C tries to check instead of posting the big blind.
     * Player must post big blind to play
     * @throws TableException
     */
    @Test
    public void testNegative3() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.pushEvent(new SetSeatEvent("Player A", 1000, 0));
        table.pushEvent(new SetSeatEvent("Player B", 1000, 1));
        table.pushEvent(new SetSeatEvent("Player C", 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        // Pre-flop
        table.pushEvent(new PostSmallBlindEvent("Player B"));
        table.handleEventQueue();
        table.pushEvent(new CheckEvent("Player C"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player A tries to check pre-flop after both small & big blinds was posted.
     * Player must at least call big blind to play.
     * @throws TableException
     */
    @Test
    public void testNegative4() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.pushEvent(new SetSeatEvent("Player A", 1000, 0));
        table.pushEvent(new SetSeatEvent("Player B", 1000, 1));
        table.pushEvent(new SetSeatEvent("Player C", 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        // Pre-flop
        table.pushEvent(new PostSmallBlindEvent("Player B"));
        table.pushEvent(new PostBigBlindEvent("Player C"));
        table.handleEventQueue();
        table.pushEvent(new CheckEvent("Player A"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player B tries to check pre-flop after posting the small blind.
     * Player must at least call to match the big blind.
     * @throws TableException
     */
    @Test
    public void testNegative5() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.pushEvent(new SetSeatEvent("Player A", 1000, 0));
        table.pushEvent(new SetSeatEvent("Player B", 1000, 1));
        table.pushEvent(new SetSeatEvent("Player C", 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        // Pre-flop
        table.pushEvent(new PostSmallBlindEvent("Player B"));
        table.pushEvent(new PostBigBlindEvent("Player C"));
        table.pushEvent(new CallEvent("Player A"));
        table.handleEventQueue();
        table.pushEvent(new CheckEvent("Player B"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player B tries to check after blinds and a raise occurred.
     * Player must at least call to match the raise.
     * @throws TableException
     */
    @Test
    public void testNegative6() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.pushEvent(new SetSeatEvent("Player A", 1000, 0));
        table.pushEvent(new SetSeatEvent("Player B", 1000, 1));
        table.pushEvent(new SetSeatEvent("Player C", 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        // Pre-flop
        table.pushEvent(new PostSmallBlindEvent("Player B"));
        table.pushEvent(new PostBigBlindEvent("Player C"));
        table.pushEvent(new RaiseEvent("Player A", 20));
        table.handleEventQueue();
        table.pushEvent(new CheckEvent("Player B"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player C tries to check after blinds, a raise and re-raise occurred.
     * Player must at least call to match the raise.
     * @throws TableException
     */
    @Test
    public void testNegative7() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.pushEvent(new SetSeatEvent("Player A", 1000, 0));
        table.pushEvent(new SetSeatEvent("Player B", 1000, 1));
        table.pushEvent(new SetSeatEvent("Player C", 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        // Pre-flop
        table.pushEvent(new PostSmallBlindEvent("Player B"));
        table.pushEvent(new PostBigBlindEvent("Player C"));
        table.pushEvent(new RaiseEvent("Player A", 20));
        table.pushEvent(new RaiseEvent("Player B", 50));
        table.handleEventQueue();
        table.pushEvent(new CheckEvent("Player C"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player C tries to check after a bet occurred on the flop
     * Player must at least call to match the bet.
     * @throws TableException
     */
    @Test
    public void testNegative8() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.pushEvent(new SetSeatEvent("Player A", 1000, 0));
        table.pushEvent(new SetSeatEvent("Player B", 1000, 1));
        table.pushEvent(new SetSeatEvent("Player C", 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        // Pre-flop
        table.pushEvent(new PostSmallBlindEvent("Player B"));
        table.pushEvent(new PostBigBlindEvent("Player C"));
        table.pushEvent(new CallEvent("Player A"));
        table.pushEvent(new CallEvent("Player B"));
        table.pushEvent(new CheckEvent("Player C"));
        table.handleEventQueue();
        // Flop
        table.pushEvent(new BetEvent("Player B", 10));
        table.pushEvent(new CheckEvent("Player C"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player A tries to check after a bet and re-raise occurred on the flop
     * Player must at least call to match the bet.
     * @throws TableException
     */
    @Test
    public void testNegative9() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.pushEvent(new SetSeatEvent("Player A", 1000, 0));
        table.pushEvent(new SetSeatEvent("Player B", 1000, 1));
        table.pushEvent(new SetSeatEvent("Player C", 1000, 2));
        table.pushEvent(new MoveButtonEvent(0));
        // Pre-flop
        table.pushEvent(new PostSmallBlindEvent("Player B"));
        table.pushEvent(new PostBigBlindEvent("Player C"));
        table.pushEvent(new CallEvent("Player A"));
        table.pushEvent(new CallEvent("Player B"));
        table.pushEvent(new CheckEvent("Player C"));
        // Flop
        table.pushEvent(new BetEvent("Player B", 10));
        table.pushEvent(new RaiseEvent("Player C", 20));
        table.handleEventQueue();
        table.pushEvent(new CheckEvent("Player A"));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }
}
