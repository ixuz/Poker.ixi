package com.ictpoker.ixi.table.event.info;

import com.ictpoker.ixi.commons.Deck;
import com.ictpoker.ixi.table.exception.TableEventException;
import com.ictpoker.ixi.table.Table;
import com.ictpoker.ixi.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NewDeckEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(NewDeckEvent.class);

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
