package com.ictpoker.ixi.table.event.info;

import com.ictpoker.ixi.table.Table;
import com.ictpoker.ixi.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SetHandDetailsEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(SetHandDetailsEvent.class);

    private final Long handId;
    private final int smallBlind;
    private final int bigBlind;

    public SetHandDetailsEvent(Long handId, int smallBlind, int bigBlind) {
        super(null, 0);
        this.handId = handId;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
    }

    @Override
    public TableEvent handle(Table table) {

        table.reset();

        table.setHandId(handId);
        table.setSmallBlindAmount(smallBlind);
        table.setBigBlindAmount(bigBlind);
        LOGGER.info(String.format("Poker.ixi Hand #%d:  Hold'em No Limit ($%d/$%d)", handId, smallBlind, bigBlind));

        return this;
    }
}
