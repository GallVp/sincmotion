package sincmotion.internals

import sincmaths.SincMatrix
import sincmotion.BalanceParameters
import kotlin.math.ln

internal fun sbOutcomes(accelData: SincMatrix, rotData: SincMatrix, fs: Double, isAndroid: Boolean): BalanceParameters {
    val filteredData = sbPreprocessedData(accelData, rotData, fs, isAndroid)

    val (maaML, maaAP, maaR) = computeMAA(filteredData)

    return BalanceParameters(-ln(maaR), -ln(maaML), -ln(maaAP))
}