package com.ictpoker.ixi.engine.table.exception;

public class TableEventException extends Exception {

    public TableEventException(final String message) {
        super(message);
    }

    public TableEventException(final String message, Exception e) {
        super(message, e);
    }
}
