package com.ictpoker.ixi.Table.Exception;

public class TableException extends Exception {

    public TableException(final String message) {

        super(message);
    }

    public TableException(final String message, Exception e) {

        super(message, e);
    }
}
