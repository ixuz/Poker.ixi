package com.ictpoker.ixi.Player.Exception;

import com.sun.istack.internal.NotNull;

public class PlayerException extends Exception {

    public PlayerException(@NotNull final String message) {

        super(message);
    }

    public PlayerException(@NotNull final String message, Exception e) {

        super(message, e);
    }
}