package com.ictpoker.ixi.engine.table.event.info;

import com.ictpoker.ixi.engine.table.exception.TableEventException;
import com.ictpoker.ixi.engine.table.Table;
import com.ictpoker.ixi.engine.table.event.TableEvent;

public class NewBettingRoundEvent extends TableEvent {

    @Override
    public TableEvent handle(Table table) throws TableEventException {

        table.setSeatToAct(table.getSeat(table.getButtonPosition()));
        table.setLastRaiseAmount(0);
        table.moveActionToNextPlayer();

        return this;
    }
}
