package com.joker2017;

import com.joker2017.apply.ApplyConfig;
import com.joker2017.task.Task;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BuildTest {

    @Test
    public void evalEmptyProject() throws IOException {
        assertProject("empty.granny", project -> {
            assertEquals(0, project.compilers.size());
        });
    }

    @Test
    public void applyMap() throws IOException {
        assertProject("apply-plugin.granny", project -> {
        });
    }

    @Test
    public void applyMapJava() throws IOException {
        assertProject("apply-plugin.granny", project -> {
            assertTrue(project.compilers.contains("java"));
        });
    }

    @Test
    public void applyFromEmpty() throws IOException {
        assertProject("apply-from-empty.granny", project -> {
            assertEquals(0, project.compilers.size());
        });
    }

    @Test
    public void applyFrom() throws IOException {
        assertProject("apply-from.granny", project -> {
            assertTrue(project.compilers.contains("java"));
        });
    }

    @Test
    public void applyCustomPlugin() throws IOException {
        assertProject("apply-custom-plugin.granny", project -> {
            assertTrue(project.compilers.contains("kotlin"));
        });
    }

    @Test
    public void setSourceCompatibility() throws IOException {
        assertProject("set-source-compatibility.granny", project -> {
            assertEquals("1.9", project.getSourceCompatibility());
        });
    }

    @Test
    public void defaultRepository() throws IOException {
        assertProject("default-repository.granny", project -> {
            assertEquals(1, project.getRepositories().size());
        });
    }

    @Test
    public void taskDefinitionAst() throws IOException {
        assertProject("create-task-ast.granny", project -> {
            assertEquals(1, project.getTasks().size());
        });
    }

    @Test
    public void customRepository() throws IOException {
        assertProject("custom-repository.granny", project -> {
            assertTrue(project.getRepositories().stream()
                    .anyMatch("http://custom-repository.com"::equals));
        });
    }

    @Test
    public void simpleDependency() throws IOException {
        assertProject("simple-dependencies.granny", project -> {
            assertEquals(1, project.getDependencies().size());
            assertTrue(project.getDependencies().stream()
                    .anyMatch("org.ow2.asm:asm-all:5.0.3"::equals));
        });
    }

    @Test
    public void mapDependency() throws IOException {
        assertProject("map-dependencies.granny", project -> {
            assertEquals(1, project.getTestDependencies().size());
            assertTrue(project.getTestDependencies().stream()
                    .anyMatch("junit:junit:4.12"::equals));
        });
    }

    @Test
    public void multipleDependency() throws IOException {
        List<String> dependencies = Arrays.asList("org.ow2.asm:asm-all:5.0.3",
                "junit:junit:4.12");

        assertProject("multiple-dependencies.granny", project -> {
            assertEquals(2, project.getDependencies().size());
            assertEquals(2, project.getDependencies().stream()
                    .filter(dependencies::contains)
                    .count());
        });
    }

    @Test
    public void taskHello() throws IOException {
        assertProject("task-hello.granny", project -> {
        });
    }

    private void assertProject(String resourceName, Consumer<Project> consumer) throws IOException {
        File resource = getBuildFileFromResources(resourceName);
        assertProject(resource, new Project(resource.getParentFile()), consumer);
    }

    private void assertProject(File resource, Project project, Consumer<Project> consumer) throws IOException {
        new GrannyInternal(resource, project)
                .build();
        consumer.accept(project);
    }

    private File getBuildFileFromResources(String resourceName) {
        try {
            URL resource = getClass().getClassLoader().getResource(resourceName);
            if (resource == null) {
                throw new AssertionError("Resource " + resourceName + " has not been found");
            }
            return Paths.get(resource.toURI()).toFile();
        } catch (URISyntaxException e) {
            throw new AssertionError("Never happens");
        }
    }

    public interface TestTask extends Task {
        int maxParallelForks = 1;
    }

    public static class KotlinPlugin implements ApplyConfig.Plugin {
        @Override
        public void apply(Project project) {
            project.compilers.add("kotlin");
        }
    }
}
