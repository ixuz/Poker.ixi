package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(TableEvent.class);

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

    public void log(final String message) {
        LOGGER.info(message);
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getAmount() {
        return amount;
    }

    public abstract TableEvent handle(final Table table) throws TableEventException;
}
