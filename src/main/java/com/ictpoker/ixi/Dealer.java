package com.ictpoker.ixi;

import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Queue;

public abstract class Dealer implements IDealer {

    public final static float DEFAULT_DEALER_SPEED = 1f;
    private final static Logger LOGGER = LogManager.getLogger(Dealer.class);
    private final float speed;
    private final Deck deck;

    private final Queue<PlayerEvent> playerEventQueue = new LinkedList<>();

    public Dealer(@NotNull final float speed)
            throws DealerException {

        this.speed = speed;

        try {
            deck = new Deck();
        } catch (Deck.DuplicateCardException e) {
            throw new DealerException("Failed to initialize Dealer", e);
        }
    }

    public boolean update(@NotNull final Table.State state) {

        // Check if all seats have acted at least once in this round.
        boolean allSeatsActed = true;
        for (final Seat seat : state.getSeats()) {
            if (seat != null && !seat.hasActed()) {
                allSeatsActed = false;
            }
        }

        if (allSeatsActed) {
            LOGGER.info("All seats have acted at least once this round");
        } else {
            LOGGER.info("Not all seats have acted at least once this round");
        }

        return false;
    }

    public void handleEventQueue(@NotNull final Table table)
            throws PlayerEventException {

        while (!playerEventQueue.isEmpty()) {
            final PlayerEvent playerEvent = playerEventQueue.remove();
            handlePlayerEvent(table, playerEvent);
            update(table.getState());
        }
    }

    public void pushPlayerEvent(@NotNull final Table table, @NotNull final PlayerEvent playerEvent) {

        playerEventQueue.add(playerEvent);
    }

    private void handlePlayerEvent(@NotNull final Table table, @NotNull final PlayerEvent playerEvent)
            throws PlayerEventException {

        switch (playerEvent.getPlayerAction()) {
            case JOIN:
                handleJoin(table, playerEvent);
                break;
            case LEAVE:
                handleLeave(table, playerEvent);
                break;
            case CHECK:
                break;
            case CALL:
                break;
            case BET:
                break;
            case RAISE:
                break;
            case FOLD:
                break;
        }
    }

    protected abstract void handleJoin(@NotNull final Table table, @NotNull final PlayerEvent playerEvent)
            throws PlayerEventException;

    protected abstract void handleLeave(@NotNull final Table table, @NotNull final PlayerEvent playerEvent)
            throws PlayerEventException;

    protected Deck getDeck() {
        return deck;
    }

    protected void shuffleDeck() {

        deck.shuffle();
    }

    protected void isRoundFinished() {

    }

    protected void moveDealerButton() {


    }

    public class DealerException extends Exception {

        public DealerException(@NotNull final String message, Exception e) {

            super(message, e);
        }
    }
}
