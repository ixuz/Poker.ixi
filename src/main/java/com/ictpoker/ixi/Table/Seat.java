package com.ictpoker.ixi.Table;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Player.Player;
import com.sun.istack.internal.NotNull;

import java.util.Stack;

public class Seat {

    private final Player player;
    private int stack = 0;
    private Stack<Card> cards = new Stack<>();
    private boolean folded = false;
    private boolean acted = false;
    private int committed = 0;
    private int collected = 0;
    private boolean sittingOut = false;

    public Seat() {

        this.player = null;
    }

    public Seat(@NotNull final Player player, @NotNull final int stack) {

        this.player = player;
        this.stack = stack;
    }

    public Player getPlayer() {

        return player;
    }

    public void setStack(@NotNull final int stack) {

        this.stack = stack;
    }

    public int getStack() {

        return stack;
    }

    public Stack<Card> getCards() {

        return cards;
    }

    public boolean hasFolded() {

        return folded;
    }

    public void setFolded(@NotNull final boolean folded) {

        this.folded = folded;
    }

    public boolean hasActed() {

        return acted;
    }

    public void setActed(boolean acted) {

        this.acted = acted;
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

    public void moveCommittedToCollected() {
        setCollected(getCollected() + getCommitted());
        setCommitted(0);
    }

    public void commit(@NotNull final int amount)
            throws Exception {

        if (amount > getStack()) {
            throw new Exception("Insufficient stack");
        }

        setStack(getStack()-amount);
        setCommitted(getCommitted()+amount);
    }

    public boolean isSittingOut() {

        return sittingOut;
    }

    public void setSittingOut(@NotNull final boolean sittingOut) {

        this.sittingOut = sittingOut;
    }

    public class InsufficientStackException extends Exception {}
}
