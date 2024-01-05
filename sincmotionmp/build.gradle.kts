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
}

tasks.getByName("iosX64Test") {

    val injected = project.objects.newInstance<Injected>()

    doFirst {
        injected.fs.copy {
            from("src/iosTest/resources")
            into("build/bin/iosX64/debugTest")
        }
    }
}
