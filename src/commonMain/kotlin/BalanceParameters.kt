package sincmotion

data class BalanceParameters(val maaROnNegLog: Double, val maaMLOnNegLog: Double, val maaAPOnNegLog: Double) {

    constructor(map: Map<String, Double>) : this(
        map["maa-r"] ?: Double.NaN,
        map["maa-ml"] ?: Double.NaN,
        map["maa-ap"] ?: Double.NaN
    )

    override fun toString(): String = "MAA R: $maaROnNegLog -ln[m/s/s]\n" +
            "MAA ML: $maaMLOnNegLog -ln[m/s/s]\n" +
            "MAA AP: $maaAPOnNegLog -ln[m/s/s]"

    val array = listOf(maaROnNegLog, maaMLOnNegLog, maaAPOnNegLog)
    val map = keys.zip(array).toMap()

    companion object {
        const val numParameters: Int = 3
        const val defaultParameterKey = "maa-r"
        const val defaultParameterIndex = 0
        val keys = listOf("maa-r", "maa-ml", "maa-ap")
        val units = keys.zip(
            listOf(
                "-ln[m/s²]",
                "-ln[m/s²]",
                "-ln[m/s²]"
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
                "Steadiness (-ln[m/s²])",
                "Steadiness ML (-ln[m/s²])",
                "Steadiness AP (-ln[m/s²])"
            )
        ).toMap()
    }
}