package com.ictpoker.ixi.Table.TableEvent.Info;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;

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
