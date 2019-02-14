package com.ictpoker.ixi.Table.TableEvent.Info;

import com.ictpoker.ixi.Commons.Deck;
import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;

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
