package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Table.Exception.*;
import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.TableState;
import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FoldEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(FoldEvent.class);

    public FoldEvent(@NotNull final Player player)
            throws TableEventException {

        super(player, 0);
    }

    @Override
    public void handle(@NotNull final TableState tableState)
            throws TableEventException {

        try {
            final Player player = getPlayer();
            final Seat seat = tableState.getSeat(player);

            if (seat != tableState.getSeatToAct()) {
                throw new TableEventException("It's not the player's turn to act");
            }

            seat.setActed(true);
            seat.setFolded(true);

            LOGGER.info(String.format("%s folded", player.getName()));

            tableState.setActionToNextPlayer();
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }
    }
}
