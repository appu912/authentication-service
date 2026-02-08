rootProject.name = "authorization-service"

pluginManagement {
    val springBootVersion: String by settings
    val spotlessVersion: String by settings

    plugins {
        id ("org.springframework.boot") version springBootVersion
        id ("com.diffplug.spotless") version spotlessVersion
    }
}