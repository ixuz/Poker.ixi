package com.ictpoker.ixi.Player.PlayerEvent;

import com.ictpoker.ixi.Player.Player;
import com.sun.istack.internal.NotNull;

public class SmallBlindPlayerEvent extends PlayerEvent {

    public SmallBlindPlayerEvent(@NotNull final Player player)
            throws PlayerEventException {

        super(player, PlayerAction.SMALL_BLIND, 0);
    }
}
