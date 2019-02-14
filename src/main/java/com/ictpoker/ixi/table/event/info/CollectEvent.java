package com.ictpoker.ixi.table.event.info;

import com.ictpoker.ixi.table.exception.TableEventException;
import com.ictpoker.ixi.table.Seat;
import com.ictpoker.ixi.table.Table;
import com.ictpoker.ixi.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CollectEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(CollectEvent.class);

    @Override
    public TableEvent handle(Table table) throws TableEventException {

        // Return uncontested chips
        final Seat highestCommitSeat = table.getSeatWithHighestCommit(0);
        final Seat secondHighestCommitSeat = table.getSeatWithHighestCommit(1);
        final int commitDifference = highestCommitSeat.getCommitted() - secondHighestCommitSeat.getCommitted();

        if (commitDifference > 0) {
            highestCommitSeat.setCommitted(highestCommitSeat.getCommitted() - commitDifference);
            highestCommitSeat.setStack(highestCommitSeat.getStack() + commitDifference);
            log(String.format("Uncalled bet ($%d) returned to %s",
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
