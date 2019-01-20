package com.ictpoker.ixi.Player.PlayerEvent;

import com.ictpoker.ixi.Player.Player;
import com.sun.istack.internal.NotNull;

public class BetPlayerEvent extends CommitPlayerEvent {

    public BetPlayerEvent(@NotNull final Player player, @NotNull final int amount)
            throws PlayerEventException {

        super(player, PlayerAction.BET, amount);
    }
}
