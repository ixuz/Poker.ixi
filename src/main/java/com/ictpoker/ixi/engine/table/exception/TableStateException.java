package com.ictpoker.ixi.engine.table.exception;

public class TableStateException extends Exception {

    public TableStateException(final String message) {
        super(message);
    }

    public TableStateException(final String message, Exception e) {
        super(message, e);
    }
}
