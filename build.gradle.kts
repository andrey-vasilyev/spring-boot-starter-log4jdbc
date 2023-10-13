import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    `java-library`
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps.management)
    pmd

    `maven-publish`
    signing
    alias(libs.plugins.axion.release)
    alias(libs.plugins.nexus.publish)
}

group = "ru.vasiand"
version = scmVersion.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.log4jdbc)
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("org.springframework.boot:spring-boot-autoconfigure")

    testImplementation("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
}

tasks.compileJava {
    options.compilerArgs.addAll(
        listOf(
            "-Xlint:all,-processing", // Enables all recommended warnings.
            "-Werror" // Terminates compilation when warnings occur.
        )
    )
    options.encoding = "UTF-8"
}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version
            )
        )
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events = setOf(TestLogEvent.FAILED)
        exceptionFormat = TestExceptionFormat.FULL
    }
}

pmd {
    toolVersion = "6.23.0"
    ruleSetFiles = files("pmd.xml")
}

scmVersion {
    localOnly.set(true)
    with(tag) {
        prefix.set("")
        versionSeparator.set("")
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}

tasks.withType<Jar> {
    from(rootProject.projectDir) {
        include("LICENSE.txt")
        into("META-INF")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set(project.name)
                description.set("The Log4jdbc Spring Boot 3 Starter facilitates the quick and easy use of http://log4jdbc.brunorozendo.com/ in Spring Boot projects.")
                url.set("https://github.com/andrey-vasilyev/spring-boot-starter-log4jdbc")
                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("andrey-vasilyev")
                        name.set("Andrey Vasilyev")
                        email.set("vasilyev.andrew@gmail.com")
                    }
                }
                scm {
                    url.set("https://github.com/andrey-vasilyev/spring-boot-starter-log4jdbc")
                }
            }
        }
    }
}

val gpgKey: String? = System.getenv("GPG_KEY")
val gpgPass: String? = System.getenv("GPG_PASS")
signing {
    isRequired = gpgKey != null
    useInMemoryPgpKeys(gpgKey, gpgPass)
    sign(publishing.publications["mavenJava"])
}
