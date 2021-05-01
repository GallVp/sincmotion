package sincmotion.internals

import sincmaths.SincMatrix
import sincmaths.sincmatrix.*

internal fun cgsOutcomes(
    accelData: SincMatrix,
    rotData: SincMatrix,
    gyroData: SincMatrix,
    fs: Double,
    personHeight: Double,
    isAndroid: Boolean
): CGSOutcomes {

    val preprocessedData = cgsPreprocessedData(accelData, rotData, gyroData, fs, isAndroid)

    val accelDataRotated = preprocessedData.accelDataRotated
    val gyroDataProcessed = preprocessedData.gyroData

    val (gSymIndex, tStrideSample) = gsi(accelDataRotated, fs)

    val truncatedAccData = accelDataRotated["$tStrideSample + 1:end, 3"]
    val aVert = truncatedAccData - truncatedAccData.mean().scalar
    val gAP = gyroDataProcessed["$tStrideSample + 1:end, 3"]
    // For iOS, Z is the intrinsic AP axis. Counter clock-wise rotations are positive.
    // Reference: https://developer.apple.com/documentation/coremotion/getting_processed_device-motion_data/understanding_reference_frames_and_device_attitude
    // Thus, positive gyro angles correspond to right swing phase. At the right
    // heel strike the phone is at its counter -clockwise peak .

    val L = personHeight * 0.5
    val fL = personHeight * 0.16

    val (ICs, isLeftIC) = footEvents(aVert, gAP, fs)

    val (stepLengths, leftStepLengths, rightStepLengths) = vertMovements(
        aVert,
        L,
        fL,
        fs,
        tStrideSample.toInt(),
        ICs,
        isLeftIC
    )

    val stepTimes = ICs.diff() / fs
    val leftStepTimes = stepTimes.getWithLV(isLeftIC[2..isLeftIC.numel()])
    val rightStepTimes = stepTimes.getWithLV(!isLeftIC[2..isLeftIC.numel()])

    return CGSOutcomes(
        gSymIndex,
        stepLengths,
        leftStepLengths,
        rightStepLengths,
        stepTimes,
        leftStepTimes,
        rightStepTimes
    )
}

internal data class CGSOutcomes(
    val gSymIndex: Double,
    val stepLengths: SincMatrix,
    val leftStepLengths: SincMatrix,
    val rightStepLengths: SincMatrix,
    val stepTimes: SincMatrix,
    val leftStepTimes: SincMatrix,
    val rightStepTimes: SincMatrix
)