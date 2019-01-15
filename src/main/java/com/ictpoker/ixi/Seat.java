package com.ictpoker.ixi;

import com.sun.istack.internal.NotNull;

public class Seat {

    private final Table table;
    private final IPlayer player;
    private int stack = 0;

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

    public class InsufficientStackException extends Exception {}
}
