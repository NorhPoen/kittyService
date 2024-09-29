plugins {
    id("java-library")
    id("org.springframework.boot") version "3.2.4" apply false
}
apply(plugin = "io.spring.dependency-management")

group = "org.ISKor"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(project(mapOf("path" to ":Cats-Owners:Data")))
    //implementation("jakarta.persistence:jakarta.persistence-api:3.2.4")
    implementation ("org.springframework:spring-jdbc:5.3.6")
    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:4.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.4")
    //implementation("org.hibernate:hibernate-core:6.4.4.Final")
    //implementation("org.hibernate:hibernate-entitymanager:5.6.15.Final")
}

tasks.test {
    useJUnitPlatform()
}