package com.ictpoker.ixi.Table;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Dealer.Dealer;
import com.ictpoker.ixi.Dealer.DealerEvent.DealerEvent;
import com.ictpoker.ixi.Dealer.DealerEvent.DealerEventException;
import com.ictpoker.ixi.Player.IPlayer;
import com.ictpoker.ixi.Player.PlayerEvent.PlayerEvent;
import com.ictpoker.ixi.Player.PlayerEvent.PlayerEventException;
import com.ictpoker.ixi.Table.TableEvent.ITableEvent;
import com.ictpoker.ixi.Table.TableException.InvalidSeatCountException;
import com.ictpoker.ixi.Table.TableException.NoSeatAvailableException;
import com.ictpoker.ixi.Table.TableException.PlayerAlreadySeatedException;
import com.ictpoker.ixi.Table.TableException.PlayerNotSeatedException;
import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Table {

    private final static Logger LOGGER = LogManager.getLogger(Table.class);
    private final TableState state;
    private final Dealer dealer;
    private final Queue<ITableEvent> tableEventQueue = new LinkedList<>();

    public Table(@NotNull final int nSeats,
                 @NotNull final int minimumBuyIn,
                 @NotNull final int maximumBuyIn,
                 @NotNull final Dealer dealer)
            throws InvalidSeatCountException {

        state = new TableState(nSeats, minimumBuyIn, maximumBuyIn);

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

        final int openSeatIndex = state.getSeats().indexOf(null);
        if (openSeatIndex != -1) {
            final Seat newPlayerSeat = new Seat(this, player, stack);
            state.getSeats().set(openSeatIndex, newPlayerSeat);
            return newPlayerSeat;
        } else {
            throw new NoSeatAvailableException();
        }
    }

    public void leave(@NotNull final IPlayer player)
            throws PlayerNotSeatedException {

        final Seat seat = getPlayerSeat(player);
        state.getSeats().set(state.getSeats().indexOf(seat), null);
    }

    public void addBoardCard(@NotNull final Card card) {

        state.getBoardCards().add(card);
    }

    public List<Card> clearBoardCards() {

        List<Card> boardCopy = new ArrayList<>(state.getBoardCards());
        state.getBoardCards().clear();

        return boardCopy;
    }

    public Seat getPlayerSeat(@NotNull final IPlayer player)
            throws PlayerNotSeatedException {

        for (final Seat seat : state.getSeats()) {
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

        for (final Seat seat : state.getSeats()) {
            if (seat != null && seat.getPlayer().equals(player)) {
                return true;
            }
        }
        return false;
    }

    public List<Seat> getSeats() {

        return state.getSeats();
    }

    public Dealer getDealer() {

        return dealer;
    }

    public int getNumberOfSeatedPlayers() {

        int nSeatedPlayers = 0;
        for (final Seat seat : state.getSeats()) {
            if (seat != null) {
                nSeatedPlayers++;
            }
        }
        return nSeatedPlayers;
    }

    public List<Card> getBoardCards() {

        return new ArrayList<>(state.getBoardCards());
    }

    public TableState getState() {

        return state;
    }
}
