package com.ictpoker.ixi.Table.TableEvent.Action;

import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Exception.TableStateException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;

public class CallEvent extends TableEvent {

    public CallEvent(String playerName, int callAmount) {
        super(playerName, callAmount);
    }

    @Override
    public TableEvent handle(Table table) throws TableEventException {
        try {
            final Seat seat = table.getSeatByPlayerName(getPlayerName());
            if (seat == null) {
                throw new TableStateException("Player is not seated at the table");
            }

            if (seat != table.getSeatToAct()) {
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
                    getAmount()));

            try {
                seat.commit(getAmount());
            } catch (Exception e) {
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
