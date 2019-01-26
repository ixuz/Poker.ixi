package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Table.Exception.InvalidSeatException;
import com.ictpoker.ixi.Table.Exception.PlayerNotSeatedException;
import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Player.Exception.InsufficientBalanceException;
import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Player.Exception.PlayerException;
import com.ictpoker.ixi.Table.Exception.TableStateException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.TableState;
import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JoinEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(JoinEvent.class);
    private final int seatIndex;

    public JoinEvent(@NotNull final Player player,
                     @NotNull final int buyIn,
                     @NotNull final int seatIndex)
            throws TableEventException {

        super(player, buyIn);

        this.seatIndex = seatIndex;
    }

    public int getSeatIndex() {

        return seatIndex;
    }

    @Override
    public void handle(@NotNull final TableState tableState)
            throws TableEventException {

        try {
            if (tableState.getSeat(getPlayer()) != null) {
                throw new TableEventException("The player is already seated");
            }
        } catch (TableStateException e) {
            // Intended exception thrown, the player must not already be seated to join a table.
        }

        try {
            if (!tableState.isSeatOccupied(getSeatIndex())) {

                if (getAmount() < tableState.getMinimumBuyIn() || getAmount() > tableState.getMaximumBuyIn()) {
                    throw new TableEventException("Invalid player buy-in amount");
                }

                tableState.getSeats().set(getSeatIndex(),
                        new Seat(getPlayer(),
                                getPlayer().deductBalance(getAmount())));

                LOGGER.info(String.format("%s joined the table at seat #%d with stack %d",
                        getPlayer().getName(),
                        getSeatIndex(),
                        getAmount()));
            } else {
                throw new TableEventException("The seat is already occupied");
            }
        } catch (InsufficientBalanceException e) {
            throw new TableEventException("Player has insufficient balance", e);
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }
    }
}
