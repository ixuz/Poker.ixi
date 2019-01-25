package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Player.PlayerEvent.PlayerEventException;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;
import com.sun.istack.internal.NotNull;

public abstract class CommitEvent extends TableEvent {

    public CommitEvent(@NotNull final Player player,
                       @NotNull final int amount)
            throws PlayerEventException {

        super(player, amount);
    }
}
