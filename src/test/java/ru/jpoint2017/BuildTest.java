package ru.jpoint2017;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by ruslanmikhalev on 30/01/17.
 */
@Tag("fast")
public class BuildTest {

    @Test
    public void evalEmptyProject() {
        assertProject("empty.granny", project -> {
            assertEquals(0, project.compilers.size());
        });
    }

    @Test
    public void applyMap() {
        assertProject("apply-plugin.granny", project -> { });
    }

    @Test
    public void applyMapJava() {
        assertProject("apply-plugin.granny", project -> {
            assertTrue(project.compilers.contains("java"));
        });
    }

    @Test
    public void applyFromEmpty() {
        assertProject("apply-from-empty.granny", project -> {
            assertEquals(0, project.compilers.size());
        });
    }

    @Test
    public void applyFrom() {
        assertProject("apply-from.granny", project -> {
            assertTrue(project.compilers.contains("java"));
        });
    }

    public static class KotlinPlugin implements ApplyConfig.Plugin {
        @Override
        public void apply(Project project) {
            project.compilers.add("kotlin");
        }
    }

    @Test
    public void applyCustomPlugin() {
        assertProject("apply-custom-plugin.granny", project -> {
            assertTrue(project.compilers.contains("kotlin"));
        });
    }

    @Test
    public void setSourceCompatibility() {
        assertProject("set-source-compatibility.granny", project -> {
            assertEquals("1.9", project.getSourceCompatibility());
        });
    }

    private void assertProject(String resourceName, Consumer<Project> o) {
        GrannyInternal internal = new GrannyInternal(getBuildFileFromResources(resourceName));
        Project project = internal.build();
        o.accept(project);
    }

    private File getBuildFileFromResources(String resourceName) {
        try {
            URL resource = getClass().getClassLoader().getResource(resourceName);
            if(resource == null) {
                fail("Resource " + resourceName + " has not been found");
                return null;
            }
            return Paths.get(resource.toURI()).toFile();
        } catch (URISyntaxException e) {
            throw new AssertionError("Never happens");
        }
    }
}
