package com.ictpoker.ixi.table.event.action;

import com.ictpoker.ixi.table.exception.TableEventException;
import com.ictpoker.ixi.table.exception.TableStateException;
import com.ictpoker.ixi.table.Seat;
import com.ictpoker.ixi.table.Table;
import com.ictpoker.ixi.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeaveEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(LeaveEvent.class);

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
            log(String.format("%s left the table", getPlayerName()));
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }

        return this;
    }
}
