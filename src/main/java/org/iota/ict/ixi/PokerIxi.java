package org.iota.ict.ixi;

import com.ictpoker.ixi.PokerIxiGossipListener;
import com.ictpoker.ixi.Commons.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PokerIxi extends IxiModule {

    private final static Logger LOGGER = LogManager.getLogger(PokerIxi.class);

    public PokerIxi(final Ixi ixi) {
        super(ixi);
    }

    @Override
    public void terminate() {
        LOGGER.info("Terminating Poker.ixi...");
        super.terminate();
        LOGGER.info("Poker.ixi terminated.");
    }

    @Override
    public void run() {
        LOGGER.info(String.format("Poker.ixi %s: Starting...", Constants.VERSION));
        ixi.addGossipListener(new PokerIxiGossipListener());
        LOGGER.info(String.format("Poker.ixi %s: Started on port: %d", Constants.VERSION, 1234));
    }

    public Ixi getIxi() {
        return ixi;
    }
}
