package io.github.gallvp.sincmotion.gaitandbalance

data class GnBGaitOutcomes(
    val meanSymIndex: Double,
    val meanStepLength: Double,
    val meanStepTime: Double,
    val stepLengthVariability: Double,
    val stepTimeVariability: Double,
    val stepLengthAsymmetry: Double,
    val stepTimeAsymmetry: Double,
    val meanStepVelocity: Double,
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

    override fun toString(): String =
        "Walking balance: $meanSymIndex %\n" +
            "Step length: $meanStepLength m\n" +
            "Step time: $meanStepTime s\n" +
            "Step length variability: $stepLengthVariability %\n" +
            "Step time variability: $stepTimeVariability %\n" +
            "Step length asymmetry: $stepLengthAsymmetry %\n" +
            "Step time asymmetry: $stepTimeAsymmetry %\n" +
            "Walking speed: $meanStepVelocity m/s"

    val array by lazy {
        listOf(
            meanSymIndex,
            meanStepLength,
            meanStepTime,
            stepLengthVariability,
            stepTimeVariability,
            stepLengthAsymmetry,
            stepTimeAsymmetry,
            meanStepVelocity,
        )
    }
    val map by lazy {
        keys.zip(array).toMap()
    }

    companion object {
        const val NUM_PARAMETERS: Int = 8
        const val DEFAULT_PARAMETER_KEY = "sym"
        const val DEFAULT_PARAMETER_INDEX = 0
        val keys by lazy {
            listOf("sym", "s-len", "s-time", "slv", "stv", "sla", "sta", "s-vel")
        }
        val presentationKeys by lazy {
            listOf("sym", "s-vel", "s-len", "s-time", "slv", "stv", "sla", "sta")
        }
        val summaryKeys by lazy {
            listOf("sym", "s-vel")
        }
        val normativeRangeType by lazy {
            keys.zip(
                listOf(
                    NormativeRangeType.LOWER,
                    NormativeRangeType.LOWER,
                    NormativeRangeType.UPPER,
                    NormativeRangeType.UPPER,
                    NormativeRangeType.UPPER,
                    NormativeRangeType.UPPER,
                    NormativeRangeType.UPPER,
                    NormativeRangeType.LOWER,
                ),
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
                    "m/s",
                ),
            ).toMap()
        }
        val names by lazy {
            keys.zip(
                listOf(
                    "Walking balance",
                    "Step length",
                    "Step time",
                    "Step length variability",
                    "Step time variability",
                    "Step length asymmetry",
                    "Step time asymmetry",
                    "Walking speed",
                ),
            ).toMap()
        }
        val namesWithUnits by lazy {
            keys.zip(
                listOf(
                    "Walking balance (%)",
                    "Step length (m)",
                    "Step time (s)",
                    "Step length variability (%)",
                    "Step time variability (%)",
                    "Step length asymmetry (%)",
                    "Step time asymmetry (%)",
                    "Walking speed (m/s)",
                ),
            ).toMap()
        }
    }
}