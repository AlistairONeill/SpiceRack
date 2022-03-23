plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version "1.1.0"
}

group = "me.alistaironeill"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

val kondorVersion : String by project
val http4kVersion : String by project

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.compose.material", "material-icons-core-desktop")
    implementation("org.http4k", "http4k-client-apache", http4kVersion)
    implementation("org.http4k", "http4k-core", http4kVersion)
    implementation(project(":domain"))
    implementation(project(":underware"))
    implementation(project(":http:"))
    implementation("com.ubertob.kondor", "kondor-outcome", kondorVersion)
}

compose.desktop {
    application {
        mainClass = "uk.co.alistaironeill.spicerack.MainKt"
    }
}