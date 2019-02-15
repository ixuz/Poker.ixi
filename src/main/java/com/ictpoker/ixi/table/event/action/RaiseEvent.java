package com.ictpoker.ixi.table.event.action;

import com.ictpoker.ixi.table.exception.SeatException;
import com.ictpoker.ixi.table.exception.TableEventException;
import com.ictpoker.ixi.table.exception.TableStateException;
import com.ictpoker.ixi.table.Seat;
import com.ictpoker.ixi.table.Table;
import com.ictpoker.ixi.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RaiseEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(RaiseEvent.class);

    public RaiseEvent(final String playerName,
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

            if (seat != table.getSeatToAct()) {
                throw new TableEventException("It's not the player's turn to act");
            }

            if (!table.isSmallBlindPosted()) {
                throw new TableEventException("The player can't raise, must post the small blind to play");
            }

            if (!table.isBigBlindPosted()) {
                throw new TableEventException("The player can't raise, must post the big blind to play");
            }

            final int toCall = table.getRequiredAmountToCall();
            if (toCall == 0 && table.getSeatWithHighestCommit(0).getCommitted() == 0) {
                throw new TableEventException("The player can't raise, since nobody has bet");
            }

            final int toRaise = table.getRequiredAmountToRaise();
            if (getAmount() < toRaise) {
                throw new TableEventException(String.format("%s he must commit at least $%d to raise",
                        getPlayerName(),
                        toRaise));
            }

            final int actualRaiseAmount = getAmount() - toCall;
            if (actualRaiseAmount > seat.getStack()) {
                throw new TableEventException(String.format("player %s can't commit more than the available stack.",
                        seat.getPlayer().getName()));
            }

            final int highestCommitted = table.getSeatWithHighestCommit(0).getCommitted();
            final int raise = getAmount() - highestCommitted;
            table.setLastRaiseAmount(actualRaiseAmount - seat.getCommitted());

            LOGGER.info(String.format("%s: raises $%d to $%d",
                    getPlayerName(),
                    raise,
                    getAmount()));

            try {
                seat.commit(getAmount() - seat.getCommitted());
            } catch (SeatException e) {
                throw new TableEventException("Failed to commit", e);
            }

            seat.setActed(true);

            if (seat.getStack() == 0) {
                LOGGER.info(String.format("%s is all-in", getPlayerName()));
            }

            table.moveActionToNextPlayer();
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }

        return this;
    }
}
