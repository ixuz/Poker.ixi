package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Player.PlayerEvent.PlayerEventException;
import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.TableState;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public abstract class TableEvent {

    private final Player player;
    private final int amount;

    public TableEvent(@Nullable final Player player,
                      @NotNull final int amount)
            throws PlayerEventException {

        this.player = player;
        this.amount = amount;

        if (player != null) {
            if (amount < 0 || amount > player.getBalance()) {
                throw new PlayerEventException("Invalid amount");
            }
        }
    }

    public Player getPlayer() {

        return player;
    }

    public int getAmount() {
        return amount;
    }

    public abstract void handle(@NotNull final TableState tableState) throws TableEventException;
}
