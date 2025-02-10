dependencies {
    implementation ("ch.qos.logback:logback-classic")
    implementation("io.freefair.gradle:lombok-plugin:8.4")
    implementation("org.projectlombok:lombok:1.18.22")

    testImplementation ("org.junit.jupiter:junit-jupiter-api")
    testImplementation ("org.junit.jupiter:junit-jupiter-engine")
    testImplementation ("org.junit.jupiter:junit-jupiter-params")
    testImplementation ("org.assertj:assertj-core")
    testImplementation ("org.mockito:mockito-core")
    testImplementation ("org.mockito:mockito-junit-jupiter")
}
