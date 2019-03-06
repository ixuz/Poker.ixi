package com.ictpoker.ixi.engine.table.event.info;

import com.ictpoker.ixi.engine.table.Table;
import com.ictpoker.ixi.engine.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SetTableDetailsEvent extends TableEvent {

    private static final Logger LOGGER = LogManager.getLogger(SetTableDetailsEvent.class);

    private final String tableName;
    private final int seatCount;
    private final int buttonPosition;

    public SetTableDetailsEvent(String tableName, int seatCount, int buttonPosition) {
        super(null, 0);
        this.tableName = tableName;
        this.seatCount = seatCount;
        this.buttonPosition = buttonPosition;
    }

    @Override
    public TableEvent handle(Table table) {

        table.setName(tableName);
        table.setSeatCount(seatCount);
        table.setButtonPosition(buttonPosition);
        LOGGER.info(String.format("table '%s' %d-max Seat #%d is the button", tableName, seatCount, buttonPosition));

        return this;
    }
}
