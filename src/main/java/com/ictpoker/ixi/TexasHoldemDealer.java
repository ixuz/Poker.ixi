package com.ictpoker.ixi;

import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TexasHoldemDealer extends Dealer {

    public final static int DEFAULT_CARDS_PER_SEAT = 2;
    private final static Logger LOGGER = LogManager.getLogger(TexasHoldemDealer.class);
    private final int buyIn;

    public TexasHoldemDealer(@NotNull final Table table, @NotNull final float speed, @NotNull final int buyIn) {

        super(table, speed);

        this.buyIn = buyIn;
    }

    @Override
    protected void handleJoin(@NotNull final PlayerEvent playerEvent)
            throws PlayerEventException {

        try {
            playerEvent.getPlayer().deductBalance(buyIn);
        } catch (Player.InsufficientBalanceException e) {
            throw new PlayerEventException("Player has insufficient balance", e);
        }

        try {
            getTable().join(playerEvent.getPlayer(), buyIn);
            LOGGER.info(String.format("Welcome to the table %s.", playerEvent.getPlayer().getName()));
        } catch (Table.NoSeatAvailableException e) {
            throw new PlayerEventException("Sorry Sir, the table is full.", e);
        } catch (Table.PlayerAlreadySeatedException e) {
            throw new PlayerEventException("Sorry Sir, you can't occupy more than one seat.", e);
        }
    }

    @Override
    protected void handleLeave(@NotNull final PlayerEvent playerEvent)
            throws PlayerEventException {

        try {
            getTable().leave(playerEvent.getPlayer());
            LOGGER.info(String.format("Thank you for playing %s. See you next time!", playerEvent.getPlayer().getName()));
        } catch (Table.PlayerNotSeatedException e) {
            throw new PlayerEventException("Sorry Sir, you can't occupy more than one seat.", e);
        }
    }

    @Override
    public void deal() {

        shuffleDeck();

        for (int i=0; i<DEFAULT_CARDS_PER_SEAT; i++) {
            for (final Seat seat : getTable().getSeats()) {
                if (seat != null) {
                    seat.pushCard(getDeck().pop());
                }
            }
        }
    }

    @Override
    public void cleanUp() {

        for (final Seat seat : getTable().getSeats()) {
            if (seat != null) {
                while (seat.getCards().size() > 0) {
                        getDeck().push(seat.popCard());
                }
            }
        }
    }
}
