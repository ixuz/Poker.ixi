package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Table.Exception.*;
import com.ictpoker.ixi.Player.PlayerEvent.PlayerEventException;
import com.ictpoker.ixi.Table.TableState;
import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MoveButtonEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(MoveButtonEvent.class);
    private final int destinationSeatIndex;

    public MoveButtonEvent(@NotNull final int destinationSeatIndex)
            throws PlayerEventException {

        super(null, 0);

        this.destinationSeatIndex = destinationSeatIndex;
    }

    public int getDestinationSeatIndex() {

        return destinationSeatIndex;
    }

    @Override
    public void handle(@NotNull final TableState tableState)
            throws TableEventException {

        try {
            if (destinationSeatIndex < 0 || destinationSeatIndex >= tableState.getSeats().size()) {
                throw new TableEventException("Can't move button to a non-existant seat");
            }

            tableState.setButtonPosition(destinationSeatIndex);
            LOGGER.info(String.format("Moved dealer button to seat #%s", tableState.getButtonPosition()));

            tableState.setSeatToAct(tableState.getNextSeatToAct(tableState.getButtonPosition(), 0));
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }
    }
}
