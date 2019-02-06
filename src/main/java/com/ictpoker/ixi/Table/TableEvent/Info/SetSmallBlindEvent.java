package com.ictpoker.ixi.Table.TableEvent.Info;

import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;

public class SetSmallBlindEvent extends TableEvent {

    private final int smallBlind;

    public SetSmallBlindEvent(int smallBlind) {
        super(null, 0);
        this.smallBlind = smallBlind;
    }

    @Override
    public TableEvent handle(Table table) {

        table.setSmallBlindAmount(smallBlind);
        log(String.format("Small blind is now set to: %d", smallBlind));

        return this;
    }
}
