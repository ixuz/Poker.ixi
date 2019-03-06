package com.ictpoker.ixi.engine.table;

import com.ictpoker.ixi.engine.commons.Card;
import com.ictpoker.ixi.engine.player.Player;
import com.ictpoker.ixi.engine.table.exception.SeatException;

import java.util.Deque;
import java.util.LinkedList;

public class Seat {

    private final Player player;
    private int stack = 0;
    private Deque<Card> cards = new LinkedList<>();
    private boolean folded = false;
    private boolean acted = false;
    private boolean sittingOut = false;
    private int committed = 0;
    private int collected = 0;

    public Seat() {

        this.player = null;
    }

    public Seat(final Player player, final int stack) {

        this.player = player;
        this.stack = stack;
    }

    public synchronized void moveCommittedToCollected() {
        setCollected(getCollected() + getCommitted());
        setCommitted(0);
    }

    public synchronized void commit(final int amount)
            throws SeatException {

        if (amount > getStack()) {
            throw new SeatException("Insufficient stack");
        }

        setStack(getStack()-amount);
        setCommitted(getCommitted()+amount);
    }

    public synchronized void commitDead(final int amount)
            throws SeatException {

        if (amount > getStack()) {
            throw new SeatException("Insufficient stack");
        }

        setStack(getStack()-amount);
        setCollected(getCollected()+amount);
    }

    public Player getPlayer() {
        return player;
    }

    public int getStack() {
        return stack;
    }

    public void setStack(int stack) {
        this.stack = stack;
    }

    public Deque<Card> getCards() {
        return cards;
    }

    public void setCards(Deque<Card> cards) {
        this.cards = cards;
    }

    public boolean isFolded() {
        return folded;
    }

    public void setFolded(boolean folded) {
        this.folded = folded;
    }

    public boolean isActed() {
        if (getStack() == 0) {
            return true;
        }
        return acted;
    }

    public void setActed(boolean acted) {
        this.acted = acted;
    }

    public boolean isSittingOut() {
        return sittingOut;
    }

    public void setSittingOut(boolean sittingOut) {
        this.sittingOut = sittingOut;
    }

    public int getCommitted() {
        return committed;
    }

    public void setCommitted(int committed) {
        this.committed = committed;
    }

    public int getCollected() {
        return collected;
    }

    public void setCollected(int collected) {
        this.collected = collected;
    }

    @Override
    public String toString() {
        return String.format("%s %s, stack: %d, committed: %d, collected: %d",
                getPlayer().getName(),
                getCards(),
                getStack(),
                getCommitted(),
                getCollected());
    }
}
