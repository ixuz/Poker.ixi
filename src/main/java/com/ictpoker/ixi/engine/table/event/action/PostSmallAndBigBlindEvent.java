package com.ictpoker.ixi.engine.table.event.action;

import com.ictpoker.ixi.engine.table.exception.SeatException;
import com.ictpoker.ixi.engine.table.exception.TableEventException;
import com.ictpoker.ixi.engine.table.exception.TableStateException;
import com.ictpoker.ixi.engine.table.Seat;
import com.ictpoker.ixi.engine.table.Table;
import com.ictpoker.ixi.engine.table.event.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PostSmallAndBigBlindEvent extends TableEvent {

    private static final Logger LOGGER = LogManager.getLogger(PostSmallAndBigBlindEvent.class);

    public PostSmallAndBigBlindEvent(String playerName) {
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

            if (seat.isActed()) {
                throw new TableStateException("player can't post small & big blind, because the player has already acted.");
            }

            if (seat.isFolded()) {
                throw new TableStateException("player can't post small & big blind, because the player has already folded.");
            }

            if (seat.isSittingOut()) {
                throw new TableStateException("player can't post small & big blind, because the player is sitting out.");
            }

            if (!table.getBoardCards().isEmpty()) {
                throw new TableStateException("player can't post small & big blind, because board cards are already present.");
            }

            if (table.getSeatWithHighestCommit(0).getCommitted() > table.getBigBlindAmount()) {
                throw new TableStateException("player can't post small & big blind, because another player has already bet");
            }

            final int add = table.getSmallBlindAmount() + table.getBigBlindAmount();
            if (seat.getStack() < add) {
                throw new TableStateException("player can't post small & big blind, because insufficient stack");
            }

            seat.commitDead(table.getSmallBlindAmount());
            seat.commit(table.getBigBlindAmount());

            LOGGER.info(String.format("%s: posts small & big blinds $%d",
                    getPlayerName(),
                    add));

            if (seat.getStack() == 0) {
                LOGGER.info(String.format("%s is all-in", getPlayerName()));
            }
        } catch (TableStateException | SeatException e) {
            throw new TableEventException("Failed to update table state", e);
        }

        return this;
    }
}
