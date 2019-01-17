package com.ictpoker.ixi.Table;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Table.TableException.InvalidSeatCountException;
import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableState {

    public final static int MAXIMUM_SEATS = 10;
    private final List<Seat> seats;
    private final List<Card> boardCards = new ArrayList<>();
    private final int minimumBuyIn;
    private final int maximumBuyIn;
    private int buttonPosition = 0;
    private int pot = 0;

    public TableState(@NotNull final int nSeats, @NotNull final int minimumBuyIn, @NotNull final int maximumBuyIn)
            throws InvalidSeatCountException {

        if (nSeats <= 0 || nSeats > MAXIMUM_SEATS) {
            throw new InvalidSeatCountException();
        }

        this.seats = Arrays.asList(new Seat[nSeats]);
        this.minimumBuyIn = minimumBuyIn;
        this.maximumBuyIn = maximumBuyIn;
    }

    public List<Seat> getSeats() {

        return seats;
    }

    public List<Card> getBoardCards() {

        return boardCards;
    }

    public void setButtonPosition(int buttonPosition) {

        this.buttonPosition = buttonPosition;
    }

    public int getButtonPosition() {

        return buttonPosition;
    }

    public int getHighestCommittedAmount() {

        int highestCommitted = 0;
        for (Seat seat : seats) {

            if (seat != null && seat.getCommitted() > highestCommitted) {
                highestCommitted = seat.getCommitted();
            }
        }
        return highestCommitted;
    }

    public void addToPot(int chips) {

        this.pot += chips;
    }

    public int collectPot() {

        int pot = this.pot;
        this.pot = 0;
        return pot;
    }

    public int getPot() {

        return pot;
    }

    public int getMinimumBuyIn() {
        return minimumBuyIn;
    }

    public int getMaximumBuyIn() {
        return maximumBuyIn;
    }
}