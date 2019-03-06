package com.ictpoker.ixi.engine.table.event.info;

import com.ictpoker.ixi.engine.table.Table;
import com.ictpoker.ixi.engine.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MoveButtonEvent extends TableEvent {

    private static final Logger LOGGER = LogManager.getLogger(MoveButtonEvent.class);

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
