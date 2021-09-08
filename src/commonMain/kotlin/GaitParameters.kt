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

    override fun toString(): String = "Periodicity index: $meanSymIndex %\n" +
            "Step length: $meanStepLength m\n" +
            "Step time: $meanStepTime s\n" +
            "Step length variability: $stepLengthVariability %\n" +
            "Step time variability: $stepTimeVariability %\n" +
            "Step length asymmetry: $stepLengthAsymmetry %\n" +
            "Step time asymmetry: $stepTimeAsymmetry %\n" +
            "Step velocity: $meanStepVelocity m/s"

    val array by lazy {
        listOf(
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
    val map by lazy {
        keys.zip(array).toMap()
    }

    companion object {
        const val numParameters: Int = 8
        const val defaultParameterKey = "sym"
        const val defaultParameterIndex = 0
        val keys by lazy {
            listOf("sym", "s-len", "s-time", "slv", "stv", "sla", "sta", "s-vel")
        }
        val normativeRangeType by lazy {
            keys.zip(
                listOf(
                    NormativeRangeType.LOWER,
                    NormativeRangeType.MIDDLE,
                    NormativeRangeType.MIDDLE,
                    NormativeRangeType.UPPER,
                    NormativeRangeType.UPPER,
                    NormativeRangeType.UPPER,
                    NormativeRangeType.UPPER,
                    NormativeRangeType.MIDDLE,
                )
            ).toMap()
        }
        val units by lazy {
            keys.zip(
                listOf(
                    "%",
                    "m",
                    "s",
                    "%",
                    "%",
                    "%",
                    "%",
                    "m/s"
                )
            ).toMap()
        }
        val names by lazy {
            keys.zip(
                listOf(
                    "Periodicity index",
                    "Step length",
                    "Step time",
                    "Step length variability",
                    "Step time variability",
                    "Step length asymmetry",
                    "Step time asymmetry",
                    "Step velocity"
                )
            ).toMap()
        }
        val namesWithUnits by lazy {
            keys.zip(
                listOf(
                    "Periodicity index (%)",
                    "Step length (m)",
                    "Step time (s)",
                    "Step length variability (%)",
                    "Step time variability (%)",
                    "Step length asymmetry (%)",
                    "Step time asymmetry (%)",
                    "Step velocity (m/s)"
                )
            ).toMap()
        }
    }
}