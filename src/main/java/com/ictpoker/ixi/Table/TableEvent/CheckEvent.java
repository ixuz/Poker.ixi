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

public class CheckEvent extends CommitEvent {

    private final static Logger LOGGER = LogManager.getLogger(CheckEvent.class);

    public CheckEvent(@NotNull final Player player)
            throws PlayerEventException {
        
        super(player, 0);
    }

    @Override
    public void handle(@NotNull final TableState tableState)
            throws TableEventException {

        try {
            final IPlayer player = getPlayer();
            final Seat seat = tableState.getSeat(player);
            final int requiredAmount = Math.min(seat.getStack(), tableState.getHighestCommitAmount() - seat.getCommitted());

            if (requiredAmount != 0) {
                throw new TableEventException(String.format("%s can't check, he must commit at least %d",
                        player.getName(),
                        requiredAmount));
            }

            seat.setActed(true);

            LOGGER.info(String.format("%s checked", player.getName()));

            final Seat nextSeatToAct = tableState.getNextSeatToAct(tableState.getSeatToAct(), 0);
            if (nextSeatToAct != null) {
                tableState.setSeatToAct(nextSeatToAct);
            } else {
                tableState.finishBettingRound();
            }
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        } catch (PlayerNotSeatedException e) {
            throw new TableEventException("The player is not seated at this table", e);
        }
    }
}
