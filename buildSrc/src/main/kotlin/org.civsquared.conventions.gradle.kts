plugins {
    id("java")
    id("com.github.johnrengelman.shadow")
    id("io.papermc.paperweight.userdev")
}

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://repo.papermc.io/repository/maven-public/")

    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")

    maven("https://libraries.minecraft.net")
}

dependencies {
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")

    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    compileOnly("com.mojang:brigadier:1.0.18")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation("org.reflections:reflections:0.10.2")
    implementation("org.mariadb.jdbc:mariadb-java-client:2.1.2")
}

tasks {
    reobfJar {
        shouldRunAfter(shadowJar)
    }
}