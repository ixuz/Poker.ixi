package com.ictpoker.ixi;

import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;

public abstract class Dealer extends Thread implements Runnable, IDealer {

    public final static float DEFAULT_DEALER_SPEED = 1f;
    private final static Logger LOGGER = LogManager.getLogger(Dealer.class);
    private final Table table;
    private final float speed;
    private final Deck deck = new Deck();
    private final LinkedBlockingQueue<PlayerEvent> playerEventQueue = new LinkedBlockingQueue<>();
    private boolean active = true;

    public Dealer(@NotNull final Table table, @NotNull final float speed) {

        this.table = table;
        this.speed = speed;
    }

    @Override
    public void run() {
        while (active) {
            PlayerEvent playerEvent = null;

            try {
                playerEvent = playerEventQueue.take();
                handlePlayerEvent(playerEvent);
                playerEvent.doCallback(null);
            } catch (InterruptedException e) {
                // TODO: Unhandled exception
                e.printStackTrace();
                break;
            } catch (PlayerEventException e) {
                playerEvent.doCallback(e);
            }
        }
    }

    public void pushPlayerEvent(@NotNull final PlayerEvent playerEvent) {

        try {
            playerEventQueue.put(playerEvent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handlePlayerEvent(@NotNull final PlayerEvent playerEvent)
            throws PlayerEventException {

        switch (playerEvent.getPlayerAction()) {
            case JOIN:
                handleJoin(playerEvent);
                break;
            case LEAVE:
                handleLeave(playerEvent);
                break;
        }
    }

    protected abstract void handleJoin(@NotNull final PlayerEvent playerEvent)
            throws PlayerEventException;

    protected abstract void handleLeave(@NotNull final PlayerEvent playerEvent)
            throws PlayerEventException;

    protected Table getTable() {
        return table;
    }

    protected Deck getDeck() {
        return deck;
    }

    protected void shuffleDeck() {

        deck.shuffle();
    }
}
