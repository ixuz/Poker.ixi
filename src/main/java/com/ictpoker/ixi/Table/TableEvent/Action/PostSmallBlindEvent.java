package com.ictpoker.ixi.Table.TableEvent.Action;

import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Exception.TableStateException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PostSmallBlindEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(PostSmallBlindEvent.class);

    public PostSmallBlindEvent(String playerName) {
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

            if (table.getSeatToAct() == null && !table.isSmallBlindPosted()) {
                final Seat seatToPostSmallBlind = table.getNextSeatToAct(table.getButtonPosition());
                if (seat == seatToPostSmallBlind) {
                    table.setSeatToAct(seatToPostSmallBlind);
                } else {
                    throw new TableStateException("Player can't post small blind, because it's not the player's turn to act.");
                }
            }

            if (seat.isActed()) {
                throw new TableStateException("Player can't post small blind, because the player has already acted.");
            }
            if (seat.isFolded()) {
                throw new TableStateException("Player can't post small blind, because the player has already folded.");
            }
            if (seat.isSittingOut()) {
                throw new TableStateException("Player can't post small blind, because the player is sitting out.");
            }
            if (table.getBoardCards().size() > 0) {
                throw new TableStateException("Player can't post small blind, because board cards are already present.");
            }

            final int add = Math.min(seat.getStack(), table.getSmallBlindAmount());
            try {
                seat.commit(add);

                log(String.format("%s: posts small blind $%d",
                        getPlayerName(),
                        add));
            } catch (Exception e) {
                throw new TableEventException("Failed to commit", e);
            }

            if (seat == table.getSeatToAct() && !table.isSmallBlindPosted()) {
                table.setLastRaiseAmount(add);
                table.setSmallBlindPosted(true);
                table.moveActionToNextPlayer();
            }

            if (seat.getStack() == 0) {
                log(String.format("%s is all-in", getPlayerName()));
            }

        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }

        return this;
    }
}
