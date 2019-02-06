package com.ictpoker.ixi.Table.TableEvent.Info;

import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;

public class MoveButtonEvent extends TableEvent {

    private final int buttonPosition;

    public MoveButtonEvent(int buttonPosition) {
        super(null, 0);
        this.buttonPosition = buttonPosition;
    }

    @Override
    public TableEvent handle(Table table) {

        table.setButtonPosition(buttonPosition);

        log(String.format("Moved button to seat #%d",
                buttonPosition));

        return this;
    }
}
