package ru.jpoint2017;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;

import java.util.Map;

/**
 * Created by ruslanmikhalev on 30/01/17.
 */
interface PluginAware {
    void apply(@DelegatesTo(ApplyConfig.class) Closure closure);
    void apply(Action<? super ApplyConfig> action);
    void apply(Map<String, ?> options);
}
