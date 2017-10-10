package com.joker2017;

import groovy.lang.GroovyObjectSupport;
import groovy.lang.Script;

public abstract class ProjectScript extends Script {

    @Override
    public Object invokeMethod(String name, Object args) {
        return ((GroovyObjectSupport) getProperty("project")).invokeMethod(name, args);
    }

    @Override
    public void setProperty(String name, Object value) {
        ((GroovyObjectSupport) getProperty("project")).setProperty(name, value);
    }

    @Override
    public Object getProperty(String name) {
        return super.getProperty(name);
    }
}
