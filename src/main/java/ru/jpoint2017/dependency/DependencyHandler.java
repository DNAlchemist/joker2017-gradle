/*
 * Copyright 2016 Aleksey Dobrynin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.jpoint2017.dependency;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DependencyHandler {
    public final Set<Dependency> dependencies = new HashSet<>();

    public void compile(String... dependencies) {
        this.dependencies.addAll(
                Arrays.stream(dependencies)
                        .map(Dependency::new)
                        .collect(Collectors.toSet())
        );
    }

    public void compile(Map<String, String> dependency) {
        this.dependencies.add(new Dependency(dependency));
    }

}
