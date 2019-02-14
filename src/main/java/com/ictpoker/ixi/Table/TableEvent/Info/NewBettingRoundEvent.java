package com.ictpoker.ixi.Table.TableEvent.Info;

import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;
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
