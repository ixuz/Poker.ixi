package org.iota.ict.ixi;

import com.ictpoker.ixi.engine.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.RestApi;

public class PokerIxi extends IxiModule {

    private static final Logger LOGGER = LogManager.getLogger(PokerIxi.class);
    protected final RestApi restApi;

    public PokerIxi(final Ixi ixi) {
        super(ixi);
        this.restApi = new RestApi();
    }

    @Override
    public void terminate() {
        LOGGER.info("Terminating Poker.ixi...");
        super.terminate();
        restApi.onTerminate();
        LOGGER.info("Poker.ixi terminated.");
    }

    @Override
    public void run() {
        LOGGER.info(String.format("Poker.ixi %s: Starting...", Constants.VERSION));
        LOGGER.info(String.format("Poker.ixi %s: Started on port: %d", Constants.VERSION, 1234));

        restApi.start();
    }

    public Ixi getIxi() {
        return ixi;
    }
}
