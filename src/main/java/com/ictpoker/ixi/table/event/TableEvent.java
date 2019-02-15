package com.ictpoker.ixi.table.event;

import com.ictpoker.ixi.table.exception.TableEventException;
import com.ictpoker.ixi.table.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class TableEvent {

    private final String playerName;
    private final int amount;

    public TableEvent(final String playerName, final int amount) {
        this.playerName = playerName;
        this.amount = amount;
    }

    public TableEvent(final String playerName) {
        this.playerName = playerName;
        this.amount = 0;
    }

    public TableEvent() {
        this.playerName = null;
        this.amount = 0;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getAmount() {
        return amount;
    }

    public abstract TableEvent handle(final Table table) throws TableEventException;
}
