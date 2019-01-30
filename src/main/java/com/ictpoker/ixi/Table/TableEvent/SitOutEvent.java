package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Exception.TableStateException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;

import java.util.Optional;

public class SitOutEvent extends TableEvent {

    private final boolean sittingOut;

    public SitOutEvent(final Player player, final boolean sittingOut)
            throws TableEventException {

        super(player, 0);

        this.sittingOut = sittingOut;
    }

    @Override
    public TableEvent handle(final Table table)
            throws TableEventException {

        try {
            final Optional<Seat> seat = table.getSeat(getPlayer());
            seat.orElseThrow(() -> new TableStateException(("Player is not seated at the table")));
            seat.get().setSittingOut(sittingOut);

            if (seat.get().isSittingOut()) {
                addMessage(String.format("%s is sitting out", getPlayer().getName()));
            } else {
                addMessage(String.format("%s is no longer sitting out", getPlayer().getName()));
            }
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }

        return this;
    }
}
