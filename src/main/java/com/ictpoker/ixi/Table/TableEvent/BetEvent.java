package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Table.Exception.*;
import com.ictpoker.ixi.Player.IPlayer;
import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Player.PlayerEvent.PlayerEventException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.TableState;
import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BetEvent extends CommitEvent {

    private final static Logger LOGGER = LogManager.getLogger(BetEvent.class);

    public BetEvent(@NotNull final Player player, @NotNull final int amount)
            throws PlayerEventException {

        super(player, amount);
    }

    @Override
    public void handle(@NotNull final TableState tableState)
            throws TableEventException {

        final IPlayer player = getPlayer();
        final Seat seat;
        try {
            seat = tableState.getSeat(player);

            final int requiredAmountToCall = Math.min(seat.getStack(), tableState.getHighestCommitAmount() - seat.getCommitted());
            final int requiredAmountToRaise = requiredAmountToCall + tableState.getLastRaiseAmount();

            if (requiredAmountToRaise > seat.getStack()) {
                throw new TableEventException("Player does not have sufficient stack to raise");
            }

            if (getAmount() > seat.getStack()) {
                throw new TableEventException("Player can't raise more than the stack");
            }

            final int raiseAmount = getAmount() - requiredAmountToCall;

            if (getAmount() < requiredAmountToRaise) {
                throw new TableEventException(String.format("Player must commit at least %d to raise",
                        requiredAmountToRaise));
            }

            tableState.setLastRaiseAmount(raiseAmount);
            seat.setStack(seat.getStack()-getAmount());
            seat.setCommitted(seat.getCommitted()+getAmount());
            seat.setActed(true);

            if (seat.getStack() == 0) {
                LOGGER.info(String.format("%s committed %d (raised %d for a total commitment of %d) and is all-in",
                        player.getName(),
                        getAmount(),
                        raiseAmount,
                        seat.getCommitted()));
            } else {
                LOGGER.info(String.format("%s committed %d (raised %d for a total commitment of %d)",
                        player.getName(),
                        getAmount(),
                        raiseAmount,
                        seat.getCommitted()));
            }

            final Seat nextSeatToAct = tableState.getNextSeatToAct(tableState.getSeatToAct(), 0);
            if (nextSeatToAct != null) {
                tableState.setSeatToAct(nextSeatToAct);
            } else {
                tableState.finishBettingRound();
            }
        } catch (PlayerNotSeatedException e) {
            throw new TableEventException("The player is not seated at this table", e);
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }
    }
}
