package com.ictpoker.ixi.Player.PlayerEvent;

import com.ictpoker.ixi.Player.Player;
import com.sun.istack.internal.NotNull;

public class CommitPlayerEvent extends PlayerEvent {

    public CommitPlayerEvent(@NotNull final Player player,
                             @NotNull final PlayerAction playerAction,
                             @NotNull final int amount)
            throws PlayerEventException {

        super(player, playerAction, amount);
    }
}
