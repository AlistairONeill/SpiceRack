import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    `java-test-fixtures`
}

group = "me.alistaironeill"
version = "1.0-SNAPSHOT"

val jSerialCommVersion : String by project
val kondorVersion : String by project
val striktVersion : String by project

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {

    implementation("com.fazecast", "jSerialComm", jSerialCommVersion)
    implementation("com.ubertob.kondor", "kondor-outcome", kondorVersion)

    testImplementation(kotlin("test"))
    testImplementation("io.strikt", "strikt-core", striktVersion)

    testFixturesImplementation("io.strikt", "strikt-core", striktVersion)
    testFixturesImplementation("com.ubertob.kondor", "kondor-outcome", kondorVersion)
}