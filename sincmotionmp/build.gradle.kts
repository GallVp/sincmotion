plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.androidLibrary)
    `maven-publish`
    signing
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    applyDefaultHierarchyTemplate()

    iosX64()
    iosArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        framework {
            baseName = "sincmotion"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // put your multiplatform dependencies here
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
    namespace = "io.github.gallvp.sincmotion"
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
        sourceDir="$sourceDir"
        destinationDir="$destinationDir"

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
