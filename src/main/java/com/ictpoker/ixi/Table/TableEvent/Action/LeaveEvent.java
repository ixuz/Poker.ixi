package com.ictpoker.ixi.Table.TableEvent.Action;

import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Exception.TableStateException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;
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
                throw new TableStateException("Player is not seated at the table");
            }
            table.getSeats().set(table.getSeats().indexOf(seat), new Seat());
            log(String.format("%s left the table", getPlayerName()));
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }

        return this;
    }
}
