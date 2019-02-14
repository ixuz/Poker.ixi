package com.ictpoker.ixi.Table.TableEvent.Action;

import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SitOutEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(SitOutEvent.class);

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
            log(String.format("%s is sitting out", getPlayerName()));
        } else {
            log(String.format("%s is no longer sitting out", getPlayerName()));
        }

        return this;
    }
}
