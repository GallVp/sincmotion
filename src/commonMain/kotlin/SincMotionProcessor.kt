package sincmotion

expect class SincMotionProcessor() {
    /**
     * All input arrays should be in row major format.
     */
    fun computeGaitParameters(
        timeVector: DoubleArray,
        accelData: DoubleArray,
        rotData: DoubleArray,
        gyroData: DoubleArray,
        fs: Double,
        personHeight: Double,
    ): GaitParameters?

    /**
     * All input arrays should be in row major format.
     */
    fun computeBalanceParameters(
        accelData: DoubleArray,
        rotData: DoubleArray,
        fs: Double,
    ): BalanceParameters?
}

internal val SincMotionProcessor.isDebug: Boolean
    get() = false

/**
 * 1 ->> Matlab ref. ver. = 0.2
 *
 * 2 ->> Matlab ref. ver. = 0.3
 */
val SincMotionProcessor.processorVersion : Int
    get() = 2