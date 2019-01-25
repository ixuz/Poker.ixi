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

public class SmallBlindEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(SmallBlindEvent.class);

    public SmallBlindEvent(@NotNull final Player player)
            throws PlayerEventException {

        super(player, 0);
    }

    @Override
    public void handle(@NotNull final TableState tableState)
            throws TableEventException {

        try {
            final IPlayer player = getPlayer();
            final Seat seat = tableState.getSeat(player);
            final int committed = Math.min(seat.getStack(), tableState.getSmallBlindAmount());
            seat.setStack(seat.getStack()-committed);
            seat.setCommitted(seat.getCommitted()+committed);
            LOGGER.info(String.format("%s posted small blind: %d", player.getName(), committed));

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
