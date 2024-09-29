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
    //implementation("jakarta.persistence:jakarta.persistence-api:3.2.4")
    compileOnly("org.projectlombok:lombok:1.18.22")
    implementation ("org.springframework:spring-jdbc:5.3.6")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
    //implementation("org.hibernate:hibernate-core:6.4.4.Final")
    //implementation("org.hibernate:hibernate-entitymanager:5.6.15.Final")
    implementation("org.postgresql:postgresql:42.7.0")
    implementation("com.h2database:h2:2.2.222")
    api("org.springframework.boot:spring-boot-starter:3.2.4")
    api("org.springframework.boot:spring-boot-starter-data-jpa:3.2.4")
    api("org.springframework.boot:spring-boot-starter-validation:3.2.4")
    api("org.springframework.boot:spring-boot-starter-security:3.2.4")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}