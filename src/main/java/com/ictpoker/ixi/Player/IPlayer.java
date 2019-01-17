package com.ictpoker.ixi.Player;

import com.ictpoker.ixi.Table.Table;
import com.sun.istack.internal.NotNull;

public interface IPlayer {

    String getName();
    int getBalance();
    void onJoinTable(@NotNull final Table table);
}
