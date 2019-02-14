package com.ictpoker.ixi.Table.TableEvent.Info;

import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;

import java.util.ArrayList;
import java.util.List;

public class FinishBettingRoundEvent extends TableEvent {

    protected final static int FLOP = 3;
    protected final static int TURN = 1;
    protected final static int RIVER = 1;

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
                } else if (table.getBoardCards().size() == 0) {
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