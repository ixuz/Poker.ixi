package com.ictpoker.ixi.table.event.action;

import com.ictpoker.ixi.table.exception.SeatException;
import com.ictpoker.ixi.table.exception.TableEventException;
import com.ictpoker.ixi.table.exception.TableStateException;
import com.ictpoker.ixi.table.Seat;
import com.ictpoker.ixi.table.Table;
import com.ictpoker.ixi.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PostSmallBlindEvent extends TableEvent {

    private static final Logger LOGGER = LogManager.getLogger(PostSmallBlindEvent.class);

    public PostSmallBlindEvent(String playerName) {
        super(playerName, 0);
    }

    @Override
    public TableEvent handle(Table table)
            throws TableEventException {
        try {
            final Seat seat = table.getSeatByPlayerName(getPlayerName());
            if (seat == null) {
                throw new TableStateException("player is not seated at the table");
            }

            if (table.getSeatToAct() == null && !table.isSmallBlindPosted()) {
                final Seat seatToPostSmallBlind = table.getNextSeatToAct(table.getButtonPosition());
                if (seat == seatToPostSmallBlind) {
                    table.setSeatToAct(seatToPostSmallBlind);
                } else {
                    throw new TableStateException("player can't post small blind, because it's not the player's turn to act.");
                }
            }

            if (seat.isActed()) {
                throw new TableStateException("player can't post small blind, because the player has already acted.");
            }
            if (seat.isFolded()) {
                throw new TableStateException("player can't post small blind, because the player has already folded.");
            }
            if (seat.isSittingOut()) {
                throw new TableStateException("player can't post small blind, because the player is sitting out.");
            }
            if (table.getBoardCards().size() > 0) {
                throw new TableStateException("player can't post small blind, because board cards are already present.");
            }

            final int add = Math.min(seat.getStack(), table.getSmallBlindAmount());
            seat.commit(add);

            LOGGER.info(String.format("%s: posts small blind $%d",
                    getPlayerName(),
                    add));

            if (seat == table.getSeatToAct() && !table.isSmallBlindPosted()) {
                table.setLastRaiseAmount(add);
                table.setSmallBlindPosted(true);
                table.moveActionToNextPlayer();
            }

            if (seat.getStack() == 0) {
                LOGGER.info(String.format("%s is all-in", getPlayerName()));
            }

        } catch (TableStateException | SeatException e) {
            throw new TableEventException("Failed to update table state", e);
        }

        return this;
    }
}
