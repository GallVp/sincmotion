# SincMotionMP Library

The library is based on older SincMotion libraries in kotlin/jvm, swift and the reference implementation on MATLAB. At the start of this project, these libraries were at the following versions:

+ kotlin/jvm: [v0.2+81d8444](https://github.com/GallVp/libsinc-android/commit/81d84447a46d203ce60b51d7d6e311a371cfebe4)
+ swift: [v0.2.3](https://github.com/GallVp/SincMotion/tree/v0.2.3)
+ MATLAB: [65ca654](https://github.com/GallVp/innerEarMatlab/commit/65ca654f33a305918c55f07270e3278461503fb5)

## Change Log

### Version 0.1 <06052021>

+ Added normative models.
+ Added study validation.
+ Added random sampling to study validation.
+ Added tests for normative scoring.
+ Updated kotlin to 1.5.0.
+ Added cocoapods integration.
+ Added processorVersion = 1.
+ Added maps and keys to parameters.
+ Added numParameters in companion object for parameters.
+ Added Map constructors for parameter classes.
+ Added names and units to parameters.
+ Added `defaultParameterKey` to parameter classes.
+ Added `makePresentable` to `NormativeDatabase`.
+ Made `database` property of `NormativeDatabase` lazy.
