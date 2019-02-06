package com.ictpoker.ixi.Table.TableEvent.Info;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;

public class DealEvent extends TableEvent {

    private final static int CARDS_PER_SEAT = 2;

    public DealEvent() {

        super(null, 0);
    }

    @Override
    public TableEvent handle(final Table table)
            throws TableEventException {

        if (table.getNumberOfActiveSeats() <= 1) {
            throw new TableEventException("Insufficient amount of active players to start a new hand");
        }

        log(String.format("*** HOLE CARDS ***"));

        for (int i=0; i<CARDS_PER_SEAT; i++) {
            for (Seat seat : table.getSeats()) {
                if (seat.isSittingOut()) {
                    continue;
                }

                final Card card = table.getDeck().draw();
                seat.getCards().push(card);
            }
        }

        return this;
    }
}
