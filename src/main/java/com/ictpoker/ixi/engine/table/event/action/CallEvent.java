package com.ictpoker.ixi.engine.table.event.action;

import com.ictpoker.ixi.engine.table.exception.SeatException;
import com.ictpoker.ixi.engine.table.exception.TableEventException;
import com.ictpoker.ixi.engine.table.exception.TableStateException;
import com.ictpoker.ixi.engine.table.Seat;
import com.ictpoker.ixi.engine.table.Table;
import com.ictpoker.ixi.engine.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CallEvent extends TableEvent {

    private static final Logger LOGGER = LogManager.getLogger(CallEvent.class);

    public CallEvent(String playerName) {
        super(playerName);
    }

    @Override
    public TableEvent handle(Table table) throws TableEventException {
        try {
            final Seat seat = table.getSeatByPlayerName(getPlayerName());
            if (seat == null) {
                throw new TableStateException("player is not seated at the table");
            }

            if (table.getSeatToAct() != null && seat != table.getSeatToAct()) {
                throw new TableEventException("It's not the player's turn to act");
            }

            if (!table.isSmallBlindPosted()) {
                throw new TableEventException("The player can't call, must post the small blind to play");
            }

            if (!table.isBigBlindPosted()) {
                throw new TableEventException("The player can't call, must post the big blind to play");
            }

            final int toCall = table.getRequiredAmountToCall(); // The minimum amount the player must commit to
            if (toCall == 0) {
                throw new TableEventException(String.format("%s can't call since nobody has bet",
                        getPlayerName()));
            }

            LOGGER.info(String.format("%s: calls $%d",
                    getPlayerName(),
                    toCall));

            seat.commit(toCall);

            seat.setActed(true);

            if (seat.getStack() == 0) {
                LOGGER.info(String.format("%s is all-in", getPlayerName()));
            }

            table.moveActionToNextPlayer();
        } catch (TableStateException | SeatException e) {
            throw new TableEventException("Failed to update table state", e);
        }

        return this;
    }
}
