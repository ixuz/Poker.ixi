package com.ictpoker.ixi.table.event.info;

import com.ictpoker.ixi.table.Table;
import com.ictpoker.ixi.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MoveButtonEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(MoveButtonEvent.class);

    private final int buttonPosition;

    public MoveButtonEvent(int buttonPosition) {
        super(null, 0);
        this.buttonPosition = buttonPosition;
    }

    @Override
    public TableEvent handle(Table table) {

        table.setButtonPosition(buttonPosition);

        LOGGER.info(String.format("Moved button to seat #%d",
                buttonPosition));

        return this;
    }
}
