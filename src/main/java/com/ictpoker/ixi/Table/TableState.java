package com.ictpoker.ixi.Table;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Commons.Deck;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

@Data
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
}
