package io.github.gallvp.sincmotion.gaitandbalance

import io.github.gallvp.sincmaths.SincMatrix
import io.github.gallvp.sincmaths.asRowVector
import io.github.gallvp.sincmaths.cat
import io.github.gallvp.sincmaths.diff
import io.github.gallvp.sincmaths.elDiv
import io.github.gallvp.sincmaths.emptySincMatrices
import io.github.gallvp.sincmaths.find
import io.github.gallvp.sincmaths.flip
import io.github.gallvp.sincmaths.get
import io.github.gallvp.sincmaths.getCol
import io.github.gallvp.sincmaths.getRows
import io.github.gallvp.sincmaths.gt
import io.github.gallvp.sincmaths.iqr
import io.github.gallvp.sincmaths.median
import io.github.gallvp.sincmaths.numRows
import io.github.gallvp.sincmaths.numel
import io.github.gallvp.sincmaths.ones
import io.github.gallvp.sincmaths.scalar
import io.github.gallvp.sincmaths.set
import io.github.gallvp.sincmaths.times
import io.github.gallvp.sincmotion.core.bandPassAt100From0d3To45
import io.github.gallvp.sincmotion.core.estimateGaitOutcomes
import io.github.gallvp.sincmotion.core.estimatePosturalStability
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

@Throws(Exception::class)
fun estimateGnBStaticOutcomes(
    accelData: SincMatrix,
    rotData: SincMatrix,
    fs: Double,
): GnBStaticOutcomes {
    val accelMLxAPxVert = preprocessGnBStaticStream(accelData, rotData, fs)

    val (stabilityR, stabilityML, stabilityAP) = estimatePosturalStability(accelMLxAPxVert)

    return GnBStaticOutcomes(stabilityR, stabilityML, stabilityAP)
}

expect fun truncateGnBStaticStream(
    accelData: SincMatrix,
    rotData: SincMatrix,
    fs: Double,
): TruncatedGnBStaticStream

data class TruncatedGnBStaticStream(
    val truncatedAccelData: SincMatrix,
    val truncatedRotData: SincMatrix,
)

fun preprocessGnBStaticStream(
    accelData: SincMatrix,
    rotData: SincMatrix,
    fs: Double,
): SincMatrix {
    // Convert acceleration units to m/sec/sec
    val accelDataSI = accelData * 9.8

    // If Android, discard first 3 seconds of data to avoid transients
    val (truncatedAccelData, truncatedRotData) = truncateGnBStaticStream(accelDataSI, rotData, fs)

    // Correct for orientation <X-ML, Y-AP, Z-Verticle>
    val accelMLxAPxVert = applyGnBFrameCorrection(truncatedAccelData, truncatedRotData, fs)

    // Filter Data
    val fsInt = fs.toInt()
    val fsInt10 = 10 * fsInt
    val paddedData =
        accelMLxAPxVert.getRows(1..fsInt10).flip().cat(
            1,
            accelMLxAPxVert,
            accelMLxAPxVert.getRows(accelMLxAPxVert.numRows - fsInt10 + 1..accelMLxAPxVert.numRows)
                .flip(),
        )

    val filteredData = bandPassAt100From0d3To45(paddedData)
    val filteredAccelMLxAPxVert =
        filteredData.get { endR, _, _, allC ->
            Pair(fsInt10 + 1..endR - fsInt10, allC)
        }

    return filteredAccelMLxAPxVert
}

