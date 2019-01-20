package com.ictpoker.ixi.Player.PlayerEvent;

import com.ictpoker.ixi.Player.Player;
import com.sun.istack.internal.NotNull;

public class RaisePlayerEvent extends CommitPlayerEvent {

    public RaisePlayerEvent(@NotNull final Player player, @NotNull final int amount)
            throws PlayerEventException {

        super(player, PlayerAction.RAISE, amount);
    }
}
