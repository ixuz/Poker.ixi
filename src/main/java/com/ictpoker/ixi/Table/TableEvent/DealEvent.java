package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.sun.istack.internal.NotNull;

public class DealEvent extends TableEvent {

    private final int dealerButtonPosition;

    public DealEvent(@NotNull final int dealerButtonPosition)
            throws TableEventException {

        super(null, 0);

        this.dealerButtonPosition = dealerButtonPosition;
    }

    @Override
    public TableEvent handle(@NotNull final Table table)
            throws TableEventException {

        if (table.getNumberOfActiveSeats() <= 1) {
            throw new TableEventException("Insufficient amount of active players to start a new hand");
        }

        table.setButtonPosition(dealerButtonPosition);
        addMessage(String.format("Moved dealer button to seat #%s and dealing cards to players", table.getButtonPosition()));

        final int cardsPerSeat = 2;
        for (int i=0; i<cardsPerSeat; i++) {
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
