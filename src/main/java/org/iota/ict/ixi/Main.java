package org.iota.ict.ixi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.RestApi;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("Nothing to see here yet...");
        RestApi restApi = new RestApi();
        restApi.start();
    }
}
