package ru.jpoint2017;

import ru.jpoint2017.ApplyConfig.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruslanmikhalev on 31/01/17.
 */
public class Plugins {

    public static Map<String, Plugin> plugins = new HashMap<>();
    static {
        plugins.put("java", project -> project.compilers.add("java"));
        plugins.put("groovy", project -> project.compilers.add("groovy"));
    }

    public static Plugin getByName(String name) {
        return plugins.get(name);
    }
}
