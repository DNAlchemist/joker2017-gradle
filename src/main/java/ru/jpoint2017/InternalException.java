package ru.jpoint2017;

/**
 * Created by ruslanmikhalev on 31/01/17.
 */
@SuppressWarnings("WeakerAccess")
public class InternalException extends RuntimeException {

    public InternalException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
