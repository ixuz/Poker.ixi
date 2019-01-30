package com.ictpoker.ixi.Table.Exception;

public class TableEventException extends Exception {

    public TableEventException(final String message) {

        super(message);
    }

    public TableEventException(final String message, Exception e) {

        super(message, e);
    }
}
