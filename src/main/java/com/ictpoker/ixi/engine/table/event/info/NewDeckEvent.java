package com.ictpoker.ixi.engine.table.event.info;

import com.ictpoker.ixi.engine.commons.Deck;
import com.ictpoker.ixi.engine.table.exception.TableEventException;
import com.ictpoker.ixi.engine.table.Table;
import com.ictpoker.ixi.engine.table.event.TableEvent;

public class NewDeckEvent extends TableEvent {

    private final Deck deck;

    public NewDeckEvent(Deck deck) {
        this.deck = deck;
    }

    @Override
    public TableEvent handle(Table table) throws TableEventException {

        table.setDeck(deck);

        return this;
    }
}
