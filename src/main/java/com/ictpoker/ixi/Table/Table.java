package com.ictpoker.ixi.Table;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Table.Exception.*;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;
import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Table extends TableState {

    private final static Logger LOGGER = LogManager.getLogger(Table.class);
    private final Queue<TableEvent> tableEventQueue = new LinkedList<>();

    public Table(@NotNull final int nSeats,
                 @NotNull final int minimumBuyIn,
                 @NotNull final int maximumBuyIn,
                 @NotNull final int smallBlindAmount,
                 @NotNull final int bigBlindAmount)
            throws TableStateException {

        super(nSeats, minimumBuyIn, maximumBuyIn, smallBlindAmount, bigBlindAmount);
    }

    public void handleEventQueue()
            throws TableException {

        while (!tableEventQueue.isEmpty()) {
            final TableEvent tableEvent = tableEventQueue.remove();

            try {
                tableEvent.handle(this);
            } catch (TableEventException e) {
                throw new TableException("Failed to handle table event", e);
            }
        }
    }

    public void pushEvent(@NotNull final TableEvent tableEvent) {

        tableEventQueue.add(tableEvent);
    }

    public String toString() {

        final StringBuilder sb = new StringBuilder();


        sb.append(String.format("Table (seats: %d, buy-in: %d-%d), stakes %d/%d",
                getSeats().size(),
                getMinimumBuyIn(),
                getMaximumBuyIn(),
                getSmallBlindAmount(),
                getBigBlindAmount()));

        sb.append(String.format("\n Total pot: %d",
                getTotalPot()));

        sb.append("\n Board [ ");
        for (final Card card : getBoardCards()) {
            sb.append(String.format("%s ", card));
        }
        sb.append("]");

        for (final Seat seat : getOccupiedSeats()) {
            sb.append(String.format("\n %s ",
                    seat.getPlayer().getName()));

            sb.append("[ ");
            for (final Card card : seat.getCards()) {
                sb.append(String.format("%s ", card));
            }
            sb.append("]");

            sb.append(String.format(", stack: %d, committed: %d, collected: %d",
                    seat.getStack(),
                    seat.getCommitted(),
                    seat.getCollected()));
        }

        return sb.toString();
    }
}
