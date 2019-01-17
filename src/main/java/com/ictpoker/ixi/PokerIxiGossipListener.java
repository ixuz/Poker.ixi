package com.ictpoker.ixi;

import com.ictpoker.ixi.Commons.Constants;
import org.iota.ict.model.Transaction;
import org.iota.ict.network.event.GossipEvent;
import org.iota.ict.network.event.GossipFilter;
import org.iota.ict.network.event.GossipListener;

public class PokerIxiGossipListener extends GossipListener {

    private final GossipFilter filter = new GossipFilter();

    public PokerIxiGossipListener() {
        filter.watchTag(Constants.TAG);
    }

    @Override
    public void onGossipEvent(final GossipEvent event) {
        if (!filter.passes(event.getTransaction())) return;

        if (event.isOwnTransaction()) {
            handleOutbound(event.getTransaction());
        } else {
            handleInbound(event.getTransaction());
        }
    }

    private void handleInbound(final Transaction transaction) {

    }

    private void handleOutbound(final Transaction transaction) {

    }
}
