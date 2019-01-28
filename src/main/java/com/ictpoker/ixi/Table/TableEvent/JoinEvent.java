package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.sun.istack.internal.NotNull;

public class JoinEvent extends TableEvent {

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
    public TableEvent handle(@NotNull final Table table)
            throws TableEventException {
        if (!table.isSeatOccupied(getSeatIndex())) {
            if (getAmount() < table.getMinimumBuyIn() || getAmount() > table.getMaximumBuyIn()) {
                throw new TableEventException("Invalid player buy-in amount");
            }

            try {
                table.getSeats().set(getSeatIndex(),
                        new Seat(getPlayer(),
                                getPlayer().deductBalance(getAmount())));
            } catch (Exception e) {
                throw new TableEventException("Player has insufficient balance", e);
            }

            addMessage(String.format("%s joined the table at seat #%d with stack %d",
                    getPlayer().getName(),
                    getSeatIndex(),
                    getAmount()));
        } else {
            throw new TableEventException("The seat is already occupied");
        }

        return this;
    }
}
