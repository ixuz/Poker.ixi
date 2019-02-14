package com.ictpoker.ixi.Table.TableEvent.Info;

import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;

public class NewBettingRoundEvent extends TableEvent {

    @Override
    public TableEvent handle(Table table) throws TableEventException {

        table.setSeatToAct(table.getSeat(table.getButtonPosition()));
        table.setLastRaiseAmount(0);
        table.moveActionToNextPlayer();

        return this;
    }
}
