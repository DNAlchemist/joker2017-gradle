package com.joker2017;

import com.joker2017.task.TaskDefinitionCustomizer;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class GrannyInternal {

    private final CompilerConfiguration config;
    private Binding binding;
    private final File script;

    public GrannyInternal(File script) {
        this(script, new Project(script.getParentFile()));
    }

    public GrannyInternal(File script, Project project) {
        this.script = script;
        TaskDefinitionCustomizer customizer = new TaskDefinitionCustomizer();

        config = new CompilerConfiguration();
        config.setScriptBaseClass(ProjectScript.class.getName());
        config.addCompilationCustomizers(customizer);
        config.setDefaultScriptExtension(".granny");

        binding = new Binding();
        binding.setProperty("project", project);
    }

    public Project build() throws IOException {
        GroovyShell shell = new GroovyShell(binding, config);
        shell.evaluate(script);
        return (Project) shell.getProperty("project");
    }
}
