package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Exception.TableStateException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.TableState;
import com.sun.istack.internal.NotNull;

public class SitOutEvent extends TableEvent {

    private final boolean sittingOut;

    public SitOutEvent(@NotNull final Player player, @NotNull final boolean sittingOut)
            throws TableEventException {

        super(player, 0);

        this.sittingOut = sittingOut;
    }

    @Override
    public void handle(@NotNull final TableState tableState)
            throws TableEventException {

        try {
            final Seat seat = tableState.getSeat(getPlayer());

            if (seat.isSittingOut() == sittingOut) {
                if (seat.isSittingOut()) {
                    throw new TableEventException("Player is already sitting out");
                } else {
                    throw new TableEventException("Player is already not sitting out");
                }
            }

            seat.setSittingOut(sittingOut);
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }
    }
}
