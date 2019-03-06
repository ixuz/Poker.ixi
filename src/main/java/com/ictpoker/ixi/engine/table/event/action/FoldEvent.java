package com.ictpoker.ixi.engine.table.event.action;

import com.ictpoker.ixi.engine.table.exception.*;
import com.ictpoker.ixi.engine.table.Seat;
import com.ictpoker.ixi.engine.table.Table;
import com.ictpoker.ixi.engine.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FoldEvent extends TableEvent {

    private static final Logger LOGGER = LogManager.getLogger(FoldEvent.class);

    public FoldEvent(final String playerName) {

        super(playerName, 0);
    }

    @Override
    public TableEvent handle(final Table table)
            throws TableEventException {

        try {
            final Seat seat = table.getSeatByPlayerName(getPlayerName());
            if (seat == null ) {
                throw new TableStateException(("player is not seated at the table"));
            }

            if (seat != table.getSeatToAct()) {
                throw new TableEventException("It's not the player's turn to act");
            }

            seat.setActed(true);
            seat.setFolded(true);

            LOGGER.info(String.format("%s: folds", getPlayerName()));

            table.moveActionToNextPlayer();
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }

        return this;
    }
}
