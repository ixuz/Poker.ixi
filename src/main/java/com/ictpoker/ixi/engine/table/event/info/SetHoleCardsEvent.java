package com.ictpoker.ixi.engine.table.event.info;

import com.ictpoker.ixi.engine.commons.Card;
import com.ictpoker.ixi.engine.table.exception.TableEventException;
import com.ictpoker.ixi.engine.table.Seat;
import com.ictpoker.ixi.engine.table.Table;
import com.ictpoker.ixi.engine.table.event.TableEvent;

import java.util.Collection;

public class SetHoleCardsEvent extends TableEvent {

    private final String playerName;
    private final Collection<Card> cards;

    public SetHoleCardsEvent(final String playerName, final Collection<Card> cards) {
        this.playerName = playerName;
        this.cards = cards;
    }

    @Override
    public TableEvent handle(Table table) throws TableEventException {
        final Seat seat = table.getSeatByPlayerName(playerName);
        seat.getCards().clear();
        seat.getCards().addAll(cards);
        return this;
    }
}
