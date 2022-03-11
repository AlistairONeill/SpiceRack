import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    `java-test-fixtures`
}

group = "me.alistaironeill"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fazecast", "jSerialComm", "2.9.1")
    implementation("com.ubertob.kondor", "kondor-outcome", "1.7.6")

    testImplementation(kotlin("test"))
    testImplementation("io.strikt", "strikt-core", "0.34.1")

    testFixturesImplementation("io.strikt", "strikt-core", "0.34.1")
    testFixturesImplementation("com.ubertob.kondor", "kondor-outcome", "1.7.6")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}