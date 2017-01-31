package ru.jpoint2017;

import java.io.File;

@SuppressWarnings("WeakerAccess")
public class ApplyConfig {

    private final Project project;

    public ApplyConfig(Project project) {
        this.project = project;
    }

    public interface Plugin {
        void apply(Project project);
    }

    public void from(final String from) {
        if(from != null) {
            GrannyInternal internal = new GrannyInternal(new File(project.getProjectDir(), from), project);
            internal.build();
        }
    }

    public void plugin(final Class<? extends Plugin> pluginClass) {
        if(pluginClass != null) {
            try {
                pluginClass.newInstance().apply(project);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new InternalException("Class access error!", e);
            }
        }
    }

    public void plugin(String name) {
        if(name != null)
            Plugins.getByName(name).apply(project);
    }

}
