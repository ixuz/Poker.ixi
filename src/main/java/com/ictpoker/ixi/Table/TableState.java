package com.ictpoker.ixi.Table;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Commons.Deck;
import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Table.Exception.*;
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
    private boolean smallBlindPosted = false;
    private boolean bigBlindPosted = false;

    public TableState(@NotNull final int nSeats,
                 @NotNull final int minimumBuyIn,
                 @NotNull final int maximumBuyIn,
                 @NotNull final int smallBlindAmount,
                 @NotNull final int bigBlindAmount)
            throws TableStateException {

        if (nSeats <= 1 || nSeats > MAXIMUM_SEATS) {
            throw new TableStateException("Invalid seat count");
        }

        try {
            this.deck = new Deck();
        } catch (Deck.DuplicateCardException e) {
            throw new TableStateException("Failed to create deck");
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

    public int getPlayerSeatIndex(@NotNull final Player player)
            throws TableStateException {

        return seats.indexOf(getSeat(player));
    }

    public boolean isSeatOccupied(@NotNull final int seatIndex)
            throws TableStateException {

        final Seat seat = getSeat(seatIndex);
        return isSeatOccupied(seat);
    }

    private boolean isSeatOccupied(@NotNull final Seat seat) {

        return (seat.getPlayer() != null);
    }

    private Seat getSeat(@NotNull final int seatIndex)
            throws TableStateException {

        if (seatIndex < 0 || seatIndex >= seats.size()) {
            throw new TableStateException("That seat doesn't exist at this table");
        }

        return seats.get(seatIndex);
    }

    public Seat getSeat(@NotNull final Player player)
            throws TableStateException {

        for (final Seat seat : seats) {
            if (seat.getPlayer() == player) {
                return seat;
            }
        }

        throw new TableStateException("The player is not seated at this table");
    }

    public List<Seat> getOccupiedSeats() {

        final List<Seat> occupiedSeats = new ArrayList<>();
        for (int seatIndex=0; seatIndex<seats.size(); seatIndex++) {
            try {
                if (isSeatOccupied(seatIndex)) {
                    occupiedSeats.add(getSeat(seatIndex));
                }
            } catch (TableStateException e) {
                // TODO: Unhandled exception
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

                if (seat.isSittingOut()) {
                    continue;
                }

                if (!hasSeatActed(seat)) {
                    if (skip > 0) {
                        skip--;
                    } else {
                        return seat;
                    }
                }
            }
        } catch (TableStateException e) {
            throw new TableStateException("Failed to find next seat to act", e);
        }

        return null;
    }

    public Seat getNextSeatToAct(@NotNull final Seat seat, @NotNull int skip)
            throws TableStateException {

        final int seatIndex = seats.indexOf(seat);
        return getNextSeatToAct(seatIndex, skip);
    }

    private boolean hasSeatActed(@NotNull final Seat seat)
            throws TableStateException {

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

        throw new TableStateException("Could not determine if the player has to act or not");
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
            if (seat.isSittingOut()) {
                continue;
            }
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
                getRequiredAmountToCall()));
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
            sb.append(String.format("\n %s ",
                    seat.getPlayer().getName()));

            sb.append("[");
            for (final Card card : seat.getCards()) {
                sb.append(String.format("%s", card));
            }
            sb.append("]");

            sb.append(String.format(", stack: %d, committed: %d",
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

    public void setButtonPosition(@NotNull final int buttonPosition)
            throws TableStateException {

        if (buttonPosition < 0 || buttonPosition >= getSeats().size()) {
            throw new TableStateException("Can't move button to a non-existent seat");
        }

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

    public int getTotalPot() {
        int pot = 0;
        for (Seat seat : seats) {
            pot += seat.getCollected();
        }
        return pot;
    }

    public boolean isSmallBlindPosted() {

        return smallBlindPosted;
    }

    public void setSmallBlindPosted(@NotNull final boolean smallBlindPosted) {

        this.smallBlindPosted = smallBlindPosted;
    }

    public boolean isBigBlindPosted() {

        return bigBlindPosted;
    }

    public void setBigBlindPosted(@NotNull final boolean bigBlindPosted) {

        this.bigBlindPosted = bigBlindPosted;
    }

    public int getRequiredAmountToCall() {

        int requiredToCall;
        if (!isSmallBlindPosted()) {
            requiredToCall = smallBlindAmount;
        } else if (!isBigBlindPosted()) {
            requiredToCall = bigBlindAmount;
        } else {
            requiredToCall = getSeatWithHighestCommit(0).getCommitted() - seatToAct.getCommitted();
        }
        requiredToCall = Math.min(seatToAct.getStack(), requiredToCall);
        return requiredToCall;
    }

    public int getRequiredAmountToRaise() {

        return Math.min(seatToAct.getStack(), getRequiredAmountToCall() + getLastRaiseAmount());
    }
}
