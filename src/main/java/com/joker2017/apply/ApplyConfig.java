package com.joker2017.apply;

import com.joker2017.GrannyInternal;
import com.joker2017.Project;
import com.joker2017.exception.InternalException;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class ApplyConfig {

    private final Project project;

    public ApplyConfig(Project project) {
        this.project = project;
    }

    public interface Plugin {
        void apply(Project project);
    }

    public void from(final String from) throws IOException {
        if(from != null) {
            GrannyInternal internal = new GrannyInternal(new File(project.getScript(), from), project);
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
