rootProject.name = "otusJava"
include("hw01-gradle")
include("hw02-generics")
include("hw03-annotation")
include("hw04-gc")
include("hw05-bc")
include("hw06-solid")
include("hw07-pattern")
include("hw08-json")
include("hw09-jdbc:demo")
include("hw09-jdbc:homework")
include("hw10-jpql")
include("hw11-cache")
include("hw12-webserver")


pluginManagement {
    val jgitver: String by settings
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings
    val jib: String by settings
    val protobufVer: String by settings
    val sonarlint: String by settings
    val spotless: String by settings

    plugins {
        id("fr.brouillard.oss.gradle.jgitver") version jgitver
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("com.google.cloud.tools.jib") version jib
        id("com.google.protobuf") version protobufVer
        id("name.remal.sonarlint") version sonarlint
        id("com.diffplug.spotless") version spotless
    }
}

include("hw13-ioc-container")
include("hw14-spring-jdbc")
include("hw16-queues")
include("hw15-executors")
include("hw17-gRPC")
include ("hw18-react:client-service")
include ("hw18-react:datastore-service")
