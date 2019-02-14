package com.ictpoker.ixi.Table.TableEvent.Action;

import com.ictpoker.ixi.Table.Exception.TableException;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.Info.MoveButtonEvent;
import com.ictpoker.ixi.Table.TableEvent.Info.SetSeatEvent;
import org.junit.Assert;
import org.junit.Test;

public class BetEventTest {

    /**
     * Player A tries to bet before the small blind has been posted.
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
        table.addEventLast(new BetEvent("Player A", 20));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player B tries to bet immediately after posting small blind.
     * It's not his turn to act.
     * @throws TableException
     */
    @Test
    public void testNegative2() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new SetSeatEvent("Player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("Player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("Player C", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Player B"));
        table.handleEventQueue();
        table.addEventLast(new BetEvent("Player B", 20));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player C tries to bet immediately after posting big blind.
     * It's not his turn to act.
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
        table.addEventLast(new PostBigBlindEvent("Player C"));
        table.handleEventQueue();
        table.addEventLast(new BetEvent("Player C", 20));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player A tries to bet after both blinds have been posted.
     * Invalid because a player has already added chips to the pot.
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
        table.handleEventQueue();
        table.addEventLast(new BetEvent("Player A", 20));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player B tries to bet after another player has raised pre-flop.
     * Invalid because a player has already added chips to the pot.
     * @throws TableException
     */
    @Test
    public void testNegative5() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new SetSeatEvent("Player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("Player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("Player C", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Player B"));
        table.addEventLast(new PostBigBlindEvent("Player C"));
        table.addEventLast(new RaiseEvent("Player A", 20));
        table.handleEventQueue();
        table.addEventLast(new BetEvent("Player B", 50));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player B tries to bet before player A has acted.
     * It's not his turn to act.
     * @throws TableException
     */
    @Test
    public void testNegative6() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new SetSeatEvent("Player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("Player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("Player C", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Player B"));
        table.addEventLast(new PostBigBlindEvent("Player C"));
        table.handleEventQueue();
        table.addEventLast(new BetEvent("Player B", 50));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player C tries to bet before player A has acted.
     * It's not his turn to act.
     * @throws TableException
     */
    @Test
    public void testNegative7() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new SetSeatEvent("Player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("Player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("Player C", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Player B"));
        table.addEventLast(new PostBigBlindEvent("Player C"));
        table.handleEventQueue();
        table.addEventLast(new BetEvent("Player C", 50));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player C tries to bet before player B has acted.
     * It's not his turn to act.
     * @throws TableException
     */
    @Test
    public void testNegative8() throws TableException {

        final Table table = new Table(500, 1000, 5,10);

        table.addEventLast(new SetSeatEvent("Player A", 1000, 0));
        table.addEventLast(new SetSeatEvent("Player B", 1000, 1));
        table.addEventLast(new SetSeatEvent("Player C", 1000, 2));
        table.addEventLast(new MoveButtonEvent(0));
        // Pre-flop
        table.addEventLast(new PostSmallBlindEvent("Player B"));
        table.addEventLast(new PostBigBlindEvent("Player C"));
        table.addEventLast(new CallEvent("Player A"));
        table.handleEventQueue();
        table.addEventLast(new BetEvent("Player C", 50));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player A tries to bet before player C has acted.
     * It's not his turn to act.
     * @throws TableException
     */
    @Test
    public void testNegative9() throws TableException {

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
        table.handleEventQueue();
        table.addEventLast(new BetEvent("Player A", 50));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player C tries to bet when he has the option to check from the BB position.
     * Invalid because a player has already added chips to the pot.
     * @throws TableException
     */
    @Test
    public void testNegative10() throws TableException {

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
        table.handleEventQueue();
        table.addEventLast(new BetEvent("Player C", 20));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player B tries to bet less than the minimum bet size while he has enough stack to fulfill a full minimum bet.
     * Invalid because a player must bet the at least the size of a big blind if he has sufficient stack.
     * @throws TableException
     */
    @Test
    public void testNegative11() throws TableException {

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
        table.addEventLast(new BetEvent("Player B", 9));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }

    /**
     * Player A tries to bet more than his remaining the stack.
     * Invalid because a player may only bet with chips available in the stack.
     * @throws TableException
     */
    @Test
    public void testNegative12() throws TableException {

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
        table.addEventLast(new BetEvent("Player B", 2000));
        try {
            table.handleEventQueue(); // Must throw
            Assert.fail();
        } catch (TableException e) {
            // Intended exception
            e.printStackTrace();
        }
    }
}
