package com.ictpoker.ixi;

import com.sun.istack.internal.NotNull;

import java.util.List;
import java.util.Stack;

public class Seat {

    private final Table table;
    private final IPlayer player;
    private int stack = 0;
    private Stack<Card> cards = new Stack<>();

    public Seat(@NotNull final Table table, @NotNull final IPlayer player, @NotNull final int stack)
            throws IllegalArgumentException {

        if (stack < 0) throw new IllegalArgumentException();

        this.table = table;
        this.player = player;
        this.stack = stack;
    }

    public void commit(@NotNull final int amount)
            throws InsufficientStackException, IllegalArgumentException {

        if (amount < 0) throw new IllegalArgumentException();
        if (stack-amount < 0) throw new InsufficientStackException();
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

    public class InsufficientStackException extends Exception {}
}
