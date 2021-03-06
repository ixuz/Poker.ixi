package com.ictpoker.ixi.engine.table.event.info;

import com.ictpoker.ixi.engine.table.Table;
import com.ictpoker.ixi.engine.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SetBigBlindEvent extends TableEvent {

    private static final Logger LOGGER = LogManager.getLogger(SetBigBlindEvent.class);

    private final int bigBlind;

    public SetBigBlindEvent(int bigBlind) {
        super(null, 0);
        this.bigBlind = bigBlind;
    }

    @Override
    public TableEvent handle(Table table) {

        table.setBigBlindAmount(bigBlind);
        LOGGER.info(String.format("Big blind is now set to: %d", bigBlind));

        return this;
    }
}