@Throws(Exception::class)
fun estimateGnBGaitOutcomes(
    timeVector: SincMatrix,
    accelData: SincMatrix,
    rotData: SincMatrix,
    gyroData: SincMatrix,
    fs: Double,
    personHeight: Double,
): GnBGaitOutcomes {
    val (accelSegments, rotSegments, gyroSegments) =
        segmentGnBGaitStream(
            timeVector,
            accelData,
            rotData,
            gyroData,
        )

    val symIndex = Double.NaN * SincMatrix.ones(4, 1)

    val stepLengths = emptySincMatrices(4)
    val leftStepLengths = emptySincMatrices(4)
    val rightStepLengths = emptySincMatrices(4)
    val stepTimes = emptySincMatrices(4)
    val leftStepTimes = emptySincMatrices(4)
    val rightStepTimes = emptySincMatrices(4)

    accelSegments.indices.map { i ->

        // iOS Reference: https://developer.apple.com/documentation/coremotion/getting_processed_device-motion_data/understanding_reference_frames_and_device_attitude
        // Android Reference: https://developer.android.com/guide/topics/sensors/sensors_overview
        val (accelMLxAPxVert, processedGyroData) =
            preprocessGnBGaitSegment(
                accelSegments[i],
                rotSegments[i],
                gyroSegments[i],
                fs,
            )

        val gyroAboutAP = processedGyroData.getCol(3)
        // For iOS and Android, Z is the intrinsic AP axis. Counter clock-wise rotations are positive. Thus, positive gyro angles correspond to right swing phase. At the right heel strike the phone is at its counter-clockwise peak.

        estimateGaitOutcomes(
            accelMLxAPxVert,
            gyroAboutAP,
            fs,
            personHeight,
        )
    }.forEachIndexed { i, outcomes ->
        symIndex[i + 1] = outcomes.symIndex
        stepLengths[i] = outcomes.stepLengths
        leftStepLengths[i] = outcomes.leftStepLengths
        rightStepLengths[i] = outcomes.rightStepLengths
        stepTimes[i] = outcomes.stepTimes
        leftStepTimes[i] = outcomes.leftStepTimes
        rightStepTimes[i] = outcomes.rightStepTimes
    }

    val meanSymIndex = symIndex.median().scalar * 100.0

    fun Array<SincMatrix>.estimateMean(): Double = this.asRowVector().median().scalar

    fun Array<SincMatrix>.estimateStd(): Double = this.asRowVector().iqr().scalar / 1.35

    val meanStepLength = stepLengths.estimateMean()
    val meanStepLengthLeft = leftStepLengths.estimateMean()
    val meanStepLengthRight = rightStepLengths.estimateMean()
    val stdStepLengthLeft = leftStepLengths.estimateStd()
    val stdStepLengthRight = rightStepLengths.estimateStd()

    val meanStepTime = stepTimes.estimateMean()
    val meanStepTimeLeft = leftStepTimes.estimateMean()
    val meanStepTimeRight = rightStepTimes.estimateMean()
    val stdStepTimeLeft = leftStepTimes.estimateStd()
    val stdStepTimeRight = rightStepTimes.estimateStd()

    fun computeVarPer(
        leftValue: Double,
        rightValue: Double,
        meanValue: Double,
    ): Double {
        val sqSum = leftValue.pow(2) + rightValue.pow(2)
        val rmsValue = sqrt(sqSum / 2)
        return rmsValue / meanValue * 100
    }

    fun computeAsymmetryPer(
        leftValue: Double,
        rightValue: Double,
        meanValue: Double,
    ) = abs(
        leftValue - rightValue,
    ) / meanValue * 100.0

    val stepLengthVariability = computeVarPer(stdStepLengthLeft, stdStepLengthRight, meanStepLength)
    val stepTimeVariability = computeVarPer(stdStepTimeLeft, stdStepTimeRight, meanStepTime)
    val stepLengthASymmetry =
        computeAsymmetryPer(meanStepLengthLeft, meanStepLengthRight, meanStepLength)
    val stepTimeASymmetry = computeAsymmetryPer(meanStepTimeLeft, meanStepTimeRight, meanStepTime)
    val meanStepVelocity = stepLengths.asRowVector().elDiv(stepTimes.asRowVector()).median().scalar

    return GnBGaitOutcomes(
        meanSymIndex,
        meanStepLength,
        meanStepTime,
        stepLengthVariability,
        stepTimeVariability,
        stepLengthASymmetry,
        stepTimeASymmetry,
        meanStepVelocity,
    )
}

expect fun truncateGnBGaitSegment(
    accelMLxAPxVert: SincMatrix,
    gyroData: SincMatrix,
    fs: Double,
): GnBPreprocessedGaitSegment

fun preprocessGnBGaitSegment(
    accelData: SincMatrix,
    rotData: SincMatrix,
    gyroData: SincMatrix,
    fs: Double,
): GnBPreprocessedGaitSegment {
    // Convert acceleration units to m/sec/sec
    val accelDataSI = accelData * 9.8

    // Correct for orientation <X-ML, Y-AP, Z-Vertical>
    val accelMLxAPxVert = applyGnBFrameCorrection(accelDataSI, rotData, fs)

    // If Android: Remove initial 2 seconds
    return truncateGnBGaitSegment(accelMLxAPxVert, gyroData, fs)
}

expect fun applyGnBFrameCorrection(
    accelData: SincMatrix,
    rotData: SincMatrix,
    fs: Double,
): SincMatrix

data class GnBPreprocessedGaitSegment(val accelMLxAPxVert: SincMatrix, val gyroData: SincMatrix)

fun segmentGnBGaitStream(
    timeVector: SincMatrix,
    accelData: SincMatrix,
    rotData: SincMatrix,
    gyroData: SincMatrix,
): GnBGaitSegments {
    val dataPauseStarts = timeVector.diff().gt(1.0).find()
    val accelDataSegments =
        listOf(
            accelData.getRows(1..dataPauseStarts[1].toInt()),
            accelData.getRows(dataPauseStarts[1].toInt() + 1..dataPauseStarts[2].toInt()),
            // +1 to go to first sample of the lap
            accelData.getRows(dataPauseStarts[2].toInt() + 1..dataPauseStarts[3].toInt()),
            accelData.getRows(dataPauseStarts[3].toInt() + 1..timeVector.numel),
        )
    val rotDataSegments =
        listOf(
            rotData.getRows(1..dataPauseStarts[1].toInt()),
            rotData.getRows(dataPauseStarts[1].toInt() + 1..dataPauseStarts[2].toInt()),
            rotData.getRows(dataPauseStarts[2].toInt() + 1..dataPauseStarts[3].toInt()),
            rotData.getRows(dataPauseStarts[3].toInt() + 1..timeVector.numel),
        )
    val gyroDataSegments =
        listOf(
            gyroData.getRows(1..dataPauseStarts[1].toInt()),
            gyroData.getRows(dataPauseStarts[1].toInt() + 1..dataPauseStarts[2].toInt()),
            gyroData.getRows(dataPauseStarts[2].toInt() + 1..dataPauseStarts[3].toInt()),
            gyroData.getRows(dataPauseStarts[3].toInt() + 1..timeVector.numel),
        )

    return GnBGaitSegments(accelDataSegments, rotDataSegments, gyroDataSegments)
}

data class GnBGaitSegments(
    val accelSegments: List<SincMatrix>,
    val rotSegments: List<SincMatrix>,
    val gyroSegments: List<SincMatrix>,
)
