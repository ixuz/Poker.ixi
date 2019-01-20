package com.ictpoker.ixi.Player.PlayerEvent;

import com.ictpoker.ixi.Player.Player;
import com.sun.istack.internal.NotNull;

public class CallPlayerEvent extends CommitPlayerEvent {

    public CallPlayerEvent(@NotNull final Player player)
            throws PlayerEventException {

        super(player, PlayerAction.CALL, 0);
    }
}
