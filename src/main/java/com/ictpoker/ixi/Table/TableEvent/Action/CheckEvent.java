package com.ictpoker.ixi.Table.TableEvent.Action;

import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Exception.TableStateException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;

public class CheckEvent extends TableEvent {

    public CheckEvent(final String playerName) {

        super(playerName, 0);
    }

    @Override
    public TableEvent handle(final Table table)
            throws TableEventException {
        try {
            final Seat seat = table.getSeatByPlayerName(getPlayerName());
            if (seat == null) {
                throw new TableStateException("Player is not seated at the table");
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

            log(String.format("%s checks",
                    getPlayerName()));

            try {
                seat.commit(getAmount());
            } catch (Exception e) {
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
