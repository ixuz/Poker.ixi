package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Player.PlayerEvent.PlayerEventException;
import com.ictpoker.ixi.Table.TableState;
import com.sun.istack.internal.NotNull;

public class DealEvent extends TableEvent {

    public DealEvent()
            throws PlayerEventException {

        super(null, 0);
    }

    @Override
    public void handle(@NotNull final TableState tableState) {

    }
}
