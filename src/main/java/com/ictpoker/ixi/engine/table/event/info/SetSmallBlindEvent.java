package com.ictpoker.ixi.engine.table.event.info;

import com.ictpoker.ixi.engine.table.Table;
import com.ictpoker.ixi.engine.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SetSmallBlindEvent extends TableEvent {

    private static final Logger LOGGER = LogManager.getLogger(SetSmallBlindEvent.class);

    private final int smallBlind;

    public SetSmallBlindEvent(int smallBlind) {
        super(null, 0);
        this.smallBlind = smallBlind;
    }

    @Override
    public TableEvent handle(Table table) {

        table.setSmallBlindAmount(smallBlind);
        LOGGER.info(String.format("Small blind is now set to: %d", smallBlind));

        return this;
    }
}
