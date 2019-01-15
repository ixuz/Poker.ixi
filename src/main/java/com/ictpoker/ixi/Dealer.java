package com.ictpoker.ixi;

import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.PriorityBlockingQueue;

public abstract class Dealer implements Runnable, IDealer {

    public final static float DEFAULT_DEALER_SPEED = 1f;
    private final static Logger LOGGER = LogManager.getLogger(Dealer.class);
    private final Table table;
    private final float speed;
    private final Deck deck = new Deck();
    private final PriorityBlockingQueue<PlayerEvent> playerEventQueue = new PriorityBlockingQueue<>();
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
                playerEventQueue.take();
                System.out.print("Took");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

/*            try {
                synchronized (playerEventQueue) {
                    if (!playerEventQueue.isEmpty()) {
                        playerEvent = playerEventQueue.take();
                        System.out.println("took");
                        handlePlayerEvent(playerEvent);
                        playerEvent.doCallback(null);
                    } else {
                        playerEventQueue.wait((long) (1.0f / Dealer.DEFAULT_DEALER_SPEED));
                    }
                }
            } catch (PlayerEventException e) {
                playerEvent.doCallback(e);
            } catch (InterruptedException e) {
                // TODO: Unhandled exception
                e.printStackTrace();
            }*/
        }
    }

    public void pushPlayerEvent(@NotNull final PlayerEvent playerEvent) {

        playerEventQueue.put(playerEvent);
    }

    // TODO: Make this method private
    public void handlePlayerEvent(@NotNull final PlayerEvent playerEvent)
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
}
