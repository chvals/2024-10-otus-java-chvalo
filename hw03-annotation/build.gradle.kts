import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id ("com.github.johnrengelman.shadow")
}

dependencies {
    implementation ("ch.qos.logback:logback-classic")
    // https://mavenlibs.com/maven/dependency/name.finsterwalder/fileutils
    implementation("name.finsterwalder:fileutils:1.1")

    testImplementation ("org.junit.jupiter:junit-jupiter-api")
    testImplementation ("org.junit.jupiter:junit-jupiter-engine")
    testImplementation ("org.assertj:assertj-core")
    testImplementation ("org.mockito:mockito-core")
    testImplementation ("org.mockito:mockito-junit-jupiter")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("annotationsHW02")
        archiveVersion.set("1.0")
        archiveClassifier.set("")
        manifest {
            attributes(mapOf("Main-Class" to "Main"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}
