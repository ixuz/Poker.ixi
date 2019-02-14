package com.ictpoker.ixi.table.event.info;

import com.ictpoker.ixi.player.Player;
import com.ictpoker.ixi.table.Seat;
import com.ictpoker.ixi.table.Table;
import com.ictpoker.ixi.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SetSeatEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(SetSeatEvent.class);

    private final int stack;
    private final int seatIndex;

    public SetSeatEvent(String playerName, int stack, int seatIndex) {
        super(playerName, 0);
        this.stack = stack;
        this.seatIndex = seatIndex;
    }

    @Override
    public TableEvent handle(Table table) {

        table.getSeats().set(seatIndex, new Seat(new Player(getPlayerName()), stack));
        log(String.format("Seat %d: %s ($%d in chips)", seatIndex+1, getPlayerName(), stack));

        return this;
    }
}
