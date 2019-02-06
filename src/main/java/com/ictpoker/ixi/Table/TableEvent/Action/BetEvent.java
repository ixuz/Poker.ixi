package com.ictpoker.ixi.Table.TableEvent.Action;

import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Exception.TableStateException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;

public class BetEvent extends TableEvent {

    public BetEvent(final String playerName,
                    final int amount) {
        super(playerName, amount);
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

            if (!table.isSmallBlindPosted()) {
                throw new TableEventException("The player can't bet, must post the small blind to play");
            }

            if (!table.isBigBlindPosted()) {
                throw new TableEventException("The player can't bet, must post the big blind to play");
            }

            final int toCall = table.getRequiredAmountToCall();
            if (toCall != 0) {
                throw new TableEventException("The player can't bet, since another player have already committed chips");
            }

            final int toRaise = table.getRequiredAmountToRaise();
            if (getAmount() < toRaise) {
                throw new TableEventException(String.format("%s must commit at least $%d to bet",
                        getPlayerName(),
                        toRaise));
            }

            final int actualRaiseAmount = getAmount() - toCall;
            table.setLastRaiseAmount(actualRaiseAmount - seat.getCommitted());

            log(String.format("%s: bets $%d",
                    getPlayerName(),
                    getAmount()));

            try {
                seat.commit(getAmount() - seat.getCommitted());
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
