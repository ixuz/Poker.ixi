package com.ictpoker.ixi.Player.PlayerEvent;

import com.ictpoker.ixi.Player.Player;
import com.sun.istack.internal.NotNull;

public class JoinPlayerEvent extends PlayerEvent {

    private final int seatIndex;

    public JoinPlayerEvent(@NotNull final Player player,
                           @NotNull final int buyIn,
                           @NotNull final int seatIndex)
            throws PlayerEventException {

        super(player, PlayerAction.JOIN, buyIn);

        this.seatIndex = seatIndex;
    }

    public int getSeatIndex() {

        return seatIndex;
    }
}
