package com.ictpoker.ixi.Table;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Commons.Deck;
import com.ictpoker.ixi.Table.Exception.*;
import com.ictpoker.ixi.Player.IPlayer;
import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class TableState {
    protected final static int MAXIMUM_SEATS = 10;
    protected final static int FLOP = 3;
    protected final static int TURN = 1;
    protected final static int RIVER = 1;
    private final static Logger LOGGER = LogManager.getLogger(Table.class);
    private final List<Seat> seats;
    private final List<Card> boardCards = new ArrayList<>();
    private final int minimumBuyIn;
    private final int maximumBuyIn;
    private final int smallBlindAmount;
    private final int bigBlindAmount;
    private final Deck deck;
    private int buttonPosition = 0;
    private int pot = 0;
    private Seat lastRaiser = null;
    private int lastRaiseAmount;
    private Seat seatToAct;

    public TableState(@NotNull final int nSeats,
                 @NotNull final int minimumBuyIn,
                 @NotNull final int maximumBuyIn,
                 @NotNull final int smallBlindAmount,
                 @NotNull final int bigBlindAmount)
            throws InvalidSeatCountException, TableException {

        if (nSeats <= 1 || nSeats > MAXIMUM_SEATS) {
            throw new InvalidSeatCountException();
        }

        try {
            this.deck = new Deck();
        } catch (Deck.DuplicateCardException e) {
            throw new TableException("Failed to create deck");
        }

        this.seats = new ArrayList<>();
        for (int i=0; i<nSeats; i++) {
            seats.add(new Seat());
        }

        this.minimumBuyIn = minimumBuyIn;
        this.maximumBuyIn = maximumBuyIn;
        this.smallBlindAmount = smallBlindAmount;
        this.bigBlindAmount = bigBlindAmount;

        setLastRaiseAmount(getBigBlindAmount());

        LOGGER.info(toString());
    }

    public int getPlayerSeatIndex(@NotNull final IPlayer player)
            throws PlayerNotSeatedException {

        return seats.indexOf(getSeat(player));
    }

    public boolean isSeatOccupied(@NotNull final int seatIndex)
            throws InvalidSeatException {

        final Seat seat = getSeat(seatIndex);
        return isSeatOccupied(seat);
    }

    private boolean isSeatOccupied(@NotNull final Seat seat) {

        return (seat.getPlayer() != null);
    }

    private Seat getSeat(@NotNull final int seatIndex)
            throws InvalidSeatException {

        if (seatIndex < 0 || seatIndex >= seats.size()) {
            throw new InvalidSeatException("That seat doesn't exist at this table");
        }

        return seats.get(seatIndex);
    }

    public Seat getSeat(@NotNull final IPlayer player)
            throws PlayerNotSeatedException {

        for (final Seat seat : seats) {
            if (seat.getPlayer() == player) {
                return seat;
            }
        }

        throw new PlayerNotSeatedException();
    }

    public List<Seat> getOccupiedSeats() {

        final List<Seat> occupiedSeats = new ArrayList<>();
        for (int seatIndex=0; seatIndex<seats.size(); seatIndex++) {
            try {
                if (isSeatOccupied(seatIndex)) {
                    occupiedSeats.add(getSeat(seatIndex));
                }
            } catch (InvalidSeatException e) {
                e.printStackTrace();
            }
        }
        return occupiedSeats;
    }

    public void setActionToNextPlayer()
            throws TableStateException {

        final Seat nextSeatToAct = getNextSeatToAct(getSeatToAct(), 0);
        if (nextSeatToAct != null) {
            setSeatToAct(nextSeatToAct);
        } else {
            finishBettingRound();
        }
    }

    public Seat getNextSeatToAct(@NotNull final int seatIndex, @NotNull int skip)
            throws TableStateException {

        if (getNumberOfActiveSeats() <= 1) {
            return null;
        }

        try {
            for (int i=0; i<seats.size(); i++) {
                final Seat seat;
                seat = getSeat((seatIndex+i+1)%seats.size());
                if (!hasSeatActed(seat)) {
                    if (skip > 0) {
                        skip--;
                    } else {
                        return seat;
                    }
                }
            }
        } catch (InvalidSeatException | TableException e) {
            throw new TableStateException("Failed to find next seat to act");
        }

        return null;
    }

    public Seat getNextSeatToAct(@NotNull final Seat seat, @NotNull int skip)
            throws TableStateException {

        final int seatIndex = seats.indexOf(seat);
        return getNextSeatToAct(seatIndex, skip);
    }

    private boolean hasSeatActed(@NotNull final Seat seat)
            throws TableException {

        // If the seat has already folded, true
        if (seat.hasFolded()) {
            return true;
        }

        // If the seat can't act because no stack remaining, true
        else if (seat.getStack() == 0) {
            return true;
        }

        // If the seat have not acted at all this round, false
        else if (!seat.hasActed()) {
            return false;
        }

        // If the seat has a stack remaining and haven't committed enough, false
        else if (seat.getStack() > 0 && seat.getCommitted() < getSeatWithHighestCommit(0).getCommitted()) {
            return false;
        }

        // If the seat has a stack remaining and committed enough, true
        else if (seat.getStack() > 0 && seat.getCommitted() == getSeatWithHighestCommit(0).getCommitted()) {
            return true;
        }

        throw new TableException("Could not determine if the player has to act or not");
    }

    public boolean hasAllSeatsActed() {

        int numberOfPlayersLeftToAct = 0;
        for (Seat seat : seats) {
            if (isSeatOccupied(seat) && !seat.hasActed()) {
                if (seat.getStack() > 0) {
                    numberOfPlayersLeftToAct++;
                }
            }
        }

        if (numberOfPlayersLeftToAct > 1) {
            return false;
        }
        return true;
    }

    public int getNumberOfActiveSeats() {
        int numberOfActiveSeats = 0;
        for (Seat seat : seats) {
            if (!isSeatOccupied(seat)) {
                continue;
            }
            if (seat.hasFolded()) {
                continue;
            }
            numberOfActiveSeats++;
        }
        return numberOfActiveSeats;
    }

    public Seat getSeatWithHighestCommit(int skip) {
        Seat[] seatArray = new Seat[seats.size()];
        seatArray = seats.toArray(seatArray);

        Arrays.sort(seatArray, new Comparator<Seat>() {
            @Override
            public int compare(Seat a, Seat b) {
                return (a.getCommitted() > b.getCommitted() ? -1 : 0);
            }
        });

        return seatArray[skip];
    }

    public void setLastRaiseAmount(@NotNull final int lastRaiseAmount) {

        this.lastRaiseAmount = lastRaiseAmount;
    }

    public int getLastRaiseAmount() {

        return (lastRaiseAmount >= getBigBlindAmount() ? lastRaiseAmount : getBigBlindAmount());
    }

    public Seat getSeatToAct() {

        return seatToAct;
    }

    public void setSeatToAct(@NotNull final Seat seatToAct) {

        this.seatToAct = seatToAct;
        LOGGER.info(String.format("%s is next to act... %d required to play...",
                seatToAct.getPlayer().getName(),
                Math.min(seatToAct.getStack(), getSeatWithHighestCommit(0).getCommitted() - seatToAct.getCommitted())));
    }

    public void finishBettingRound()
            throws TableStateException {

        LOGGER.info("Betting round has finished");

        // Return uncontested chips
        final Seat highestCommitSeat = getSeatWithHighestCommit(0);
        final Seat secondHighestCommitSeat = getSeatWithHighestCommit(1);
        final int commitDifference = highestCommitSeat.getCommitted() - secondHighestCommitSeat.getCommitted();

        if (commitDifference > 0) {
            highestCommitSeat.setCommitted(highestCommitSeat.getCommitted() - commitDifference);
            highestCommitSeat.setStack(highestCommitSeat.getStack() + commitDifference);
            LOGGER.info(String.format("Returned %d uncontested chips to %s",
                    commitDifference,
                    highestCommitSeat.getPlayer().getName()));
        }

        // Collect all committed chips
        for (Seat seat : seats) {
            seat.moveCommittedToCollected();
        }

        // Deal flop, turn, river
        if (getNumberOfActiveSeats() > 1) {
            if (hasAllSeatsActed() && getBoardCards().size() == 0) {
                LOGGER.info("Dealing flop");
                getBoardCards().add(getDeck().draw());
                getBoardCards().add(getDeck().draw());
                getBoardCards().add(getDeck().draw());

                for (Seat seat : getSeats()) {
                    seat.setActed(false);
                }
            }

            if (hasAllSeatsActed() && getBoardCards().size() == FLOP) {
                LOGGER.info("Dealing turn");
                getBoardCards().add(getDeck().draw());

                for (Seat seat : getSeats()) {
                    seat.setActed(false);
                }
            }

            if (hasAllSeatsActed() && getBoardCards().size() == FLOP+TURN) {
                LOGGER.info("Dealing river");
                getBoardCards().add(getDeck().draw());

                for (Seat seat : getSeats()) {
                    seat.setActed(false);
                }

            }

            if (hasAllSeatsActed() && getBoardCards().size() == FLOP+TURN+RIVER) {
                LOGGER.info("Hand finished");
            } else {
                setSeatToAct(getNextSeatToAct(getButtonPosition(), 0));
            }
        } else {
            LOGGER.info("Hand finished, no contestants for the pot");
        }
    }

    public String toString() {

        final StringBuilder sb = new StringBuilder();

        sb.append(String.format("Table (seats: %d, buy-in: %d-%d)",
                seats.size(),
                minimumBuyIn,
                maximumBuyIn));

        for (final Seat seat : getOccupiedSeats()) {
            sb.append(String.format("\n %s, stack: %d, committed: %d",
                    seat.getPlayer().getName(),
                    seat.getStack(),
                    seat.getCommitted()));
        }

        return sb.toString();
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

    public List<Card> getBoardCards() {

        return boardCards;
    }

    public Deck getDeck() {

        return deck;
    }

    public List<Seat> getSeats() {

        return seats;
    }

    public void setButtonPosition(@NotNull final int buttonPosition) {

        this.buttonPosition = buttonPosition;
    }

    public int getButtonPosition() {

        return buttonPosition;
    }

    public Seat getLastRaiser() {

        return lastRaiser;
    }

    public void setLastRaiser(@NotNull final Seat lastRaiser) {

        this.lastRaiser = lastRaiser;
    }

    public int getPot() {

        return pot;
    }

    public void setPot(int pot) {

        this.pot = pot;
    }

    public int getTotalCommitted() {

        int committed = 0;
        for (Seat seat : seats) {
            committed += seat.getCommitted();
        }
        return committed;
    }
}
