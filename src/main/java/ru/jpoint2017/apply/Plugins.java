package ru.jpoint2017.apply;

import ru.jpoint2017.apply.ApplyConfig.Plugin;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class Plugins {

    private static Map<String, Plugin> plugins = new HashMap<>();
    static {
        plugins.put("java", project -> project.compilers.add("java"));
        plugins.put("groovy", project -> project.compilers.add("groovy"));
    }

    public static Plugin getByName(String name) {
        return plugins.get(name);
    }
}
