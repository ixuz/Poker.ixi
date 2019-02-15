package com.ictpoker.ixi.table.event.action;

import com.ictpoker.ixi.table.exception.SeatException;
import com.ictpoker.ixi.table.exception.TableEventException;
import com.ictpoker.ixi.table.exception.TableStateException;
import com.ictpoker.ixi.table.Seat;
import com.ictpoker.ixi.table.Table;
import com.ictpoker.ixi.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CheckEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(CheckEvent.class);

    public CheckEvent(final String playerName) {

        super(playerName, 0);
    }

    @Override
    public TableEvent handle(final Table table)
            throws TableEventException {
        try {
            final Seat seat = table.getSeatByPlayerName(getPlayerName());
            if (seat == null) {
                throw new TableStateException("player is not seated at the table");
            }

            if (!table.isSmallBlindPosted()) {
                throw new TableEventException("The player can't check, must post the small blind to play");
            }

            if (!table.isBigBlindPosted()) {
                throw new TableEventException("The player can't check, must post the big blind to play");
            }

            if (seat != table.getSeatToAct()) {
                throw new TableEventException("It's not the player's turn to act");
            }

            final int toCall = table.getRequiredAmountToCall();

            if (toCall != 0) {
                throw new TableEventException(String.format("%s can't check, he must commit at least call %d",
                        getPlayerName(),
                        toCall));
            }

            LOGGER.info(String.format("%s checks", getPlayerName()));

            try {
                seat.commit(getAmount());
            } catch (SeatException e) {
                throw new TableEventException("Failed to commit", e);
            }

            seat.setActed(true);

            table.moveActionToNextPlayer();
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }

        return this;
    }
}
