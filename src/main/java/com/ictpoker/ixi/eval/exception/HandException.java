package com.ictpoker.ixi.eval.exception;

public class HandException extends Exception {

    public HandException(final String message) {
        super(message);
    }

    public HandException(final String message, Exception e) {
        super(message, e);
    }
}
