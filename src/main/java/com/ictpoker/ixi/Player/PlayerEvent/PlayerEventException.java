package com.ictpoker.ixi.Player.PlayerEvent;

import com.sun.istack.internal.NotNull;

public class PlayerEventException extends Exception {

    public PlayerEventException(@NotNull final String message) {

        super(message);
    }

    public PlayerEventException(@NotNull final String message, Exception e) {

        super(message, e);
    }
}