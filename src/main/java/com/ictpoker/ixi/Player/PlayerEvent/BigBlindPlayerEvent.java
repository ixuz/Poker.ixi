package com.ictpoker.ixi.Player.PlayerEvent;

import com.ictpoker.ixi.Player.Player;
import com.sun.istack.internal.NotNull;

public class BigBlindPlayerEvent extends PlayerEvent {

    public BigBlindPlayerEvent(@NotNull final Player player)
            throws PlayerEventException {

        super(player, PlayerAction.BIG_BLIND, 0);
    }
}
