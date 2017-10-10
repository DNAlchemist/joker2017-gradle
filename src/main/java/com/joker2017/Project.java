package com.joker2017;

import com.joker2017.apply.ApplyConfig;
import com.joker2017.dependency.DependencyHandler;
import com.joker2017.repository.RepositoryHandler;
import com.joker2017.task.Task;
import com.joker2017.task.TaskHandler;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.lang.GroovyObjectSupport;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public class Project extends GroovyObjectSupport {

    public final Set<String> compilers = new HashSet<>();

    final File script;
    private String sourceCompatibility;
    private RepositoryHandler repositoryHandler = new RepositoryHandler();
    private DependencyHandler dependencyHandler = new DependencyHandler();
    private TaskHandler tasks = new TaskHandler();

    public Project(File script) {
        this.script = script;
    }

    public void apply(@DelegatesTo(ApplyConfig.class) Closure closure) {
        closure.setDelegate(new ApplyConfig(this));
        closure.call();
    }

    public void apply(@DelegatesTo(ApplyConfig.class) Map<String, ?> options) throws IOException {
        ApplyConfig applyConfig = new ApplyConfig(this);
        applyConfig.from((String)options.get("from"));

        if(options.get("plugin") instanceof String) {
            applyConfig.plugin((String)options.get("plugin"));
        } else
            applyConfig.plugin((Class<ApplyConfig.Plugin>)options.get("plugin"));
    }

    public File getScript() {
        return script;
    }

    public String getSourceCompatibility() {
        return sourceCompatibility;
    }

    public void setSourceCompatibility(String sourceCompatibility) {
        this.sourceCompatibility = sourceCompatibility;
    }

    public Set<String> getRepositories() {
        return repositoryHandler.repositories;
    }

    public void repository(@DelegatesTo(RepositoryHandler.class) Closure closure) {
        closure.setDelegate(repositoryHandler);
        closure.call();
    }

    public void task(String name, @DelegatesTo(Task.class)Closure closure) {
        tasks.putTask(name, closure::call);
    }

    public TaskHandler getTasks() {
        return tasks;
    }

    public Set<String> getDependencies() {
        return dependencyHandler.depends;
    }

    public Set<String> getTestDependencies() {
        return dependencyHandler.testDeps;
    }


    public void dependencies(@DelegatesTo(DependencyHandler.class) Closure closure) {
        closure.setDelegate(dependencyHandler);
        closure.call();
    }
}
