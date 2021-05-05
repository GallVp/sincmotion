package sincmotion

data class GaitParameters(
    val meanSymIndex: Double,
    val meanStepLength: Double,
    val meanStepTime: Double,
    val stepLengthVariability: Double,
    val stepTimeVariability: Double,
    val stepLengthAsymmetry: Double,
    val stepTimeAsymmetry: Double,
    val meanStepVelocity: Double
) {
    override fun toString(): String = "Gait symmetry index: $meanSymIndex %\n" +
            "Step length: $meanStepLength m\n" +
            "Step time: $meanStepTime sec\n" +
            "Step length variability: $stepLengthVariability %\n" +
            "Step time variability: $stepTimeVariability %\n" +
            "Step length asymmetry: $stepLengthAsymmetry %\n" +
            "Step time asymmetry: $stepTimeAsymmetry %\n" +
            "Step velocity: $meanStepVelocity m/sec"

    val array = doubleArrayOf(
        meanSymIndex,
        meanStepLength,
        meanStepTime,
        stepLengthVariability,
        stepTimeVariability,
        stepLengthAsymmetry,
        stepTimeAsymmetry,
        meanStepVelocity
    )
}