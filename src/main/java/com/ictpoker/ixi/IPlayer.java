package com.ictpoker.ixi;

import com.sun.istack.internal.NotNull;

public interface IPlayer {

    String getName();
    int getBalance();
    void onJoinTable(@NotNull final Table table);
}
