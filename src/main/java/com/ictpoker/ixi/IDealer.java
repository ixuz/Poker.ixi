package com.ictpoker.ixi;

import com.ictpoker.ixi.DealerEvent.DealerEventException;
import com.sun.istack.internal.NotNull;

public interface IDealer {

    void moveButton(@NotNull final Table table, @NotNull final int seatIndex) throws DealerEventException;
    void postSmallBlind(@NotNull final Table table) throws DealerEventException;
    void postBigBlind(@NotNull final Table table) throws DealerEventException;
    void dealHoleCards(@NotNull final Table table);
    void dealFlop(@NotNull final Table table);
    void dealTurn(@NotNull final Table table);
    void dealRiver(@NotNull final Table table);
    void cleanUp(@NotNull final Table table);
}
