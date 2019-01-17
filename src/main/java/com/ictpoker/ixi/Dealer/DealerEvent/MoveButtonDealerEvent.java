package com.ictpoker.ixi.Dealer.DealerEvent;

import com.sun.istack.internal.NotNull;

public class MoveButtonDealerEvent extends DealerEvent {

    private final int destinationSeatIndex;

    public MoveButtonDealerEvent(@NotNull final int destinationSeatIndex) {

        super(DealerAction.MOVE_BUTTON);

        this.destinationSeatIndex = destinationSeatIndex;
    }

    public int getDestinationSeatIndex() {

        return destinationSeatIndex;
    }
}
