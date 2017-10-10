package com.joker2017.repository;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public class RepositoryHandler {

    public final Set<String> repositories = new HashSet<>();

    public void maven(@DelegatesTo(MavenRepository.class) Closure closure) {
        MavenRepository mavenRepository = new MavenRepository();
        closure.setDelegate(mavenRepository);
        closure.call();
        repositories.add(mavenRepository.getUrl());
    }

    public void mavenCentral() {
        repositories.add("http://repo1.maven.org/maven2/");
    }

    public void jcenter() {
        repositories.add("http://jcenter.bintray.com/");
    }
}
