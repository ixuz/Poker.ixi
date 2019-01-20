package com.ictpoker.ixi.Table;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Player.IPlayer;
import com.sun.istack.internal.NotNull;

import java.util.Stack;

public class Seat {

    private final IPlayer player;
    private int stack = 0;
    private Stack<Card> cards = new Stack<>();
    private boolean folded = false;
    private boolean acted = false;
    private int committed = 0;

    public Seat() {

        this.player = null;
    }

    public Seat(@NotNull final IPlayer player, @NotNull final int stack) {

        this.player = player;
        this.stack = stack;
    }

    public IPlayer getPlayer() {

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

    public class InsufficientStackException extends Exception {}
}
