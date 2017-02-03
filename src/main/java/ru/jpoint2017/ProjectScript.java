package ru.jpoint2017;

import groovy.lang.GroovyObjectSupport;
import groovy.lang.MissingMethodException;
import groovy.lang.Script;

public abstract class ProjectScript extends Script {

    @Override
    public Object invokeMethod(String name, Object args) {
        try {
            return super.invokeMethod(name, args);
        } catch (MissingMethodException e) {
            return ((GroovyObjectSupport) getProperty("project")).invokeMethod(name, args);
        }
    }

    @Override
    public void setProperty(String name, Object value) {
        try {
            super.invokeMethod(name, value);
        } catch (MissingMethodException e) {
            ((GroovyObjectSupport) getProperty("project")).setProperty(name, value);
        }
    }

    @Override
    public Object getProperty(String name) {
        try {
            return super.getProperty(name);
        } catch (MissingMethodException e) {
            return ((GroovyObjectSupport) getProperty("project")).getProperty(name);
        }
    }
}
