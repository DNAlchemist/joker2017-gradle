package ru.jpoint2017;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public class RepositoryHandler {

    Set<MavenRepository> repositories = new HashSet<>();

    public void maven(@DelegatesTo(MavenRepository.class) Closure closure) {
        MavenRepository mavenRepository = new MavenRepository();
        closure.setDelegate(mavenRepository);
        closure.call();
        repositories.add(mavenRepository);
    }

    public void mavenCentral() {
        MavenRepository mavenCentral = new MavenRepository();
        mavenCentral.setUrl("http://repo1.maven.org/maven2/");
        repositories.add(mavenCentral);
    }

}
