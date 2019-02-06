package com.ictpoker.ixi.Table.TableEvent.Action;

import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Exception.TableStateException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;

public class PostBigBlindEvent extends TableEvent {

    public PostBigBlindEvent(String playerName) {
        super(playerName, 0);
    }

    @Override
    public TableEvent handle(Table table)
            throws TableEventException {
        try {
            final Seat seat = table.getSeatByPlayerName(getPlayerName());
            if (seat == null) {
                throw new TableStateException("Player is not seated at the table");
            }

            if (seat.isActed()) {
                throw new TableStateException("Player can't post big blind, because the player has already acted.");
            }
            if (seat.isFolded()) {
                throw new TableStateException("Player can't post big blind, because the player has already folded.");
            }
            if (seat.isSittingOut()) {
                throw new TableStateException("Player can't post big blind, because the player is sitting out.");
            }
            if (table.getBoardCards().size() > 0) {
                throw new TableStateException("Player can't post big blind, because board cards are already present.");
            }
            if (table.getSeatWithHighestCommit(0).getCommitted() > table.getBigBlindAmount()) {
                throw new TableStateException("Player can't post big blind, because another player has already bet");
            }

            final int add = Math.min(seat.getStack(), table.getBigBlindAmount());
            try {
                seat.commit(add);

                log(String.format("%s: posts big blind $%d",
                        getPlayerName(),
                        add));
            } catch (Exception e) {
                throw new TableEventException("Failed to commit", e);
            }

            if (seat == table.getSeatToAct() && !table.isBigBlindPosted()) {
                table.setLastRaiseAmount(add);
                table.setBigBlindPosted(true);
                table.moveActionToNextPlayer();
            }

            if (seat.getStack() == 0) {
                log(String.format("%s is all-in", getPlayerName()));
            }

            if (table.getSeatToAct() == null && !table.isSmallBlindPosted()) {
                table.setSeatToAct(table.getNextSeatToAct(table.getButtonPosition()));
            }
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }

        return this;
    }
}
