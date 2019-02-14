package com.ictpoker.ixi.Table.TableEvent.Info;

import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;

public class DealRiverEvent extends TableEvent {

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
