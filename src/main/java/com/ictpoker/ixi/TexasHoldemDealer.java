package com.ictpoker.ixi;

import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TexasHoldemDealer extends Dealer {

    private final static Logger LOGGER = LogManager.getLogger(TexasHoldemDealer.class);

    public TexasHoldemDealer(@NotNull final Table table, @NotNull final float speed) {

        super(table, speed);
    }

    @Override
    protected void handleJoin(@NotNull final PlayerEvent playerEvent)
            throws PlayerEventException {

        try {
            // TODO: Temporary stack value
            getTable().join(playerEvent.getPlayer(), 1000);
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
}
