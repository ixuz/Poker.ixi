package com.ictpoker.ixi.table;

import com.google.common.collect.Lists;
import com.ictpoker.ixi.player.Player;
import com.ictpoker.ixi.table.exception.*;
import com.ictpoker.ixi.table.event.info.CollectEvent;
import com.ictpoker.ixi.table.event.info.FinishBettingRoundEvent;
import com.ictpoker.ixi.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class Table extends TableState {

    private final static Logger LOGGER = LogManager.getLogger(Table.class);
    private final Deque<TableEvent> tableEventQueue = new LinkedList<>();

    public Table(final int minimumBuyIn,
                 final int maximumBuyIn,
                 final int smallBlindAmount,
                 final int bigBlindAmount) {
        super(minimumBuyIn, maximumBuyIn, smallBlindAmount, bigBlindAmount);
    }

    public void handleEventQueue()
            throws TableException {

        while (!tableEventQueue.isEmpty()) {
            try {
                tableEventQueue.removeFirst().handle(this);
            } catch (TableEventException e) {
                throw new TableException("Failed to handle table event", e);
            }
        }
    }

    public void addEventLast(final TableEvent tableEvent) {
        tableEventQueue.addLast(tableEvent);
    }

    public void addEventFirst(final TableEvent tableEvent) {
        tableEventQueue.addFirst(tableEvent);
    }

    public void addEventsFirst(final List<TableEvent> tableEvents) {
        Lists.reverse(tableEvents).forEach(tableEventQueue::addFirst);
    }

    public void addEvents(final Collection<TableEvent> tableEvents) {
        tableEventQueue.addAll(tableEvents);
    }

    public int getPlayerSeatIndex(final Player player) {
        return getSeats().indexOf(getSeat(player));
    }

    public int getSeatIndexByPlayerName(final String playerName) {
        Seat seat = getSeatByPlayerName(playerName);
        if (seat != null) {
            return getSeats().indexOf(seat);
        }
        return -1;
    }

    public Seat getSeatByPlayerName(final String playerName) {
        return getSeats()
                .stream()
                .filter(x -> x != null && x.getPlayer() != null && x.getPlayer().getName().equals(playerName))
                .findFirst().orElse(null);
    }

    public Player getPlayerByName(final String playerName) {

        Seat seat = getSeatByPlayerName(playerName);
        if (seat != null) {
            return seat.getPlayer();
        }
        return null;
    }

    public boolean isSeatOccupied(final int seatIndex) {
        return isSeatOccupied(getSeat(seatIndex));
    }

    private boolean isSeatOccupied(final Seat seat) {
        return (seat.getPlayer() != null);
    }

    public Seat getSeat(final int seatIndex) {
        return getSeats().get(seatIndex);
    }

    public Seat getSeat(final Player player) {
        return getSeats().stream().filter(seat -> seat.getPlayer() == player).findFirst().orElse(null);
    }

    public List<Seat> getOccupiedSeats() {
        return getSeats().stream().filter(seat -> seat.getPlayer() != null).collect(Collectors.toList());
    }

    public void moveActionToNextPlayer() {
        final Seat nextSeatToAct = getNextSeatToAct(getSeatToAct());
        if (nextSeatToAct != null) {
            setSeatToAct(nextSeatToAct);
        } else {
            addEventsFirst(Arrays.asList(new CollectEvent(), new FinishBettingRoundEvent()));
        }
    }

    public Seat getNextSeatToAct(final int seatIndex) {
        if (getNumberOfActiveSeats() <= 1) {
            return null;
        }

        for (int i=0; i<getSeats().size(); i++) {
            final Seat seat = getSeat((seatIndex+i+1)%getSeats().size());
            if (!seat.isSittingOut() && !hasSeatActed(seat)) {
                return seat;
            }
        }

        return null;
    }

    public Seat getNextSeatToAct(final Seat seat) {
        return getNextSeatToAct(getSeats().indexOf(seat));
    }

    private boolean hasSeatActed(final Seat seat) {
        if (seat.isFolded() || seat.getStack() == 0) {
            return true;
        } else if (!seat.isActed()) {
            return false;
        } else {
            return seat.getCommitted() == getSeatWithHighestCommit(0).getCommitted();
        }
    }

    public boolean hasAllSeatsActed() {
        final int numberOfPlayersLeftToAct = getSeats().stream()
                .filter(seat -> isSeatOccupied(seat)
                        && !seat.isActed()
                        && !seat.isFolded())
                .collect(Collectors.toList()).size();
        return numberOfPlayersLeftToAct <= 1;
    }

    public List<Seat> getActiveSeats() {
        return getSeats().stream()
                .filter(seat -> isSeatOccupied(seat) && !seat.isSittingOut() && !seat.isFolded())
                .collect(Collectors.toList());
    }

    public int getNumberOfActiveSeats() {
        return getSeats().stream()
                .filter(seat -> isSeatOccupied(seat) && !seat.isSittingOut() && !seat.isFolded())
                .collect(Collectors.toList()).size();
    }

    public Seat getSeatWithHighestCommit(int skip) {
        Seat[] seatArray = new Seat[getSeats().size()];
        seatArray = getSeats().toArray(seatArray);
        Arrays.sort(seatArray, (a, b) -> (a.getCommitted() > b.getCommitted() ? -1 : 0));
        return seatArray[skip];
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
        final int toRaise = Math.max(getRequiredAmountToCall() + getLastRaiseAmount(), getBigBlindAmount());
        return Math.min(getSeatToAct().getStack(), toRaise);
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("table (seats: %d, buy-in: %d-%d), stakes %d/%d",
                getSeats().size(),
                getMinimumBuyIn(),
                getMaximumBuyIn(),
                getSmallBlindAmount(),
                getBigBlindAmount()));
        sb.append("\nTotal pot: ").append(getTotalPot());
        sb.append("\nBoard ").append(getBoardCards());
        getOccupiedSeats().forEach(seat -> sb.append("\n").append(seat.toString()));
        return sb.toString();
    }
}
