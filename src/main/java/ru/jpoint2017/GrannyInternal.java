package ru.jpoint2017;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.runtime.ResourceGroovyMethods;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class GrannyInternal {

    private final CompilerConfiguration compilerConfiguration;
    private Binding binding;
    String scriptText;

    public GrannyInternal(File buildScript) {
        this(buildScript, new Project(buildScript.getParentFile()));
    }

    public GrannyInternal(File buildScript, Project project) {
        try {
            scriptText = ResourceGroovyMethods.getText(buildScript);
        } catch (IOException e) {
            throw new InternalException("Build script not found!", e);
        }
        compilerConfiguration = new CompilerConfiguration();
        compilerConfiguration.setScriptBaseClass(ProjectScript.class.getName());
        binding = new Binding();
        binding.setProperty("project", project);
    }

    public Project build() {
        GroovyShell shell = new GroovyShell(binding, compilerConfiguration);
        shell.evaluate(scriptText);
        return (Project) shell.getProperty("project");
    }

}
