package com.ictpoker.ixi.engine.table.event.action;

import com.ictpoker.ixi.engine.table.Seat;
import com.ictpoker.ixi.engine.table.Table;
import com.ictpoker.ixi.engine.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SitOutEvent extends TableEvent {

    private static final Logger LOGGER = LogManager.getLogger(SitOutEvent.class);

    private final boolean sittingOut;

    public SitOutEvent(final String playerName, final boolean sittingOut) {

        super(playerName, 0);

        this.sittingOut = sittingOut;
    }

    @Override
    public TableEvent handle(final Table table) {

        Seat seat = table.getSeatByPlayerName(getPlayerName());
        if (seat != null) {
            seat.setSittingOut(sittingOut);
        }

        if (sittingOut) {
            LOGGER.info(String.format("%s is sitting out", getPlayerName()));
        } else {
            LOGGER.info(String.format("%s is no longer sitting out", getPlayerName()));
        }

        return this;
    }
}
