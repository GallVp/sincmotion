package sincmotion

data class BalanceParameters(val maaROnNegLog: Double, val maaMLOnNegLog: Double, val maaAPOnNegLog: Double) {

    constructor(map: Map<String, Double>) : this(
        map["maa-r"] ?: Double.NaN,
        map["maa-ml"] ?: Double.NaN,
        map["maa-ap"] ?: Double.NaN
    )

    override fun toString(): String = "MAA R: $maaROnNegLog -ln[m/sec/sec]\n" +
            "MAA ML: $maaMLOnNegLog -ln[m/sec/sec]\n" +
            "MAA AP: $maaAPOnNegLog -ln[m/sec/sec]"

    val array = listOf(maaROnNegLog, maaMLOnNegLog, maaAPOnNegLog)
    val map = keys.zip(array).toMap()

    companion object {
        const val numParameters: Int = 3
        const val defaultParameterKey = "maa-r"
        const val defaultParameterIndex = 0
        val keys = listOf("maa-r", "maa-ml", "maa-ap")
        val units = keys.zip(
            listOf(
                "-ln[m/sec/sec]",
                "-ln[m/sec/sec]",
                "-ln[m/sec/sec]"
            )
        ).toMap()
        val names = keys.zip(
            listOf(
                "Steadiness",
                "Steadiness ML",
                "Steadiness AP"
            )
        ).toMap()
        val namesWithUnits = keys.zip(
            listOf(
                "Steadiness (-ln[m/sec/sec])",
                "Steadiness ML (-ln[m/sec/sec])",
                "Steadiness AP (-ln[m/sec/sec])"
            )
        ).toMap()
    }
}