val kotlin_version: String by project

plugins {
    kotlin("jvm") version "1.9.21"
}

group = "me.elephant1214"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
