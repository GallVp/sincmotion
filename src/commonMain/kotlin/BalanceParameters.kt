package sincmotion

data class BalanceParameters(val maaROnNegLog: Double, val maaMLOnNegLog: Double, val maaAPOnNegLog: Double) {

    constructor(map: Map<String, Double>) : this(
        map["maa-r"] ?: Double.NaN,
        map["maa-ml"] ?: Double.NaN,
        map["maa-ap"] ?: Double.NaN
    )

    override fun toString(): String = "Stability R: $maaROnNegLog -ln[m/s/s]\n" +
            "Stability ML: $maaMLOnNegLog -ln[m/s/s]\n" +
            "Stability AP: $maaAPOnNegLog -ln[m/s/s]"

    val array by lazy {
        listOf(maaROnNegLog, maaMLOnNegLog, maaAPOnNegLog)
    }
    val map by lazy {
        keys.zip(array).toMap()
    }

    companion object {
        const val numParameters: Int = 3
        const val defaultParameterKey = "maa-r"
        const val defaultParameterIndex = 0
        val keys = listOf("maa-r", "maa-ml", "maa-ap")
        val presentationKeys = keys
        val normativeRangeType by lazy {
            keys.zip(
                listOf(
                    NormativeRangeType.LOWER,
                    NormativeRangeType.LOWER,
                    NormativeRangeType.LOWER
                )
            ).toMap()
        }
        val units by lazy {
            keys.zip(
                listOf(
                    "-ln[m/s²]",
                    "-ln[m/s²]",
                    "-ln[m/s²]"
                )
            ).toMap()
        }
        val names by lazy {
            keys.zip(
                listOf(
                    "Stability",
                    "Stability ML",
                    "Stability AP"
                )
            ).toMap()
        }
        val namesWithUnits by lazy {
            keys.zip(
                listOf(
                    "Stability (-ln[m/s²])",
                    "Stability ML (-ln[m/s²])",
                    "Stability AP (-ln[m/s²])"
                )
            ).toMap()
        }
    }
}