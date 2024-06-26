import de.thetaphi.forbiddenapis.gradle.CheckForbiddenApis

plugins {
	java
    idea
    checkstyle
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
	id("io.freefair.lombok") version "8.6"
	id("de.thetaphi.forbiddenapis") version "3.7"
}

group = "com.colin-moerbe"

// Exposed additional information about the application to the /info actuator endpoint.
springBoot {
    buildInfo()
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom ("org.springframework.boot:spring-boot-dependencies:3.3.1") // This actually sets every version I currently need
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-docker-compose")
    implementation("org.springframework.boot:spring-boot-testcontainers")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.slf4j:slf4j-api")
    implementation("org.postgresql:postgresql")
    implementation("org.apache.httpcomponents.client5:httpclient5") // Necessary so that PATCH requests work

    developmentOnly("org.springframework.boot:spring-boot-devtools") // Ctrl+F9 for recompiling, this fast restarts the server

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter:")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:")
    testImplementation("org.testcontainers:postgresql")
}

// Loads the versioning logic, and the custom Gradle tasks
apply(from = "versioning.gradle.kts")

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<DefaultTask>("checkstyleMain").configure {
    isEnabled = true
}

tasks.named<DefaultTask>("checkstyleTest").configure {
    isEnabled = true
}

tasks.named<CheckForbiddenApis>("forbiddenApisMain").configure {
	bundledSignatures = setOf("jdk-unsafe", "jdk-deprecated", "jdk-internal", "jdk-non-portable", "jdk-system-out", "jdk-reflection")
	signaturesFiles = project.files("forbidden-apis.txt")
    isEnabled = true
}

tasks.named<CheckForbiddenApis>("forbiddenApisTest").configure {
	bundledSignatures = setOf("jdk-unsafe", "jdk-deprecated", "jdk-internal", "jdk-non-portable", "jdk-reflection")
	signaturesFiles = project.files("forbidden-apis.txt")
	isEnabled = true
}

tasks.named("check").configure {
	dependsOn(tasks.named("forbiddenApisMain"))
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

checkstyle {
	configFile = project.file("checkstyle.xml")
	toolVersion = "10.17.0"
}
