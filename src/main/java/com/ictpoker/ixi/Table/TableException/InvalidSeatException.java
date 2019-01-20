package com.ictpoker.ixi.Table.TableException;

import com.sun.istack.internal.NotNull;

public class InvalidSeatException extends Exception {

    public InvalidSeatException(@NotNull final String message) {

        super(message);
    }

    public InvalidSeatException(@NotNull final String message, Exception e) {

        super(message, e);
    }
}
