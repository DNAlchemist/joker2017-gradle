package ru.jpoint2017;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.lang.GroovyObjectSupport;
import ru.jpoint2017.apply.ApplyConfig;
import ru.jpoint2017.dependency.Dependency;
import ru.jpoint2017.dependency.DependencyHandler;
import ru.jpoint2017.repository.MavenRepository;
import ru.jpoint2017.repository.RepositoryHandler;
import ru.jpoint2017.task.Task;
import ru.jpoint2017.task.TaskHandler;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public class Project extends GroovyObjectSupport {

    public final Set<String> compilers = new HashSet<>();

    final File projectDir;
    private String sourceCompatibility;
    private RepositoryHandler repositoryHandler = new RepositoryHandler();
    private TaskHandler tasks = new TaskHandler();
    private DependencyHandler dependencyHandler = new DependencyHandler();

    public Project(File projectDir) {
        this.projectDir = projectDir;
    }

    public void apply(@DelegatesTo(ApplyConfig.class) Closure closure) {
        closure.setDelegate(new ApplyConfig(this));
        closure.call();
    }

    public void apply(@DelegatesTo(ApplyConfig.class) Map<String, ?> options) {
        ApplyConfig applyConfig = new ApplyConfig(this);
        applyConfig.from((String)options.get("from"));

        if(options.get("plugin") instanceof String) {
            applyConfig.plugin((String)options.get("plugin"));
        } else
            applyConfig.plugin((Class<ApplyConfig.Plugin>)options.get("plugin"));
    }

    public File getProjectDir() {
        return projectDir;
    }

    public String getSourceCompatibility() {
        return sourceCompatibility;
    }

    public void setSourceCompatibility(String sourceCompatibility) {
        this.sourceCompatibility = sourceCompatibility;
    }

    public Set<MavenRepository> getRepositories() {
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

    public Set<Dependency> getDependencies() {
        return dependencyHandler.depends;
    }

    public void dependencies(@DelegatesTo(DependencyHandler.class) Closure closure) {
        closure.setDelegate(dependencyHandler);
        closure.call();
    }

}
