package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Exception.TableStateException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.TableState;
import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DealEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(DealEvent.class);
    private final int dealerButtonPosition;

    public DealEvent(@NotNull final int dealerButtonPosition)
            throws TableEventException {

        super(null, 0);

        this.dealerButtonPosition = dealerButtonPosition;
    }

    @Override
    public void handle(@NotNull final TableState tableState)
            throws TableEventException {

        if (tableState.getNumberOfActiveSeats() <= 1) {
            throw new TableEventException("Insufficient amount of active players to start a new hand");
        }

        try {
            tableState.setButtonPosition(dealerButtonPosition);
            LOGGER.info(String.format("Moved dealer button to seat #%s", tableState.getButtonPosition()));

            LOGGER.info("Dealing cards to players");
            final int cardsPerSeat = 2;
            for (int i=0; i<cardsPerSeat; i++) {
                for (Seat seat : tableState.getSeats()) {
                    if (seat.isSittingOut()) {
                        continue;
                    }

                    final Card card = tableState.getDeck().draw();
                    seat.getCards().push(card);
                }
            }

            tableState.setSeatToAct(tableState.getNextSeatToAct(tableState.getButtonPosition(), 0));
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }
    }
}
