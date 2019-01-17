package com.ictpoker.ixi.DealerEvent;

import com.sun.istack.internal.NotNull;

public class DealerEventException extends Exception {

    public DealerEventException(@NotNull final String message) {

        super(message);
    }

    public DealerEventException(@NotNull final String message, Exception e) {

        super(message, e);
    }
}