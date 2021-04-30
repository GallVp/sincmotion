package sincmotion.internals

import sincmaths.SincMatrix
import sincmaths.asRowVector
import sincmaths.sincmatrix.*
import sincmotion.GaitParameters
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

internal fun cgOutcomes(
    timeVector: SincMatrix,
    accelData: SincMatrix,
    rotData: SincMatrix,
    gyroData: SincMatrix,
    fs: Double,
    personHeight: Double,
    isAndroid: Boolean
): GaitParameters {

    val segments = cgCreateSegments(timeVector, accelData, rotData, gyroData, fs, isAndroid)
    val accelDataSegments = segments.accelDataSegments
    val rotDataSegments = segments.rotDataSegments
    val gyroDataSegments = segments.gyroDataSegments

    val gSymIndex = Double.NaN * SincMatrix.ones(4, 1)

    val stepLengths = emptySincMatrices(4)
    val leftStepLengths = emptySincMatrices(4)
    val rightStepLengths = emptySincMatrices(4)
    val stepTimes = emptySincMatrices(4)
    val leftStepTimes = emptySincMatrices(4)
    val rightStepTimes = emptySincMatrices(4)


    accelDataSegments.indices.map { i ->
        cgsOutcomes(
            accelDataSegments[i],
            rotDataSegments[i],
            gyroDataSegments[i],
            fs,
            personHeight,
            isAndroid
        )
    }.forEachIndexed { i, cgsOut ->
        gSymIndex[i] = cgsOut.gSymIndex
        stepLengths[i] = cgsOut.stepLengths
        leftStepLengths[i] = cgsOut.leftStepLengths
        rightStepLengths[i] = cgsOut.rightStepLengths
        stepTimes[i] = cgsOut.stepTimes
        leftStepTimes[i] = cgsOut.leftStepTimes
        rightStepTimes[i] = cgsOut.rightStepTimes
    }

    val meanSymIndex = gSymIndex.median().scalar * 100.0

    val meanStepLength = stepLengths.asRowVector().median().scalar
    val meanStepLengthLeft = leftStepLengths.asRowVector().median().scalar
    val meanStepLengthRight = rightStepLengths.asRowVector().median().scalar
    val stdStepLengthLeft = leftStepLengths.asRowVector().iqr().scalar / 1.35
    val stdStepLengthRight = rightStepLengths.asRowVector().iqr().scalar / 1.35

    val meanStepTime = stepTimes.asRowVector().median().scalar
    val meanStepTimeLeft = leftStepTimes.asRowVector().median().scalar
    val meanStepTimeRight = rightStepTimes.asRowVector().median().scalar
    val stdStepTimeLeft = leftStepTimes.asRowVector().iqr().scalar / 1.35
    val stdStepTimeRight = rightStepTimes.asRowVector().iqr().scalar / 1.35

    val stepLengthVariability =
        (sqrt((stdStepLengthLeft.pow(2.0) + stdStepLengthRight.pow(2.0)) / 2.0)) / meanStepLength * 100.0
    val stepTimeVariability =
        (sqrt((stdStepTimeLeft.pow(2.0) + stdStepTimeRight.pow(2.0)) / 2.0)) / meanStepTime * 100.0
    val stepLengthASymmetry = (meanStepLengthLeft - meanStepLengthRight).absoluteValue / meanStepLength * 100.0
    val stepTimeASymmetry = (meanStepTimeLeft - meanStepTimeRight).absoluteValue / meanStepTime * 100.0
    val meanStepVelocity = rightStepTimes.asRowVector().elDiv(stepTimes.asRowVector()).median().scalar

    return GaitParameters(
        meanSymIndex,
        meanStepLength,
        meanStepTime,
        stepLengthVariability,
        stepTimeVariability,
        stepLengthASymmetry,
        stepTimeASymmetry,
        meanStepVelocity
    )
}