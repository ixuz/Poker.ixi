package com.ictpoker.ixi.engine.table.event.action;

import com.ictpoker.ixi.engine.table.exception.TableEventException;
import com.ictpoker.ixi.engine.table.exception.TableStateException;
import com.ictpoker.ixi.engine.table.Seat;
import com.ictpoker.ixi.engine.table.Table;
import com.ictpoker.ixi.engine.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeaveEvent extends TableEvent {

    private static final Logger LOGGER = LogManager.getLogger(LeaveEvent.class);

    public LeaveEvent(final String playerName) {

        super(playerName, 0);
    }

    @Override
    public TableEvent handle(final Table table)
            throws TableEventException {

        try {
            final Seat seat = table.getSeatByPlayerName(getPlayerName());
            if (seat == null) {
                throw new TableStateException("player is not seated at the table");
            }
            table.getSeats().set(table.getSeats().indexOf(seat), new Seat());
            LOGGER.info(String.format("%s left the table", getPlayerName()));
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }

        return this;
    }
}
