package sincmotion

data class BalanceParameters(val maaMLOnNegLog: Double, val maaAPOnNegLog: Double, val maaROnNegLog: Double) {

    override fun toString(): String = "MAA ML: $maaMLOnNegLog -log[m/sec/sec]\n" +
            "MAA AP: $maaAPOnNegLog -log[m/sec/sec]\n" +
            "MAA R: $maaROnNegLog -log[m/sec/sec]"

    val array = doubleArrayOf(maaMLOnNegLog, maaAPOnNegLog, maaROnNegLog)
}