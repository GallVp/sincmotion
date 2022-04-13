# SincMotionMP Library

## Reference Implementation

MATLAB Reference Implementation version: [e1142a5](https://github.com/GallVp/innerEarMatlab/commit/e1142a56ab229c11319e13a1c9e462e8bb3ad136), Test Tol. = 1e-10

## Validation Data

The validation data is placed inside *./build/bin/iosX64/debugTest* directory. Each study has its own folder. Each study folder contains all the app raw data files and an *Outcomes* folder which contains the *AppValidationData* and *AppValidationGaitData* folders.

If the app data is located in participant-wise folders, it can be extracted to a single parent folder with the following bash command.

```shell
find . -mindepth 2 -type f -print -exec mv {} . \;
```

## Change Log

### Version 0.2.2 <13042022>

+ Updated Kotlin to 1.6.20.
+ Updated SincMaths to 0.2.2.

### Version 0.2.1 <25032022>

+ Fixed the `processorVersion` issue.

### Version 0.2 <25032022>

+ Updated to kotlin to 1.6.10.
+ Updated SincMaths to 0.2.1.
+ Updated espresso-core to 3.4.0.
+ Updated Android SDK to 31.
+ Updated Android NDK to 23.0.7599858.
+ Added the improved version of `gsi` algo from MATLAB.
+ Added NAVSII validation data.
+ Updated normative models to stage I and III data.

### Version 0.1.4 <11092021>

+ Added functions for summary report.

### Version 0.1.3 <10092021>

+ Changed normative range for s-vel, s-len, s-time.

### Version 0.1.2 <09092021>

+ Changed order and names of the gait parameters.

### Version 0.1.1 <08092021>

+ Changed normative range for slv, stv, sla and sta.

### Version 0.1 <21052021>

+ Added normative models.
+ Added study validation.
+ Added random sampling to study validation.
+ Added tests for normative scoring.
+ Added Cocoapods integration.
+ Added processorVersion = 1.
+ Added maps and keys to parameters.
+ Added the improved version of `gsi` algo from MATLAB.

## History

This library is based on older SincMotion libraries in kotlin/jvm, swift and the reference implementation on MATLAB. At the start of this project, these libraries were at the following versions:

+ kotlin/jvm: [v0.2+81d8444](https://github.com/GallVp/libsinc-android/commit/81d84447a46d203ce60b51d7d6e311a371cfebe4)
+ swift: [v0.2.3](https://github.com/GallVp/SincMotion/tree/v0.2.3)
+ MATLAB: [65ca654](https://github.com/GallVp/innerEarMatlab/commit/65ca654f33a305918c55f07270e3278461503fb5)
