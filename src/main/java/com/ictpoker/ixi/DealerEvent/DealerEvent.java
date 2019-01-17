package com.ictpoker.ixi.DealerEvent;

import com.ictpoker.ixi.ITableEvent;
import com.sun.istack.internal.NotNull;

public class DealerEvent implements ITableEvent {

    public enum DealerAction { MOVE_BUTTON, DEAL, SMALL_BLIND, BIG_BLIND }

    private final DealerAction dealerAction;

    public DealerEvent(@NotNull final DealerAction dealerAction) {

        this.dealerAction = dealerAction;
    }

    public DealerAction getDealerAction() {

        return dealerAction;
    }
}
