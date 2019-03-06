package com.ictpoker.ixi.engine.table.event.info;

import com.ictpoker.ixi.engine.table.exception.TableEventException;
import com.ictpoker.ixi.engine.table.Table;
import com.ictpoker.ixi.engine.table.event.TableEvent;

import java.util.ArrayList;
import java.util.List;

public class FinishBettingRoundEvent extends TableEvent {

    private static final int FLOP = 3;
    private static final int TURN = 1;
    private static final int RIVER = 1;

    @Override
    public TableEvent handle(Table table) throws TableEventException {

        final List<TableEvent> tableEvents = new ArrayList<>();

        // Deal flop, turn, river
        if (table.getNumberOfActiveSeats() > 1) {
            if (table.hasAllSeatsActed()) {
                if (table.getBoardCards().size() == FLOP + TURN + RIVER) {
                    tableEvents.add(new DeliverWinningsEvent());
                } else if (table.getBoardCards().size() == FLOP + TURN) {
                    tableEvents.add(new DealRiverEvent());
                    tableEvents.add(new NewBettingRoundEvent());
                } else if (table.getBoardCards().size() == FLOP) {
                    tableEvents.add(new DealTurnEvent());
                    tableEvents.add(new NewBettingRoundEvent());
                } else if (table.getBoardCards().isEmpty()) {
                    tableEvents.add(new DealFlopEvent());
                    tableEvents.add(new NewBettingRoundEvent());
                }
            }
        } else {
            tableEvents.add(new CollectEvent());
            tableEvents.add(new DeliverWinningsEvent());
        }

        table.addEventsFirst(tableEvents);

        return this;
    }
}
