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
    val keys = listOf("maa-r", "maa-ml", "maa-ap")
    val map = keys.zip(array).toMap()

    companion object {
        const val numParameters: Int = 3
    }
}