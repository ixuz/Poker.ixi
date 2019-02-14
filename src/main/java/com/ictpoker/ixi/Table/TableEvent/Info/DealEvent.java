package com.ictpoker.ixi.Table.TableEvent.Info;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DealEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(DealEvent.class);

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
            for (int j=0; j<table.getSeats().size(); j++) {
                int wrappingIndex = (j+1)%table.getSeats().size();
                Seat seat = table.getSeats().get(wrappingIndex);

                if (seat.getPlayer() == null) {
                    continue;
                }

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
