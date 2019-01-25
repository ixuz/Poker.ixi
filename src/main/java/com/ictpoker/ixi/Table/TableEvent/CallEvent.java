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

public class CallEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(CallEvent.class);

    public CallEvent(@NotNull final Player player)
            throws PlayerEventException {

        super(player, 0);
    }

    @Override
    public void handle(@NotNull final TableState tableState)
            throws TableEventException {

        try {
            final IPlayer player = getPlayer();
            final Seat seat;
            seat = tableState.getSeat(player);

            final int requiredAmount = Math.min(seat.getStack(), tableState.getHighestCommitAmount() - seat.getCommitted());

            seat.setStack(seat.getStack()-requiredAmount);
            seat.setCommitted(seat.getCommitted()+requiredAmount);
            seat.setActed(true);

            if (seat.getStack() == 0) {
                LOGGER.info(String.format("%s called %d and is all-in", player.getName(), requiredAmount));
            } else {
                LOGGER.info(String.format("%s called %d", player.getName(), requiredAmount));
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
