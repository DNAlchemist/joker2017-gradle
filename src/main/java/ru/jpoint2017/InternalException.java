package ru.jpoint2017;

@SuppressWarnings("WeakerAccess")
public class InternalException extends RuntimeException {

    public InternalException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
