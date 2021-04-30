package sincmotion

import sincmaths.asSincMatrix
import sincmotion.internals.cgOutcomes

actual class SincMotionProcessor {

    actual fun computeGaitParameters(
        timeVector: DoubleArray,
        accelData: DoubleArray,
        rotData: DoubleArray,
        gyroData: DoubleArray,
        fs: Double,
        personHeight: Double
    ): GaitParameters? {
        return try {
            cgOutcomes(
                timeVector.asSincMatrix(false),
                accelData.asSincMatrix(accelData.size / 3, 3),
                rotData.asSincMatrix(rotData.size / 3, 3),
                gyroData.asSincMatrix(rotData.size / 4, 4),
                fs,
                personHeight,
                false
            )
        } catch (e: Exception) {
            null
        }
    }
}