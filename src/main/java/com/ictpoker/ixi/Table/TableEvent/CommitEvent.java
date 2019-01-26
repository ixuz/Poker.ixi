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

            final int requiredAmountToCall = Math.min(seat.getStack(), tableState.getSeatWithHighestCommit(0).getCommitted() - seat.getCommitted());
            final int requiredAmountToRaise = requiredAmountToCall + tableState.getLastRaiseAmount();
            final int actualRaiseAmount = getAmount() - requiredAmountToCall;

/*            if (requiredAmountToCall != 0) {
                throw new TableEventException(String.format("%s can't check, he must commit at least %d",
                        player.getName(),
                        requiredAmountToCall));
            }*/

/*            if (requiredAmountToRaise > seat.getStack()) {
                throw new TableEventException("Player does not have sufficient stack to raise");
            }*/

            if (getAmount() > seat.getStack()) {
                throw new TableEventException("Player can't commit more than the available stack");
            }

            if (getAmount() == 0) { // Desired check
                if (requiredAmountToCall != 0) {
                    throw new TableEventException(String.format("%s can't check, he must commit at least %d",
                            player.getName(),
                            requiredAmountToCall));
                }
                LOGGER.info(String.format("%s checked",
                        player.getName()));
            } else if (getAmount() > 0 && getAmount() == requiredAmountToCall) { // Desired call
                LOGGER.info(String.format("%s called %d",
                        player.getName(),
                        getAmount()));
            } else if (getAmount() > requiredAmountToCall) { // Desired raise
                if (getAmount() < requiredAmountToRaise) {
                    throw new TableEventException(String.format("%s he must commit at least %d to raise",
                            player.getName(),
                            requiredAmountToRaise));
                }
                tableState.setLastRaiseAmount(actualRaiseAmount);
                LOGGER.info(String.format("%s raised %d by committing %d",
                        player.getName(),
                        actualRaiseAmount,
                        getAmount()));
            } else {
                throw new TableEventException("Failed to identify desired player action");
            }

            seat.setStack(seat.getStack()-getAmount());
            seat.setCommitted(seat.getCommitted()+getAmount());
            seat.setActed(true);

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
