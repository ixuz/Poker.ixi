package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;

public class DealEvent extends TableEvent {

    private final static int CARDS_PER_SEAT = 2;
    private final int dealerButtonPosition;

    public DealEvent(final int dealerButtonPosition)
            throws TableEventException {

        super(null, 0);

        this.dealerButtonPosition = dealerButtonPosition;
    }

    @Override
    public TableEvent handle(final Table table)
            throws TableEventException {

        if (table.getNumberOfActiveSeats() <= 1) {
            throw new TableEventException("Insufficient amount of active players to start a new hand");
        }

        table.setButtonPosition(dealerButtonPosition);
        addMessage(String.format("Moved dealer button to seat #%s and dealing cards to players", table.getButtonPosition()));

        for (int i=0; i<CARDS_PER_SEAT; i++) {
            for (Seat seat : table.getSeats()) {
                if (seat.isSittingOut()) {
                    continue;
                }

                final Card card = table.getDeck().draw();
                seat.getCards().push(card);
            }
        }

        table.setSeatToAct(table.getSeat(table.getButtonPosition()));
        table.moveActionToNextPlayer();

        return this;
    }
}
