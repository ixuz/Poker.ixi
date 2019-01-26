package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.TableState;
import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DealEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(DealEvent.class);

    public DealEvent()
            throws TableEventException {

        super(null, 0);
    }

    @Override
    public void handle(@NotNull final TableState tableState) {

        LOGGER.info("Dealing cards to players");
        final int cardsPerSeat = 2;
        for (int i=0; i<cardsPerSeat; i++) {
            for (Seat seat : tableState.getSeats()) {
                final Card card = tableState.getDeck().draw();
                seat.getCards().push(card);
            }
        }
    }
}
