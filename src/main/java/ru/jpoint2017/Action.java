package ru.jpoint2017;

/**
 * Created by ruslanmikhalev on 30/01/17.
 */
@SuppressWarnings("WeakerAccess")
@FunctionalInterface
public interface Action<T> {
    void execute(T t);
}
