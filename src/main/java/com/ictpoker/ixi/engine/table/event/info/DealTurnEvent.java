package com.ictpoker.ixi.engine.table.event.info;

import com.ictpoker.ixi.engine.table.exception.TableEventException;
import com.ictpoker.ixi.engine.table.Seat;
import com.ictpoker.ixi.engine.table.Table;
import com.ictpoker.ixi.engine.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DealTurnEvent extends TableEvent {

    private static final Logger LOGGER = LogManager.getLogger(DealTurnEvent.class);

    @Override
    public TableEvent handle(Table table) throws TableEventException {

        table.getBoardCards().add(table.getDeck().draw());
        LOGGER.info(String.format("*** TURN *** %s [%s]",
                table.getBoardCards().subList(0, 3),
                table.getBoardCards().get(3)));

        for (Seat seat : table.getSeats()) {
            seat.setActed(false);
        }

        if (table.hasAllSeatsActed()) {
            table.addEventFirst(new FinishBettingRoundEvent());
        }

        return this;
    }
}
