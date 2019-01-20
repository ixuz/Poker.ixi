package com.ictpoker.ixi.Table.TableException;

import com.sun.istack.internal.NotNull;

public class TableException extends Exception {

    public TableException(@NotNull final String message) {

        super(message);
    }

    public TableException(@NotNull final String message, Exception e) {

        super(message, e);
    }
}
