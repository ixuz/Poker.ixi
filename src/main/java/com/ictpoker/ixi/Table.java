package com.ictpoker.ixi;

import com.ictpoker.ixi.DealerEvent.DealerEvent;
import com.ictpoker.ixi.DealerEvent.DealerEventException;
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

    public Table(@NotNull final int nSeats,
                 @NotNull final int minimumBuyIn,
                 @NotNull final int maximumBuyIn,
                 @NotNull final Dealer dealer)
            throws InvalidSeatCountException {

        state = new State(nSeats, minimumBuyIn, maximumBuyIn);

        this.dealer = dealer;

        LOGGER.info(String.format("New table (seats: %d, buy-in: %d-%d)",
                nSeats,
                getState().getMinimumBuyIn(),
                getState().getMaximumBuyIn()));
    }

    public void handleEventQueue()
            throws DealerEventException, PlayerEventException {

        while (!tableEventQueue.isEmpty()) {
            final ITableEvent tableEvent = tableEventQueue.remove();

            if (tableEvent instanceof PlayerEvent) {
                final PlayerEvent playerEvent = (PlayerEvent) tableEvent;
                dealer.handlePlayerEvent(this, playerEvent);
            }
            if (tableEvent instanceof DealerEvent) {
                final DealerEvent dealerEvent = (DealerEvent) tableEvent;
                dealer.handleDealerEvent(this, dealerEvent);
            }
        }
    }

    public void pushEvent(@NotNull final ITableEvent tableEvent) {

        tableEventQueue.add(tableEvent);
    }

    public Seat join(@NotNull final IPlayer player, @NotNull final int stack)
            throws NoSeatAvailableException, PlayerAlreadySeatedException, IllegalArgumentException {

        if (isPlayerSeated(player)) {
            throw new PlayerAlreadySeatedException();
        }

        final int openSeatIndex = state.seats.indexOf(null);
        if (openSeatIndex != -1) {
            final Seat newPlayerSeat = new Seat(this, player, stack);
            state.seats.set(openSeatIndex, newPlayerSeat);
            return newPlayerSeat;
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

    public int getPlayerSeatIndex(@NotNull final IPlayer player)
            throws PlayerNotSeatedException {

        for (int i=0; i<state.getSeats().size(); i++) {
            final Seat seat = state.getSeats().get(i);
            if (seat != null && seat.getPlayer().equals(player)) {
                return i;
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
        private final int minimumBuyIn;
        private final int maximumBuyIn;
        private int buttonPosition = 0;
        private int pot = 0;

        private State(@NotNull final int nSeats, @NotNull final int minimumBuyIn, @NotNull final int maximumBuyIn)
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
}
