package com.ictpoker.ixi.engine.table.event.info;

import com.ictpoker.ixi.engine.commons.Card;
import com.ictpoker.ixi.engine.table.exception.TableEventException;
import com.ictpoker.ixi.engine.table.Seat;
import com.ictpoker.ixi.engine.table.Table;
import com.ictpoker.ixi.engine.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DealEvent extends TableEvent {

    private static final Logger LOGGER = LogManager.getLogger(DealEvent.class);

    private static final int CARDS_PER_SEAT = 2;

    public DealEvent() {

        super(null, 0);
    }

    @Override
    public TableEvent handle(final Table table)
            throws TableEventException {

        if (table.getNumberOfActiveSeats() <= 1) {
            throw new TableEventException("Insufficient amount of active players to start a new hand");
        }

        LOGGER.info("*** HOLE CARDS ***");

        for (int i=0; i<CARDS_PER_SEAT; i++) {
            for (int j=0; j<table.getSeats().size(); j++) {
                int wrappingIndex = (j+1)%table.getSeats().size();
                Seat seat = table.getSeats().get(wrappingIndex);

                if (seat.getPlayer() == null || seat.isSittingOut()) {
                    continue;
                }

                final Card card = table.getDeck().draw();
                seat.getCards().push(card);
            }
        }

        return this;
    }
}
