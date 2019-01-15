package com.ictpoker.ixi;

import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class Table {

    public final static int MAXIMUM_SEATS = 10;
    private final static Logger LOGGER = LogManager.getLogger(Table.class);
    private final List<Seat> seats;

    public Table(@NotNull final int nSeats)
            throws InvalidSeatCountException {

        if (nSeats <= 0 || nSeats > MAXIMUM_SEATS) throw new InvalidSeatCountException();

        this.seats = Arrays.asList(new Seat[nSeats]);
    }

    public void join(@NotNull final IPlayer player, @NotNull final int stack)
            throws NoSeatAvailableException, PlayerAlreadySeatedException, IllegalArgumentException {

        if (isPlayerSeated(player)) {
            throw new PlayerAlreadySeatedException();
        }

        final int openSeatIndex = seats.indexOf(null);
        if (openSeatIndex != -1) {
            seats.set(openSeatIndex, new Seat(this, player, stack));
        } else {
            throw new NoSeatAvailableException();
        }
    }

    public void leave(@NotNull final IPlayer player)
            throws PlayerNotSeatedException {

        final Seat seat = getPlayerSeat(player);
        seats.set(seats.indexOf(seat), null);
    }

    public Seat getPlayerSeat(@NotNull final IPlayer player)
            throws PlayerNotSeatedException {

        for (final Seat seat : seats) {
            if (seat != null && seat.getPlayer().equals(player)) {
                return seat;
            }
        }
        throw new PlayerNotSeatedException();
    }

    public boolean isPlayerSeated(@NotNull final IPlayer player) {

        for (final Seat seat : seats) {
            if (seat != null && seat.getPlayer().equals(player)) {
                return true;
            }
        }
        return false;
    }

    public class NoSeatAvailableException extends Exception {}
    public class PlayerAlreadySeatedException extends Exception {}
    public class PlayerNotSeatedException extends Exception {}
    public class InvalidSeatCountException extends Exception {}
}
