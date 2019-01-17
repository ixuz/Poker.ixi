package com.ictpoker.ixi.Table;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Player.IPlayer;
import com.sun.istack.internal.NotNull;

import java.util.Stack;

public class Seat {

    private final Table table;
    private final IPlayer player;
    private int stack = 0;
    private Stack<Card> cards = new Stack<>();
    private boolean folded = false;
    private boolean acted = false;
    private int committed = 0;

    public Seat(@NotNull final Table table, @NotNull final IPlayer player, @NotNull final int stack)
            throws IllegalArgumentException {

        if (stack < 0) throw new IllegalArgumentException();

        this.table = table;
        this.player = player;
        this.stack = stack;
    }

    public int commit(@NotNull final int amount, @NotNull final boolean fullAmountRequired)
            throws InsufficientStackException, IllegalArgumentException {

        if (stack == 0) throw new InsufficientStackException();
        if (amount < 0) throw new IllegalArgumentException();
        if (fullAmountRequired && stack - amount < 0) throw new InsufficientStackException();

        final int toCommit = Math.min(amount, stack - amount);

        stack -= toCommit;
        committed += toCommit;

        return toCommit;
    }

    public IPlayer getPlayer() {

        return player;
    }

    public int getStack() {

        return stack;
    }

    public void pushCard(@NotNull final Card card) {

        cards.push(card);
    }

    public Stack<Card> getCards() {

        return cards;
    }

    public Card popCard() {

        return cards.pop();
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

    public void setActed(@NotNull final boolean acted) {

        this.acted = acted;
    }

    public int getCommitted() {

        return committed;
    }

    public int collectCommitted() {

        int committed = getCommitted();
        this.committed = 0;
        return committed;
    }

    public class InsufficientStackException extends Exception {}
}
