import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `java-test-fixtures`
}

group = "me.alistaironeill"
version = "1.0-SNAPSHOT"

val kondorVersion : String by project
val striktVersion : String by project
val kMongoVersion : String by project

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
    implementation(project("::underware"))
    implementation(project("::domain"))
    implementation("com.ubertob.kondor", "kondor-outcome", kondorVersion)
    implementation("org.litote.kmongo", "kmongo", kMongoVersion)

    testImplementation(kotlin("test"))
    testImplementation("io.strikt", "strikt-core", striktVersion)
    testImplementation(testFixtures(project("::underware")))
    testImplementation(testFixtures(project("::domain")))

    testFixturesImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")

    testFixturesImplementation(kotlin("test"))
    testFixturesImplementation(project("::underware"))
    testFixturesImplementation(testFixtures(project("::underware")))
    testFixturesImplementation("io.strikt", "strikt-core", striktVersion)
    testFixturesImplementation("com.ubertob.kondor", "kondor-outcome", kondorVersion)
    testFixturesImplementation("org.litote.kmongo", "kmongo", kMongoVersion)
}