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

dependencies {
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "uk.co.alistaironeill.spicerack.MainKt"
    }
}