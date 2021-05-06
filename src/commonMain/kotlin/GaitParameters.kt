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

    constructor(map: Map<String, Double>) : this(
        map["sym"] ?: Double.NaN,
        map["s-len"] ?: Double.NaN,
        map["s-time"] ?: Double.NaN,
        map["slv"] ?: Double.NaN,
        map["stv"] ?: Double.NaN,
        map["sla"] ?: Double.NaN,
        map["sta"] ?: Double.NaN,
        map["s-vel"] ?: Double.NaN,
    )

    override fun toString(): String = "Gait symmetry index: $meanSymIndex %\n" +
            "Step length: $meanStepLength m\n" +
            "Step time: $meanStepTime sec\n" +
            "Step length variability: $stepLengthVariability %\n" +
            "Step time variability: $stepTimeVariability %\n" +
            "Step length asymmetry: $stepLengthAsymmetry %\n" +
            "Step time asymmetry: $stepTimeAsymmetry %\n" +
            "Step velocity: $meanStepVelocity m/sec"

    val array = listOf(
        meanSymIndex,
        meanStepLength,
        meanStepTime,
        stepLengthVariability,
        stepTimeVariability,
        stepLengthAsymmetry,
        stepTimeAsymmetry,
        meanStepVelocity
    )
    val keys = listOf("sym", "s-len", "s-time", "slv", "stv", "sla", "sta", "s-vel")
    val map = keys.zip(array).toMap()

    companion object {
        const val numParameters:Int = 8
    }
}