package com.ictpoker.ixi.table.event.action;

import com.ictpoker.ixi.table.exception.SeatException;
import com.ictpoker.ixi.table.exception.TableEventException;
import com.ictpoker.ixi.table.exception.TableStateException;
import com.ictpoker.ixi.table.Seat;
import com.ictpoker.ixi.table.Table;
import com.ictpoker.ixi.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CallEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(CallEvent.class);

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

            log(String.format("%s: calls $%d",
                    getPlayerName(),
                    toCall));

            try {
                seat.commit(toCall);
            } catch (SeatException e) {
                throw new TableEventException("Failed to commit", e);
            }

            seat.setActed(true);

            if (seat.getStack() == 0) {
                log(String.format("%s is all-in", getPlayerName()));
            }

            table.moveActionToNextPlayer();
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }

        return this;
    }
}
