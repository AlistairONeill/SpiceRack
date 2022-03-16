
rootProject.name = "SpiceRack"

include(":underware")
include(":domain")
include(":http")
include(":ui")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}