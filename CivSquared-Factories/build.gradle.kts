plugins {
    id("org.civsquared.conventions")
}

dependencies {
    compileOnly(project(":CivSquared-Core"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")

    testImplementation("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
}

tasks {
    test {
        useJUnitPlatform()
    }
}