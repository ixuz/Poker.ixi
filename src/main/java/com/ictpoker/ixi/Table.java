package com.ictpoker.ixi;

import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Table {

    public final static int MAXIMUM_SEATS = 10;
    private final static Logger LOGGER = LogManager.getLogger(Table.class);
    private final State state;
    private final Dealer dealer;
    private final Queue<ITableEvent> tableEventQueue = new LinkedList<>();

    public Table(@NotNull final int nSeats, @NotNull final Dealer dealer)
            throws InvalidSeatCountException {

        state = new State(nSeats);

        this.dealer = dealer;
    }

    public void handleEventQueue()
            throws PlayerEventException {

        dealer.handleEventQueue(this);
    }

    public void pushEvent(@NotNull final PlayerEvent playerEvent) {

        dealer.pushPlayerEvent(this, playerEvent);
    }

    public void join(@NotNull final IPlayer player, @NotNull final int stack)
            throws NoSeatAvailableException, PlayerAlreadySeatedException, IllegalArgumentException {

        if (isPlayerSeated(player)) {
            throw new PlayerAlreadySeatedException();
        }

        final int openSeatIndex = state.seats.indexOf(null);
        if (openSeatIndex != -1) {
            state.seats.set(openSeatIndex, new Seat(this, player, stack));
        } else {
            throw new NoSeatAvailableException();
        }
    }

    public void leave(@NotNull final IPlayer player)
            throws PlayerNotSeatedException {

        final Seat seat = getPlayerSeat(player);
        state.seats.set(state.seats.indexOf(seat), null);
    }

    public void addBoardCard(@NotNull final Card card) {

        state.boardCards.add(card);
    }

    public List<Card> clearBoardCards() {

        List<Card> boardCopy = new ArrayList<>(state.boardCards);
        state.boardCards.clear();

        return boardCopy;
    }

    public Seat getPlayerSeat(@NotNull final IPlayer player)
            throws PlayerNotSeatedException {

        for (final Seat seat : state.seats) {
            if (seat != null && seat.getPlayer().equals(player)) {
                return seat;
            }
        }
        throw new PlayerNotSeatedException();
    }

    public boolean isPlayerSeated(@NotNull final IPlayer player) {

        for (final Seat seat : state.seats) {
            if (seat != null && seat.getPlayer().equals(player)) {
                return true;
            }
        }
        return false;
    }

    public List<Seat> getSeats() {

        return state.seats;
    }

    public Dealer getDealer() {

        return dealer;
    }

    public int getNumberOfSeatedPlayers() {

        int nSeatedPlayers = 0;
        for (final Seat seat : state.seats) {
            if (seat != null) {
                nSeatedPlayers++;
            }
        }
        return nSeatedPlayers;
    }

    public List<Card> getBoardCards() {

        return new ArrayList<>(state.boardCards);
    }

    public State getState() {

        return state;
    }

    public class NoSeatAvailableException extends Exception {}
    public class PlayerAlreadySeatedException extends Exception {}
    public class PlayerNotSeatedException extends Exception {}
    public class InvalidSeatCountException extends Exception {}

    public class State {

        private final List<Seat> seats;
        private final List<Card> boardCards = new ArrayList<>();
        private int buttonPosition = 0;

        private State(@NotNull final int nSeats)
                throws InvalidSeatCountException {

            if (nSeats <= 0 || nSeats > MAXIMUM_SEATS) {
                throw new InvalidSeatCountException();
            }

            this.seats = Arrays.asList(new Seat[nSeats]);
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
    }
}
