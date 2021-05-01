package sincmotion

import sincmaths.asSincMatrix
import sincmotion.internals.cgOutcomes
import sincmotion.internals.sbOutcomes

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
                rotData.asSincMatrix(rotData.size / 4, 4),
                gyroData.asSincMatrix(gyroData.size / 3, 3),
                fs,
                personHeight,
                false
            )
        } catch (e: Exception) {
            if(isDebug){e.printStackTrace()}
            null
        }
    }

    actual fun computeBalanceParameters(
        accelData: DoubleArray,
        rotData: DoubleArray,
        fs: Double
    ): BalanceParameters? {
        return try {
            sbOutcomes(
                accelData.asSincMatrix(accelData.size / 3, 3),
                rotData.asSincMatrix(rotData.size / 4, 4),
                fs,
                false
            )
        } catch (e: Exception) {
            if(isDebug){e.printStackTrace()}
            null
        }
    }
}