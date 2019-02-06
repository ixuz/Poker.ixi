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

    private String name = "Unnamed";
    private List<Seat> seats = Stream.generate(Seat::new).limit(SEATS).collect(toCollection(ArrayList::new));
    private final List<Card> boardCards = new ArrayList<>();
    private Deck deck = new Deck();
    private int buttonPosition = 0;
    private Seat lastRaiser = null;
    private int lastRaiseAmount = 0;
    private Seat seatToAct = null;
    private boolean smallBlindPosted = false;
    private boolean bigBlindPosted = false;
    private Long handId = 0L;

    private int minimumBuyIn;
    private int maximumBuyIn;
    private int smallBlindAmount;
    private int bigBlindAmount;

    public TableState(int minimumBuyIn,
                      int maximumBuyIn,
                      int smallBlindAmount,
                      int bigBlindAmount) {
        this.minimumBuyIn = minimumBuyIn;
        this.maximumBuyIn = maximumBuyIn;
        this.smallBlindAmount = smallBlindAmount;
        this.bigBlindAmount = bigBlindAmount;
    }

    public void reset() {
        seatToAct = null;
        smallBlindPosted = false;
        bigBlindPosted = false;
        deck = new Deck();
        boardCards.clear();
        lastRaiser = null;
        lastRaiseAmount = 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

    public Long getHandId() {
        return handId;
    }

    public void setHandId(Long handId) {
        this.handId = handId;
    }

    public void setMinimumBuyIn(int minimumBuyIn) {
        this.minimumBuyIn = minimumBuyIn;
    }

    public int getMinimumBuyIn() {
        return minimumBuyIn;
    }

    public void setMaximumBuyIn(int maximumBuyIn) {
        this.maximumBuyIn = maximumBuyIn;
    }

    public int getMaximumBuyIn() {
        return maximumBuyIn;
    }

    public void setSmallBlindAmount(int smallBlindAmount) {
        this.smallBlindAmount = smallBlindAmount;
    }

    public int getSmallBlindAmount() {
        return smallBlindAmount;
    }

    public void setBigBlindAmount(int bigBlindAmount) {
        this.bigBlindAmount = bigBlindAmount;
    }

    public int getBigBlindAmount() {
        return bigBlindAmount;
    }

    public void setSeatCount(int seatCount) {
        seats = Stream.generate(Seat::new).limit(seatCount).collect(toCollection(ArrayList::new));
    }
}
