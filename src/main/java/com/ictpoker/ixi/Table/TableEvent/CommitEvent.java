package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Exception.TableStateException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.TableState;
import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommitEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(CommitEvent.class);

    public CommitEvent(@NotNull final Player player,
                       @NotNull final int amount)
            throws TableEventException {

        super(player, amount);
    }

    @Override
    public void handle(@NotNull final TableState tableState)
            throws TableEventException {

        try {
            final Player player = getPlayer();
            final Seat seat = tableState.getSeat(player);

            final int toCall = tableState.getRequiredAmountToCall();
            final int toRaise = tableState.getRequiredAmountToRaise();
            final int actualRaiseAmount = getAmount() - toCall;

            if (seat != tableState.getSeatToAct()) {
                throw new TableEventException("It's not the player's turn to act");
            }

            if (getAmount() > seat.getStack()) {
                throw new TableEventException("Player can't commit more than the available stack");
            }

            if (!tableState.isSmallBlindPosted() && getAmount() != toCall) {
                throw new TableEventException("The player must post the small blind to play");
            } else if (!tableState.isBigBlindPosted() && getAmount() != toCall) {
                throw new TableEventException("The player must post the big blind to play");
            }

            if (getAmount() == 0) { // Desired check
                if (toCall != 0) {
                    throw new TableEventException(String.format("%s can't check, he must commit at least %d",
                            player.getName(),
                            toCall));
                }
                LOGGER.info(String.format("%s checked",
                        player.getName()));
            } else if (getAmount() > 0 && getAmount() == toCall) { // Desired call
                if (!tableState.isSmallBlindPosted()) {
                    LOGGER.info(String.format("%s posted small blind %d",
                            player.getName(),
                            getAmount()));
                } else if (!tableState.isBigBlindPosted()) {
                    LOGGER.info(String.format("%s posted big blind %d",
                            player.getName(),
                            getAmount()));
                } else {
                    LOGGER.info(String.format("%s called %d",
                            player.getName(),
                            getAmount()));
                }
            } else if (getAmount() > toCall) { // Desired raise
                if (getAmount() < toRaise) {
                    throw new TableEventException(String.format("%s he must commit at least %d to raise",
                            player.getName(),
                            toRaise));
                }
                tableState.setLastRaiseAmount(actualRaiseAmount);
                LOGGER.info(String.format("%s raised %d by committing %d",
                        player.getName(),
                        actualRaiseAmount,
                        getAmount()));
            } else {
                throw new TableEventException("Failed to identify desired player action");
            }

            try {
                seat.commit(getAmount());
            } catch (Exception e) {
                throw new TableEventException("Failed to commit", e);
            }

            if (!tableState.isSmallBlindPosted()) {
                tableState.setSmallBlindPosted(true);
            } else if (!tableState.isBigBlindPosted()) {
                tableState.setBigBlindPosted(true);
            } else {
                seat.setActed(true);
            }

            if (seat.getStack() == 0) {
                LOGGER.info(String.format("%s is all-in",
                        player.getName()));
            }

            tableState.setActionToNextPlayer();
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }
    }
}
