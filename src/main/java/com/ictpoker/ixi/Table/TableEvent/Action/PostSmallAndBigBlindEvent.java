package com.ictpoker.ixi.Table.TableEvent.Action;

import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Exception.TableStateException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PostSmallAndBigBlindEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(PostSmallAndBigBlindEvent.class);

    public PostSmallAndBigBlindEvent(String playerName) {
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
                throw new TableStateException("Player can't post small & big blind, because the player has already acted.");
            }

            if (seat.isFolded()) {
                throw new TableStateException("Player can't post small & big blind, because the player has already folded.");
            }

            if (seat.isSittingOut()) {
                throw new TableStateException("Player can't post small & big blind, because the player is sitting out.");
            }

            if (table.getBoardCards().size() > 0) {
                throw new TableStateException("Player can't post small & big blind, because board cards are already present.");
            }

            if (table.getSeatWithHighestCommit(0).getCommitted() > table.getBigBlindAmount()) {
                throw new TableStateException("Player can't post small & big blind, because another player has already bet");
            }

            final int add = table.getSmallBlindAmount() + table.getBigBlindAmount();
            if (seat.getStack() < add) {
                throw new TableStateException("Player can't post small & big blind, because insufficient stack");
            }

            try {
                seat.commitDead(table.getSmallBlindAmount());
                seat.commit(table.getBigBlindAmount());

                log(String.format("%s: posts small & big blinds $%d",
                        getPlayerName(),
                        add));
            } catch (Exception e) {
                throw new TableEventException("Failed to commit", e);
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
