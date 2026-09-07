import org.apache.tools.ant.filters.ReplaceTokens

plugins {
	id("com.gradleup.shadow") version "9.3.0"
	java
}

group = "com.github.hapily04.skriptnbs"
version = "1.0.0"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(25))
	}
}

repositories {
	mavenCentral()
	maven("https://maven.hapily.me/snapshots")
	maven("https://maven.hapily.me/releases")
}

dependencies {
	compileOnly("com.github.hapily04:skript-minestom:1.0.0-alpha.41")
	implementation("com.xxmicloxx:NoteBlockAPI:2026.09.06-26.2")
}

tasks.withType<JavaCompile>().configureEach {
	options.encoding = "UTF-8"
}

tasks.named<ProcessResources>("processResources") {
	filter<ReplaceTokens>("tokens" to mapOf("version" to version))
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
	archiveClassifier.set("")
}

tasks.named("build") {
	dependsOn("shadowJar")
}
