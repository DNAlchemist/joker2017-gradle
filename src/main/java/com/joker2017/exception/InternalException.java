package com.joker2017.exception;

public class InternalException extends RuntimeException {
    public InternalException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
