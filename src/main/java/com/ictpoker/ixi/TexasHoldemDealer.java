package com.ictpoker.ixi;

import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TexasHoldemDealer extends Dealer {

    public final static int CARDS_PER_SEAT = 2;
    public final static int CARDS_FLOP = 3;
    public final static int CARDS_TURN = 1;
    public final static int CARDS_RIVER = 1;
    private final static Logger LOGGER = LogManager.getLogger(TexasHoldemDealer.class);
    private final int buyIn;

    public TexasHoldemDealer(@NotNull final float speed, @NotNull final int buyIn)
            throws DealerException {

        super(speed);

        this.buyIn = buyIn;
    }

    @Override
    protected void handleJoin(@NotNull final Table table, @NotNull final PlayerEvent playerEvent)
            throws PlayerEventException {

        try {
            playerEvent.getPlayer().deductBalance(buyIn);
        } catch (Player.InsufficientBalanceException e) {
            throw new PlayerEventException("Player has insufficient balance", e);
        }

        try {
            table.join(playerEvent.getPlayer(), buyIn);
            LOGGER.info(String.format("Welcome to the table %s.", playerEvent.getPlayer().getName()));
        } catch (Table.NoSeatAvailableException e) {
            throw new PlayerEventException("Sorry Sir, the table is full.", e);
        } catch (Table.PlayerAlreadySeatedException e) {
            throw new PlayerEventException("Sorry Sir, you can't occupy more than one seat.", e);
        }
    }

    @Override
    protected void handleLeave(@NotNull final Table table, @NotNull final PlayerEvent playerEvent)
            throws PlayerEventException {

        try {
            table.leave(playerEvent.getPlayer());
            LOGGER.info(String.format("Thank you for playing %s. See you next time!", playerEvent.getPlayer().getName()));
        } catch (Table.PlayerNotSeatedException e) {
            throw new PlayerEventException("Sorry Sir, you can't occupy more than one seat.", e);
        }
    }

    @Override
    public void dealHoleCards(@NotNull final Table table) {

        shuffleDeck();

        for (int i = 0; i< CARDS_PER_SEAT; i++) {
            for (final Seat seat : table.getSeats()) {
                if (seat != null) {
                    seat.pushCard(getDeck().draw());
                }
            }
        }
    }

    @Override
    public void dealFlop(@NotNull final Table table) {

        LOGGER.info("Dealing flop...");

        for (int i=0; i<CARDS_FLOP; i++) {
            table.addBoardCard(getDeck().draw());
        }

        final StringBuilder sb = new StringBuilder();
        for (final Card card : table.getBoardCards()) {
            sb.append(String.format("[%s] ", card));
        }
        LOGGER.info(sb.toString());
    }

    @Override
    public void dealTurn(@NotNull final Table table) {

        LOGGER.info("Dealing turn...");

        for (int i=0; i<CARDS_TURN; i++) {
            table.addBoardCard(getDeck().draw());
        }

        final StringBuilder sb = new StringBuilder();
        for (final Card card : table.getBoardCards()) {
            sb.append(String.format("[%s] ", card));
        }
        LOGGER.info(sb.toString());
    }

    @Override
    public void dealRiver(@NotNull final Table table) {

        LOGGER.info("Dealing river...");

        for (int i=0; i<CARDS_RIVER; i++) {
            table.addBoardCard(getDeck().draw());
        }

        final StringBuilder sb = new StringBuilder();
        for (final Card card : table.getBoardCards()) {
            sb.append(String.format("[%s] ", card));
        }
        LOGGER.info(sb.toString());
    }

    @Override
    public void cleanUp(@NotNull final Table table) {

        for (final Seat seat : table.getSeats()) {
            if (seat != null) {
                while (seat.getCards().size() > 0) {
                    try {
                        getDeck().add(seat.popCard());
                    } catch (Deck.DuplicateCardException e) {
                        // TODO: Unhandled exception
                        e.printStackTrace();
                    }
                }
            }
        }

        getDeck().addAll(table.clearBoardCards());
    }
}
