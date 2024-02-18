plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.dokka)
    `maven-publish`
    signing
}

group = "io.github.gallvp"
version = "0.3"

object Meta {
    const val PROJECT_NAME = "sincmotion"
    const val PUBLISH_NAME = "SincMotion"
    const val DESC =
        "SincMotion: A Kotlin multi-platform implementation of gait and balance algorithms"
    const val LICENSE = "MIT"
    const val LICENSE_URL = "https://opensource.org/license/MIT/"
    const val GITHUB_REPO = "GallVp/sincmotion"
    const val DEVELOPER = "Usman Rashid"
    const val DEVELOPER_ID = "gallvp"
    const val RELEASE_URL = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
    const val SNAPSHOT_URL = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }

        publishAllLibraryVariants()
    }

    applyDefaultHierarchyTemplate()

    iosX64()
    iosArm64()

    cocoapods {
        summary = Meta.DESC
        homepage = "https://github.com/${Meta.GITHUB_REPO}"
        authors = Meta.DEVELOPER
        name = Meta.PUBLISH_NAME
        ios.deploymentTarget = "13.0"
        framework {
            baseName = Meta.PUBLISH_NAME
            isStatic = false
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.sincmaths)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        getByName("androidInstrumentedTest") {
            dependencies {
                implementation(libs.espresso.core)
            }

            dependsOn(sourceSets.commonTest.get())
        }
    }
}

android {
    namespace = "$group.${Meta.PROJECT_NAME}"
    compileSdk = 34
    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

interface Injected {
    @get:Inject
    val fs: FileSystemOperations

    @get:Inject
    val eo: ExecOperations
}

tasks.getByName("iosX64Test") {

    val injected = project.objects.newInstance<Injected>()
    val sourceDir = project.file("validation_data/ios_savs")
    val destinationDir = project.file("build/bin/iosX64/debugTest/validation_data/ios_savs")
    val scriptFile = File.createTempFile("symlinkScript", ".sh")

    scriptFile.writeText(
        """
        #!/usr/bin/env bash

        mkdir -p "$destinationDir"

        # Create symbolic links
        for file in "$sourceDir"/*; do
            dest_file="$destinationDir/${'$'}(basename ${'$'}file)"
            if [[ ! -e "${'$'}dest_file" ]]; then
                ln -s "${'$'}file" "$destinationDir"
            else
                echo "${'$'}file already linked..."
            fi
        done
        """.trimIndent(),
    )

    scriptFile.setExecutable(true)

    doFirst {
        injected.eo.exec {
            commandLine(scriptFile.absolutePath)
        }
    }
}

tasks.getByName("iosX64Test") {

    val injected = project.objects.newInstance<Injected>()

    doFirst {
        injected.fs.copy {
            from("src/iosTest/resources/example_data/gaitandbalance")
            into("build/bin/iosX64/debugTest/example_data/gaitandbalance")
            include("*.csv")
        }
    }
}

publishing {
    publications.withType<MavenPublication> {

        val publication = this
        val javadocJar =
            tasks.register("${publication.name}JavadocJar", Jar::class) {
                archiveClassifier.set("javadoc")
                archiveBaseName.set("${archiveBaseName.get()}-${publication.name}")

                from(tasks.named("dokkaHtml"))
            }
        artifact(javadocJar)

        pom {
            name.set(Meta.PUBLISH_NAME)
            description.set(Meta.DESC)
            url.set("https://github.com/${Meta.GITHUB_REPO}")
            licenses {
                license {
                    name.set(Meta.LICENSE)
                    url.set(Meta.LICENSE_URL)
                }
            }
            developers {
                developer {
                    id.set(Meta.DEVELOPER_ID)
                    name.set(Meta.DEVELOPER)
                }
            }
            scm {
                url.set(
                    "https://github.com/${Meta.GITHUB_REPO}.git",
                )
                connection.set(
                    "scm:git:git://github.com/${Meta.GITHUB_REPO}.git",
                )
                developerConnection.set(
                    "scm:git:git://github.com/${Meta.GITHUB_REPO}.git",
                )
            }
            issueManagement {
                url.set("https://github.com/${Meta.GITHUB_REPO}/issues")
            }
        }
    }

    repositories {
        maven {
            credentials {
                username = providers.gradleProperty("NEXUS_USERNAME").orNull
                password = providers.gradleProperty("NEXUS_PASSWORD").orNull
            }
            url =
                if ((version as String).endsWith("SNAPSHOT")) uri(Meta.SNAPSHOT_URL) else uri(Meta.RELEASE_URL)
        }
    }
}

publishing.publications.configureEach {
    signing.sign(this)
}
