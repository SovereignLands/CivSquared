import io.papermc.paperweight.tasks.RemapJar

plugins {
    id("xyz.jpenilla.run-paper") version "2.2.2"
}

fun projectJar(projectName: String): File {
    return project(projectName).tasks.named<RemapJar>("reobfJar").flatMap { it.outputJar }.get().asFile
}

runPaper {
    disablePluginJarDetection()
}

tasks {
    runServer {
        dependsOn(":CivSquared-Core:reobfJar")
        dependsOn(":CivSquared-Nations:reobfJar")
        dependsOn(":CivSquared-Factories:reobfJar")

        minecraftVersion("1.20.4")

        pluginJars(projectJar(":CivSquared-Core"))
        pluginJars(projectJar(":CivSquared-Nations"))
        pluginJars(projectJar(":CivSquared-Factories"))
    }
}