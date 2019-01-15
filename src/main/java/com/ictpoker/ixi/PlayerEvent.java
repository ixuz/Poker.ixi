package com.ictpoker.ixi;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public class PlayerEvent {

    public enum PlayerAction { JOIN, LEAVE }

    private final Player player;
    private final PlayerAction playerAction;
    private final IPlayerEventCallback callback;

    public PlayerEvent(@NotNull final Player player, @NotNull final PlayerAction playerAction) {

        this.player = player;
        this.playerAction = playerAction;
        this.callback = null;
    }

    public PlayerEvent(@NotNull final Player player,
                       @NotNull final PlayerAction playerAction,
                       @Nullable final IPlayerEventCallback callback) {

        this.player = player;
        this.playerAction = playerAction;
        this.callback = callback;
    }

    public Player getPlayer() {

        return player;
    }

    public PlayerAction getPlayerAction() {

        return playerAction;
    }

    public void doCallback(@Nullable final PlayerEventException e) {

        if (callback != null) {
            callback.onCallback(e);
        }
    }

    public interface IPlayerEventCallback {

        void onCallback(@Nullable final PlayerEventException e);
    }
}
