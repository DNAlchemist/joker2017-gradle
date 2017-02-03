package ru.jpoint2017;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ruslanmikhalev on 02/02/17.
 */
@SuppressWarnings("WeakerAccess")
public class TaskHandler {

    private Map<String, Task> tasks = new HashMap<>();

    public void putTask(String name, Task task) {
        tasks.put(name, task);
    }

    public void withType(@DelegatesTo.Target Class<? extends Task> clazz, @DelegatesTo(genericTypeIndex = 0) Closure closure) {
        tasks.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(clazz::isInstance)
                .forEach(closure::call);
    }

    public Collection<Task> withType(Class<? extends Task> clazz) {
        return tasks.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(clazz::isInstance)
                .collect(Collectors.toList());
    }


    public int size() {
        return tasks.size();
    }
}
