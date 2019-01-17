package com.ictpoker.ixi;

import com.sun.istack.internal.NotNull;

public interface IDealer {

    void dealHoleCards(@NotNull final Table table);
    void dealFlop(@NotNull final Table table);
    void dealTurn(@NotNull final Table table);
    void dealRiver(@NotNull final Table table);
    void cleanUp(@NotNull final Table table);
}
