package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Table.Exception.TableStateException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.sun.istack.internal.NotNull;

import java.util.Optional;

public class LeaveEvent extends TableEvent {

    public LeaveEvent(@NotNull final Player player)
            throws TableEventException {

        super(player, 0);
    }

    @Override
    public TableEvent handle(@NotNull final Table table)
            throws TableEventException {

        try {
            final Optional<Seat> seat = table.getSeat(getPlayer());
            seat.orElseThrow(() -> new TableStateException(("Player is not seated at the table")));
            table.getSeats().set(table.getSeats().indexOf(seat.get()), new Seat());
            addMessage(String.format("%s left the table", getPlayer().getName()));
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }

        return this;
    }
}
