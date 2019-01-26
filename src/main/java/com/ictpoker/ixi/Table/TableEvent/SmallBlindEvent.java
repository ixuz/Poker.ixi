package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Table.Exception.*;
import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Player.Exception.PlayerException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.TableState;
import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SmallBlindEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(SmallBlindEvent.class);

    public SmallBlindEvent(@NotNull final Player player)
            throws TableEventException {

        super(player, 0);
    }

    @Override
    public void handle(@NotNull final TableState tableState)
            throws TableEventException {

        try {
            final Player player = getPlayer();
            final Seat seat = tableState.getSeat(player);
            final int committed = Math.min(seat.getStack(), tableState.getSmallBlindAmount());
            seat.setStack(seat.getStack()-committed);
            seat.setCommitted(seat.getCommitted()+committed);
            LOGGER.info(String.format("%s posted small blind: %d", player.getName(), committed));

            tableState.setActionToNextPlayer();
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }
    }
}
