package com.ictpoker.ixi.engine.table.exception;

public class SeatException extends Exception {

    public SeatException(final String message) {
        super(message);
    }

    public SeatException(final String message, Exception e) {
        super(message, e);
    }
}
