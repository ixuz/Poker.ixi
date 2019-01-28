package com.ictpoker.ixi.Table;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Player.Player;
import com.sun.istack.internal.NotNull;
import lombok.Data;
import lombok.Getter;

import java.util.Stack;

@Data
public class Seat {

    private final Player player;
    private int stack = 0;
    private Stack<Card> cards = new Stack<>();
    private boolean folded = false;
    private boolean acted = false;
    private boolean sittingOut = false;
    private int committed = 0;
    private int collected = 0;

    public Seat() {

        this.player = null;
    }

    public Seat(@NotNull final Player player, @NotNull final int stack) {

        this.player = player;
        this.stack = stack;
    }

    public synchronized void moveCommittedToCollected() {
        setCollected(getCollected() + getCommitted());
        setCommitted(0);
    }

    public synchronized void commit(@NotNull final int amount)
            throws Exception {

        if (amount > getStack()) {
            throw new Exception("Insufficient stack");
        }

        setStack(getStack()-amount);
        setCommitted(getCommitted()+amount);
    }
}
