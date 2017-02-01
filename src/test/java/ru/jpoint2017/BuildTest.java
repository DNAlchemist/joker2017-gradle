package ru.jpoint2017;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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

    @Test
    public void defaultRepository() {
        assertProject("default-repository.granny", project -> {
            assertEquals(1, project.getRepositories().size());
        });
    }

    @Test
    public void customRepository() {
        assertProject("custom-repository.granny", project -> {
            assertTrue(project.getRepositories().stream()
                    .map(MavenRepository::getUrl)
                    .anyMatch("http://custom-repository.com"::equals));
        });
    }

    @Test
    public void simpleDependency() {
        assertProject("simple-dependencies.granny", project -> {
            assertEquals(1, project.getDependencies().size());
            assertTrue(project.getDependencies().stream()
                    .map(Dependency::getUrl)
                    .anyMatch("org.ow2.asm:asm-all:5.0.3"::equals));
        });
    }

    @Test
    public void mapDependency() {
        assertProject("map-dependencies.granny", project -> {
            assertEquals(1, project.getDependencies().size());
            assertTrue(project.getDependencies().stream()
                    .map(Dependency::getUrl)
                    .anyMatch("junit:junit:4.12"::equals));
        });
    }

    @Test
    public void multipleDependency() {
        List<String> dependencies = Arrays.asList("org.ow2.asm:asm-all:5.0.3",
                "junit:junit:4.12",
                "junit:junit:4.11");

        assertProject("multiple-dependencies.granny", project -> {
            assertEquals(3, project.getDependencies().size());
            assertEquals(3, project.getDependencies().stream()
                    .map(Dependency::getUrl)
                    .filter(dependencies::contains)
                    .count());
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
