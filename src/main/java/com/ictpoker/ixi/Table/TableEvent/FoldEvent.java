package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Table.Exception.*;
import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.sun.istack.internal.NotNull;

import java.util.Optional;

public class FoldEvent extends TableEvent {

    public FoldEvent(@NotNull final Player player)
            throws TableEventException {

        super(player, 0);
    }

    @Override
    public TableEvent handle(@NotNull final Table table)
            throws TableEventException {

        try {
            final Optional<Seat> seat = table.getSeat(getPlayer());
            seat.orElseThrow(() -> new TableStateException(("Player is not seated at the table")));

            if (seat.get() != table.getSeatToAct()) {
                throw new TableEventException("It's not the player's turn to act");
            }

            seat.get().setActed(true);
            seat.get().setFolded(true);

            addMessage(String.format("%s folded", getPlayer().getName()));

            table.setActionToNextPlayer();
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }

        return this;
    }
}
