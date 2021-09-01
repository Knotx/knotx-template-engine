/*
 * Copyright (C) 2019 Knot.x Project
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
rootProject.name = "knotx-template-engine"

pluginManagement {
    val version: String by settings
    plugins {
        id("io.knotx.java-library") version version
        id("io.knotx.codegen") version version
        id("io.knotx.unit-test") version version
        id("io.knotx.jacoco") version version
        id("io.knotx.maven-publish") version version
        id("io.knotx.composite-build-support") version version
        id("io.knotx.release-java") version version
        id("org.nosphere.apache.rat") version "0.7.0"
    }
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}


include("knotx-template-engine-api")
include("knotx-template-engine-core")
include("knotx-template-engine-handlebars")
include("knotx-template-engine-it-test")
include("knotx-template-engine-pebble")

project(":knotx-template-engine-api").projectDir = file("api")
project(":knotx-template-engine-core").projectDir = file("core")
project(":knotx-template-engine-handlebars").projectDir = file("handlebars")
project(":knotx-template-engine-pebble").projectDir = file("pebble")
project(":knotx-template-engine-it-test").projectDir = file("it-test")
