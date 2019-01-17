package com.ictpoker.ixi;

import com.sun.istack.internal.NotNull;

public class DealerEvent implements ITableEvent {

    public enum DealerAction { DEAL }

    private final Dealer dealer;
    private final DealerAction dealerAction;

    public DealerEvent(@NotNull final Dealer dealer, @NotNull final DealerAction dealerAction) {

        this.dealer = dealer;
        this.dealerAction = dealerAction;
    }

    public Dealer getDealer() {

        return dealer;
    }

    public DealerAction getDealerAction() {

        return dealerAction;
    }
}
