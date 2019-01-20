package com.ictpoker.ixi.Dealer.DealerEvent;

import com.ictpoker.ixi.Table.TableEvent.ITableEvent;
import com.sun.istack.internal.NotNull;

public class DealerEvent implements ITableEvent {

    public enum DealerAction { MOVE_BUTTON, DEAL, SMALL_BLIND, BIG_BLIND, NEXT_PLAYER_TO_ACT }

    private final DealerAction dealerAction;

    public DealerEvent(@NotNull final DealerAction dealerAction) {

        this.dealerAction = dealerAction;
    }

    public DealerAction getDealerAction() {

        return dealerAction;
    }
}
