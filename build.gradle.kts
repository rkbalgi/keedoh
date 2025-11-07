plugins {
    java
    id("maven-publish")
    id("application")
    id("com.gradleup.shadow") version "9.2.2"
    id("com.diffplug.spotless") version "8.0.0"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

group = "com.daalitoy.apps"
version = "1.0-SNAPSHOT"
description = "keedoh"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

}

tasks.withType<Jar>() {
    manifest {
        attributes(Pair("Main-Class", "com.daalitoy.apps.keedoh.Keedoh"))
    }
}

dependencies {

    implementation(libs.org.codehaus.groovy.groovy.all)
    implementation(libs.com.google.guava.guava)
    implementation(libs.org.hsqldb.hsqldb)
    implementation(libs.org.apache.logging.log4j.log4j.core)
    implementation(libs.org.apache.logging.log4j.log4j.api)
    implementation(libs.com.google.inject.guice)
    implementation(libs.com.fasterxml.jackson.core.jackson.core)
    implementation(libs.com.fasterxml.jackson.core.jackson.annotations)
    implementation(libs.com.fasterxml.jackson.core.jackson.databind)
    implementation(libs.io.netty.netty.all)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

spotless {
    java {
        // apply a specific flavor of google-java-format
        googleJavaFormat("1.31.0").reflowLongStrings().skipJavadocFormatting()
        // fix formatting of type annotations
        formatAnnotations()
        removeUnusedImports()

    }
}
application {
    mainClass = "com.daalitoy.apps.keedoh.Keedoh"
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}
