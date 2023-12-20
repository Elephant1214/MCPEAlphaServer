import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlin_version: String by project

plugins {
    kotlin("jvm") version("1.9.21")
    id("io.gitlab.arturbosch.detekt") version("1.23.3")
}

group = "me.elephant1214"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

detekt {
    toolVersion = "1.23.3"
    config.setFrom(file("detekt.yml"))
    buildUponDefaultConfig = true
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions {
        freeCompilerArgs += listOf(
            "-opt-in=kotlin.ExperimentalStdlibApi"
        )
    }
}
