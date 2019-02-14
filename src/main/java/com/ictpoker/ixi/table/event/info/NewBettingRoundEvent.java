package com.ictpoker.ixi.table.event.info;

import com.ictpoker.ixi.table.exception.TableEventException;
import com.ictpoker.ixi.table.Table;
import com.ictpoker.ixi.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NewBettingRoundEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(NewBettingRoundEvent.class);

    @Override
    public TableEvent handle(Table table) throws TableEventException {

        table.setSeatToAct(table.getSeat(table.getButtonPosition()));
        table.setLastRaiseAmount(0);
        table.moveActionToNextPlayer();

        return this;
    }
}
