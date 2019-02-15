package com.ictpoker.ixi.table.event.info;

import com.ictpoker.ixi.table.exception.TableEventException;
import com.ictpoker.ixi.table.Table;
import com.ictpoker.ixi.table.event.TableEvent;

public class NewBettingRoundEvent extends TableEvent {

    @Override
    public TableEvent handle(Table table) throws TableEventException {

        table.setSeatToAct(table.getSeat(table.getButtonPosition()));
        table.setLastRaiseAmount(0);
        table.moveActionToNextPlayer();

        return this;
    }
}
