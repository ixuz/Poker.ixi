package com.ictpoker.ixi.Table.TableEvent.Info;

import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;

public class DealFlopEvent extends TableEvent {

    @Override
    public TableEvent handle(Table table) throws TableEventException {
        table.getBoardCards().add(table.getDeck().draw());
        table.getBoardCards().add(table.getDeck().draw());
        table.getBoardCards().add(table.getDeck().draw());
        log(String.format("*** FLOP *** %s", table.getBoardCards()));

        for (Seat seat : table.getSeats()) {
            seat.setActed(false);
        }

        if (table.hasAllSeatsActed()) {
            table.addEventFirst(new FinishBettingRoundEvent());
        }

        return this;
    }
}
