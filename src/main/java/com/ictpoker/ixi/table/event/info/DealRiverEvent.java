package com.ictpoker.ixi.table.event.info;

import com.ictpoker.ixi.table.exception.TableEventException;
import com.ictpoker.ixi.table.Seat;
import com.ictpoker.ixi.table.Table;
import com.ictpoker.ixi.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DealRiverEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(DealRiverEvent.class);

    @Override
    public TableEvent handle(Table table) throws TableEventException {

        table.getBoardCards().add(table.getDeck().draw());
        log(String.format("*** RIVER *** %s [%s] [%s]",
                table.getBoardCards().subList(0, 3),
                table.getBoardCards().get(3),
                table.getBoardCards().get(4)));

        for (Seat seat : table.getSeats()) {
            seat.setActed(false);
        }

        if (table.hasAllSeatsActed()) {
            table.addEventFirst(new FinishBettingRoundEvent());
        }

        return this;
    }
}
