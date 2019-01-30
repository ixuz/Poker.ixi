package com.ictpoker.ixi.Table;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Commons.Deck;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

public class TableState {
    protected final static int SEATS = 6;
    protected final static int FLOP = 3;
    protected final static int TURN = 1;
    protected final static int RIVER = 1;

    private final List<Seat> seats = Stream.generate(Seat::new).limit(SEATS).collect(toCollection(ArrayList::new));
    private final List<Card> boardCards = new ArrayList<>();
    private final Deck deck = new Deck();
    private int buttonPosition = 0;
    private Seat lastRaiser = null;
    private int lastRaiseAmount = 0;
    private Seat seatToAct = null;
    private boolean smallBlindPosted = false;
    private boolean bigBlindPosted = false;

    private final int minimumBuyIn;
    private final int maximumBuyIn;
    private final int smallBlindAmount;
    private final int bigBlindAmount;

    public TableState(int minimumBuyIn, int maximumBuyIn, int smallBlindAmount, int bigBlindAmount) {
        this.minimumBuyIn = minimumBuyIn;
        this.maximumBuyIn = maximumBuyIn;
        this.smallBlindAmount = smallBlindAmount;
        this.bigBlindAmount = bigBlindAmount;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public List<Card> getBoardCards() {
        return boardCards;
    }

    public Deck getDeck() {
        return deck;
    }

    public int getButtonPosition() {
        return buttonPosition;
    }

    public void setButtonPosition(int buttonPosition) {
        this.buttonPosition = buttonPosition;
    }

    public Seat getLastRaiser() {
        return lastRaiser;
    }

    public void setLastRaiser(Seat lastRaiser) {
        this.lastRaiser = lastRaiser;
    }

    public int getLastRaiseAmount() {
        return lastRaiseAmount;
    }

    public void setLastRaiseAmount(int lastRaiseAmount) {
        this.lastRaiseAmount = lastRaiseAmount;
    }

    public Seat getSeatToAct() {
        return seatToAct;
    }

    public void setSeatToAct(Seat seatToAct) {
        this.seatToAct = seatToAct;
    }

    public boolean isSmallBlindPosted() {
        return smallBlindPosted;
    }

    public void setSmallBlindPosted(boolean smallBlindPosted) {
        this.smallBlindPosted = smallBlindPosted;
    }

    public boolean isBigBlindPosted() {
        return bigBlindPosted;
    }

    public void setBigBlindPosted(boolean bigBlindPosted) {
        this.bigBlindPosted = bigBlindPosted;
    }

    public int getMinimumBuyIn() {
        return minimumBuyIn;
    }

    public int getMaximumBuyIn() {
        return maximumBuyIn;
    }

    public int getSmallBlindAmount() {
        return smallBlindAmount;
    }

    public int getBigBlindAmount() {
        return bigBlindAmount;
    }
}
