package io.github.gallvp.sincmotion.gaitandbalance

data class GnBStaticOutcomes(val stabilityR: Double, val stabilityML: Double, val stabilityAP: Double) {
    constructor(map: Map<String, Double>) : this(
        map["stb-r"] ?: Double.NaN,
        map["stb-ml"] ?: Double.NaN,
        map["stb-ap"] ?: Double.NaN,
    )

    override fun toString(): String =
        "Stability R: $stabilityR -ln[m/s/s]\n" +
            "Stability ML: $stabilityML -ln[m/s/s]\n" +
            "Stability AP: $stabilityAP -ln[m/s/s]"

    val array by lazy {
        listOf(stabilityR, stabilityML, stabilityAP)
    }
    val map by lazy {
        keys.zip(array).toMap()
    }

    companion object {
        const val NUM_PARAMETERS: Int = 3
        const val DEFAULT_PARAMETER_KEY = "stb-r"
        const val DEFAULT_PARAMETER_INDEX = 0
        val keys = listOf("stb-r", "stb-ml", "stb-ap")
        val presentationKeys = keys
        val summaryKeys = listOf("stb-r")
        val normativeRangeType by lazy {
            keys.zip(
                listOf(
                    NormativeRangeType.LOWER,
                    NormativeRangeType.LOWER,
                    NormativeRangeType.LOWER,
                ),
            ).toMap()
        }
        val units by lazy {
            keys.zip(
                listOf(
                    "-ln[m/s²]",
                    "-ln[m/s²]",
                    "-ln[m/s²]",
                ),
            ).toMap()
        }
        val names by lazy {
            keys.zip(
                listOf(
                    "Stability",
                    "Stability ML",
                    "Stability AP",
                ),
            ).toMap()
        }
        val namesWithUnits by lazy {
            keys.zip(
                listOf(
                    "Stability (-ln[m/s²])",
                    "Stability ML (-ln[m/s²])",
                    "Stability AP (-ln[m/s²])",
                ),
            ).toMap()
        }
    }
}
