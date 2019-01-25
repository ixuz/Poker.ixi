package com.ictpoker.ixi.Table.Exception;

import com.sun.istack.internal.NotNull;

public class TableEventException extends Exception {

    public TableEventException(@NotNull final String message) {

        super(message);
    }

    public TableEventException(@NotNull final String message, Exception e) {

        super(message, e);
    }
}
