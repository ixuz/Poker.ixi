package com.ictpoker.ixi.Table;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Table.Exception.*;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;
import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class Table extends TableState {

    private final static Logger LOGGER = LogManager.getLogger(Table.class);
    private final Queue<TableEvent> tableEventQueue = new LinkedList<>();

    public Table(@NotNull final int minimumBuyIn,
                 @NotNull final int maximumBuyIn,
                 @NotNull final int smallBlindAmount,
                 @NotNull final int bigBlindAmount) {
        super(minimumBuyIn, maximumBuyIn, smallBlindAmount, bigBlindAmount);
    }

    public void handleEventQueue()
            throws TableException {

        while (!tableEventQueue.isEmpty()) {
            try {
                LOGGER.info(tableEventQueue.remove().handle(this).toString());
            } catch (TableEventException e) {
                throw new TableException("Failed to handle table event", e);
            }
        }
    }

    public void pushEvent(@NotNull final TableEvent tableEvent) {
        tableEventQueue.add(tableEvent);
    }

    public int getPlayerSeatIndex(@NotNull final Player player) {
        return getSeats().indexOf(getSeat(player).orElse(null));
    }

    public boolean isSeatOccupied(@NotNull final int seatIndex) {
        return isSeatOccupied(getSeat(seatIndex));
    }

    private boolean isSeatOccupied(@NotNull final Seat seat) {
        return (seat.getPlayer() != null);
    }

    private Seat getSeat(@NotNull final int seatIndex) {
        return getSeats().get(seatIndex);
    }

    public Optional<Seat> getSeat(@NotNull final Player player) {
        return getSeats().stream().filter(seat -> seat.getPlayer() == player).findFirst();
    }

    public List<Seat> getOccupiedSeats() {
        return getSeats().stream().filter(seat -> seat.getPlayer() != null).collect(Collectors.toList());
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
            for (int i=0; i<getSeats().size(); i++) {
                final Seat seat;
                seat = getSeat((seatIndex+i+1)%getSeats().size());

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
        final int seatIndex = getSeats().indexOf(seat);
        return getNextSeatToAct(seatIndex, skip);
    }

    private boolean hasSeatActed(@NotNull final Seat seat)
            throws TableStateException {
        // If the seat has already folded, true
        if (seat.isFolded()) {
            return true;
        }

        // If the seat can't act because no stack remaining, true
        else if (seat.getStack() == 0) {
            return true;
        }

        // If the seat have not acted at all this round, false
        else if (!seat.isActed()) {
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
        for (Seat seat : getSeats()) {
            if (isSeatOccupied(seat) && !seat.isActed()) {
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
        for (Seat seat : getSeats()) {
            if (seat.isSittingOut()) {
                continue;
            }
            if (!isSeatOccupied(seat)) {
                continue;
            }
            if (seat.isFolded()) {
                continue;
            }
            numberOfActiveSeats++;
        }
        return numberOfActiveSeats;
    }

    public Seat getSeatWithHighestCommit(int skip) {
        Seat[] seatArray = new Seat[getSeats().size()];
        seatArray = getSeats().toArray(seatArray);

        Arrays.sort(seatArray, new Comparator<Seat>() {
            @Override
            public int compare(Seat a, Seat b) {
                return (a.getCommitted() > b.getCommitted() ? -1 : 0);
            }
        });

        return seatArray[skip];
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
        for (Seat seat : getSeats()) {
            seat.moveCommittedToCollected();
        }

        // Deal flop, turn, river
        if (getNumberOfActiveSeats() > 1) {
            if (hasAllSeatsActed() && getBoardCards().size() == 0) {
                getBoardCards().add(getDeck().draw());
                getBoardCards().add(getDeck().draw());
                getBoardCards().add(getDeck().draw());
                LOGGER.info(String.format("Dealing flop %s", getBoardCards()));

                for (Seat seat : getSeats()) {
                    seat.setActed(false);
                }
            }

            if (hasAllSeatsActed() && getBoardCards().size() == FLOP) {
                getBoardCards().add(getDeck().draw());
                LOGGER.info(String.format("Dealing turn %s", getBoardCards()));

                for (Seat seat : getSeats()) {
                    seat.setActed(false);
                }
            }

            if (hasAllSeatsActed() && getBoardCards().size() == FLOP+TURN) {
                getBoardCards().add(getDeck().draw());
                LOGGER.info(String.format("Dealing river %s", getBoardCards()));

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

    public int getTotalPot() {
        return getSeats().stream().map(Seat::getCollected).reduce(0, Integer::sum);
    }

    public int getRequiredAmountToCall() {
        if (!isSmallBlindPosted()) {
            return Math.min(getSeatToAct().getStack(), getSmallBlindAmount());
        } else if (!isBigBlindPosted()) {
            return Math.min(getSeatToAct().getStack(), getBigBlindAmount());
        } else {
            return Math.min(getSeatToAct().getStack(), getSeatWithHighestCommit(0).getCommitted() - getSeatToAct().getCommitted());
        }
    }

    public int getRequiredAmountToRaise() {
        return Math.min(getSeatToAct().getStack(), getRequiredAmountToCall() + getLastRaiseAmount());
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(String.format("Table (seats: %d, buy-in: %d-%d), stakes %d/%d",
                getSeats().size(),
                getMinimumBuyIn(),
                getMaximumBuyIn(),
                getSmallBlindAmount(),
                getBigBlindAmount()));

        sb.append(String.format("\n Total pot: %d",
                getTotalPot()));

        sb.append("\n Board [ ");
        for (final Card card : getBoardCards()) {
            sb.append(String.format("%s ", card));
        }
        sb.append("]");

        for (final Seat seat : getOccupiedSeats()) {
            sb.append(String.format("\n %s ",
                    seat.getPlayer().getName()));

            sb.append("[ ");
            for (final Card card : seat.getCards()) {
                sb.append(String.format("%s ", card));
            }
            sb.append("]");

            sb.append(String.format(", stack: %d, committed: %d, collected: %d",
                    seat.getStack(),
                    seat.getCommitted(),
                    seat.getCollected()));
        }

        return sb.toString();
    }
}
