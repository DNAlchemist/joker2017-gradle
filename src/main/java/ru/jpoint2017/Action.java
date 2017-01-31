package ru.jpoint2017;

@SuppressWarnings("WeakerAccess")
@FunctionalInterface
public interface Action<T> {
    void execute(T t);
}
