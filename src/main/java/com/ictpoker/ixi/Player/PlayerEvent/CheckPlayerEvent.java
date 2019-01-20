package com.ictpoker.ixi.Player.PlayerEvent;

import com.ictpoker.ixi.Player.Player;
import com.sun.istack.internal.NotNull;

public class CheckPlayerEvent extends CommitPlayerEvent {

    public CheckPlayerEvent(@NotNull final Player player)
            throws PlayerEventException {
        
        super(player, PlayerAction.CHECK, 0);
    }
}
