package sincmotion

expect class SincMotionProcessor {
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
}