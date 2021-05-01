package sincmotion

data class GaitParameters(
    val meanSymIndex: Double,
    val meanStepLength: Double,
    val meanStepTime: Double,
    val stepLengthVariability: Double,
    val stepTimeVariability: Double,
    val stepLengthASymmetry: Double,
    val stepTimeASymmetry: Double,
    val meanStepVelocity: Double
) {
    override fun toString(): String = "Gait symmetry index: $meanSymIndex %\n" +
            "Step length: $meanStepLength m\n" +
            "Step time: $meanStepTime sec\n" +
            "Step length variability: $stepLengthVariability %\n" +
            "Step time variability: $stepTimeVariability %\n" +
            "Step length asymmetry: $stepLengthASymmetry %\n" +
            "Step time asymmetry: $stepTimeASymmetry %\n'" +
            "Step velocity: $meanStepVelocity m/sec"
}