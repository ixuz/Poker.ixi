package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Table.Exception.PlayerNotSeatedException;
import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Player.PlayerEvent.PlayerEventException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.TableState;
import com.sun.istack.internal.NotNull;

public class LeaveEvent extends TableEvent {

    public LeaveEvent(@NotNull final Player player)
            throws PlayerEventException {

        super(player, 0);
    }

    @Override
    public void handle(@NotNull final TableState tableState)
            throws TableEventException {

        try {
            final Seat seat = tableState.getSeat(getPlayer());
            tableState.getSeats().set(tableState.getSeats().indexOf(seat), new Seat());
        } catch (PlayerNotSeatedException e) {
            throw new TableEventException("The player is not seated at this table", e);
        }
    }
}
