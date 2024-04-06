# SincMotion

[![linting](https://github.com/GallVp/sincmotion/actions/workflows/linting.yml/badge.svg)](https://github.com/GallVp/sincmotion/actions/workflows/linting.yml)
[![build and test](https://github.com/GallVp/sincmotion/actions/workflows/build-test.yml/badge.svg)](https://github.com/GallVp/sincmotion/actions/workflows/build-test.yml)
[![cite](https://img.shields.io/badge/DOI-10.3390/s22010124-0f5fa5.svg)](https://doi.org/10.3390/s22010124)

SincMotion is a Kotlin Multiplatform implementation of algorithms for gait and balance assessment using an inertial measurement unit. A [MATLAB implementation](https://github.com/GallVp/sincmotion-matlab) of this library is also available. See [documentation](https://gallvp.github.io/sincmotion/) for implementation details. This library is built on [sincmaths](https://github.com/GallVp/sincmaths) matrix library.

## Usage

### Kotlin Project: [MavenCentral](https://central.sonatype.com/artifact/io.github.gallvp/sincmotionmp)

> **Note:** This library does not work with `iosSimulatorArm64` target. Android tests have to be
> performed on an emulator or a connected device, otherwise, the underlying JAVA JNI libraries
> fail to load.

Add dependency:

```kotlin
implementation("io.github.gallvp:sincmotionmp:0.3")
```

### Swift Project: [CocoaPods](https://kotlinlang.org/docs/native-cocoapods.html#update-podfile-for-xcode)

Clone this project and add it to your project's Podfile:

```pod
pod 'SincMotion', :path => '/path/to/cloned/sincmotion/sincmotionmp'
```

Import it in your project:

```swift
import SincMotion
```
