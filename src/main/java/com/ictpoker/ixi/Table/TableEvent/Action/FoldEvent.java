package com.ictpoker.ixi.Table.TableEvent.Action;

import com.ictpoker.ixi.Table.Exception.*;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;

public class FoldEvent extends TableEvent {

    public FoldEvent(final String playerName) {

        super(playerName, 0);
    }

    @Override
    public TableEvent handle(final Table table)
            throws TableEventException {

        try {
            final Seat seat = table.getSeatByPlayerName(getPlayerName());
            if (seat == null ) {
                throw new TableStateException(("Player is not seated at the table"));
            }

            if (seat != table.getSeatToAct()) {
                throw new TableEventException("It's not the player's turn to act");
            }

            seat.setActed(true);
            seat.setFolded(true);

            log(String.format("%s: folds", getPlayerName()));

            table.moveActionToNextPlayer();
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }

        return this;
    }
}
