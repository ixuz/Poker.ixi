package com.ictpoker.ixi.engine.table.event.info;

import com.ictpoker.ixi.engine.table.exception.TableEventException;
import com.ictpoker.ixi.engine.table.Seat;
import com.ictpoker.ixi.engine.table.Table;
import com.ictpoker.ixi.engine.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CollectEvent extends TableEvent {

    private static final Logger LOGGER = LogManager.getLogger(CollectEvent.class);

    @Override
    public TableEvent handle(Table table) throws TableEventException {

        // Return uncontested chips
        final Seat highestCommitSeat = table.getSeatWithHighestCommit(0);
        final Seat secondHighestCommitSeat = table.getSeatWithHighestCommit(1);
        final int commitDifference = highestCommitSeat.getCommitted() - secondHighestCommitSeat.getCommitted();

        if (commitDifference > 0) {
            highestCommitSeat.setCommitted(highestCommitSeat.getCommitted() - commitDifference);
            highestCommitSeat.setStack(highestCommitSeat.getStack() + commitDifference);
            LOGGER.info(String.format("Uncalled bet ($%d) returned to %s",
                    commitDifference,
                    highestCommitSeat.getPlayer().getName()));
        }

        // Collect all committed chips
        for (Seat seat : table.getSeats()) {
            seat.moveCommittedToCollected();
        }

        return this;
    }
}
