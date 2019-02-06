package com.ictpoker.ixi.Table.TableEvent.Info;

import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;

public class SetTableDetailsEvent extends TableEvent {

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
        log(String.format("Table '%s' %d-max Seat #%d is the button", tableName, seatCount, buttonPosition));

        return this;
    }
}
