package com.ictpoker.ixi.table.event.action;

import com.ictpoker.ixi.table.exception.SeatException;
import com.ictpoker.ixi.table.exception.TableEventException;
import com.ictpoker.ixi.table.exception.TableStateException;
import com.ictpoker.ixi.table.Seat;
import com.ictpoker.ixi.table.Table;
import com.ictpoker.ixi.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BetEvent extends TableEvent {

    private static final Logger LOGGER = LogManager.getLogger(BetEvent.class);

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
                throw new TableStateException("player is not seated at the table");
            }

            if (table.getSeatToAct() == null || (table.getSeatToAct() != null && seat != table.getSeatToAct())) {
                throw new TableEventException("It's not the player's turn to act");
            }

            if (!table.isSmallBlindPosted()) {
                throw new TableEventException("The player can't bet, must post the small blind to play");
            }

            if (!table.isBigBlindPosted()) {
                throw new TableEventException("The player can't bet, must post the big blind to play");
            }

            final int toCall = table.getRequiredAmountToCall();
            if (toCall != 0 || table.getLastRaiseAmount() != 0) {
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

            LOGGER.info(String.format("%s: bets $%d",
                    getPlayerName(),
                    getAmount()));

            seat.commit(getAmount() - seat.getCommitted());

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
