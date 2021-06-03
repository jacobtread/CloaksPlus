import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    id("edu.sc.seis.launch4j") version "2.4.3"
}
group = "me.jacobtread.cloaksplus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(files("libs/jfxrt.jar"))
    implementation("com.jfoenix:jfoenix:8.0.10")
}

launch4j {
    headerType="gui"
    outfile = "CloaksPlusInstaller.exe"
    mainClassName = "me.jacobtread.cloaksplus.App"
    icon = "${project.projectDir.absolutePath}/src/main/resources/icon.ico"
    manifest = "${project.projectDir.absolutePath}/app.manifest"
    dontWrapJar=false
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Jar> {
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    manifest {
        attributes["Main-Class"] = "me.jacobtread.cloaksplus.Main"
    }
}