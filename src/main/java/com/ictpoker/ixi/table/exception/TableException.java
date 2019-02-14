package com.ictpoker.ixi.table.exception;

public class TableException extends Exception {

    public TableException(final String message) {
        super(message);
    }

    public TableException(final String message, Exception e) {
        super(message, e);
    }
}
