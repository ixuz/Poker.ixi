package com.ictpoker.ixi.Table.TableEvent.Info;

import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;
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
