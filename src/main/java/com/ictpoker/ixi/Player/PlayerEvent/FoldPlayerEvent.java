package com.ictpoker.ixi.Player.PlayerEvent;

import com.ictpoker.ixi.Player.Player;
import com.sun.istack.internal.NotNull;

public class FoldPlayerEvent extends PlayerEvent {

    public FoldPlayerEvent(@NotNull final Player player)
            throws PlayerEventException {

        super(player, PlayerAction.FOLD, 0);
    }
}
