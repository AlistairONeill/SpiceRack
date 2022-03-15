import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    `java-test-fixtures`
}

group = "me.alistaironeill"
version = "1.0-SNAPSHOT"

val kondorVersion : String by project
val striktVersion : String by project
val http4kVersion : String by project

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
    implementation("org.http4k", "http4k-core", http4kVersion)
    implementation("org.http4k", "http4k-contract", http4kVersion)
    implementation("org.http4k", "http4k-server-undertow", http4kVersion)
    implementation("org.http4k", "http4k-client-apache", http4kVersion)

    implementation(project(":domain"))
    implementation(project(":underware"))
    implementation("com.ubertob.kondor", "kondor-outcome", kondorVersion)
    implementation("com.ubertob.kondor", "kondor-core", kondorVersion)

    testImplementation(kotlin("test"))
    testImplementation(testFixtures(project(":underware")))
    testImplementation(testFixtures(project(":domain")))
    testImplementation("org.http4k", "http4k-testing-strikt", http4kVersion)
    testImplementation("io.strikt", "strikt-core", striktVersion)

    testFixturesImplementation(testFixtures(project(":underware")))
    testFixturesImplementation(testFixtures(project(":domain")))
    testFixturesImplementation("org.http4k", "http4k-core", http4kVersion)
    testFixturesImplementation("org.http4k", "http4k-contract", http4kVersion)
    testFixturesImplementation("org.http4k", "http4k-server-undertow", http4kVersion)
    testFixturesImplementation("org.http4k", "http4k-client-apache", http4kVersion)
    testFixturesImplementation("io.strikt", "strikt-core", striktVersion)
    testFixturesImplementation("com.ubertob.kondor", "kondor-outcome", kondorVersion)
    testFixturesImplementation("com.ubertob.kondor", "kondor-core", kondorVersion)
}