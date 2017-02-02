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

import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class Dependency {
    private String group;
    private String name;
    private String version;

    public Dependency(String dependency) {
        String[] params = dependency.split(":");
        group = params[0];
        name = params[1];
        version = params[2];
    }

    public Dependency(Map<String, String> params) {
        group = params.get("group");
        name = params.get("name");
        version = params.get("version");
    }

    public String getUrl() {
        return group + ":" + name + ":" + version;
    }
}
