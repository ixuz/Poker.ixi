package com.ictpoker.ixi.table.exception;

public class TableEventException extends Exception {

    public TableEventException(final String message) {

        super(message);
    }

    public TableEventException(final String message, Exception e) {

        super(message, e);
    }
}
