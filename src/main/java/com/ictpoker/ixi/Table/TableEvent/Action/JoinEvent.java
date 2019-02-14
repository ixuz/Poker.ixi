package com.ictpoker.ixi.Table.TableEvent.Action;

import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JoinEvent extends TableEvent {

    private final static Logger LOGGER = LogManager.getLogger(JoinEvent.class);

    private final int seatIndex;

    public JoinEvent(final String playerName,
                     final int buyIn,
                     final int seatIndex) {
        super(playerName, buyIn);

        this.seatIndex = seatIndex;
    }

    public int getSeatIndex() {
        return seatIndex;
    }

    @Override
    public TableEvent handle(final Table table)
            throws TableEventException {
        if (!table.isSeatOccupied(getSeatIndex())) {
            if (getAmount() < table.getMinimumBuyIn() || getAmount() > table.getMaximumBuyIn()) {
                throw new TableEventException("Invalid player buy-in amount");
            }

            try {
                table.getSeats().set(getSeatIndex(),
                        new Seat(new Player(getPlayerName()), getAmount()));
            } catch (Exception e) {
                throw new TableEventException("Player has insufficient balance", e);
            }

            log(String.format("%s joined the table at seat #%d with stack %d",
                    getPlayerName(),
                    getSeatIndex(),
                    getAmount()));
        } else {
            throw new TableEventException("The seat is already occupied");
        }

        return this;
    }
}
