package com.ictpoker.ixi.Player.PlayerEvent;

import com.ictpoker.ixi.Player.Player;
import com.sun.istack.internal.NotNull;

public class LeavePlayerEvent extends PlayerEvent {

    public LeavePlayerEvent(@NotNull final Player player)
            throws PlayerEventException {

        super(player, PlayerAction.LEAVE, 0);
    }
}
