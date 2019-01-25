package com.ictpoker.ixi.Table.Exception;

import com.sun.istack.internal.NotNull;

public class TableStateException extends Exception {

    public TableStateException(@NotNull final String message) {

        super(message);
    }

    public TableStateException(@NotNull final String message, Exception e) {

        super(message, e);
    }
}
