package sincmotion.internals

import sincmaths.SincMatrix
import sincmaths.sincmatrix.*
import kotlin.math.roundToInt

internal fun gsi(accelMat: SincMatrix, fs: Double): GsiOutcomes {
    val arx = accelMat.getCol(1).acf((fs * 4.0).toInt())
    val ary = accelMat.getCol(2).acf((fs * 4.0).toInt())
    val arz = accelMat.getCol(3).acf((fs * 4.0).toInt())

    val atLoc = estimateStrideIndex(accelMat.getCol(3), arz, fs)
    arx.setWithLV(arx.lessThan(0.0), 0.0)
    ary.setWithLV(ary.lessThan(0.0), 0.0)
    arz.setWithLV(arz.lessThan(0.0), 0.0)
    val cStep = ((arx + ary) + arz).sqrt()
    val cStepLocations = cStep.findpeaks()
    val minValues = (cStepLocations - atLoc).abs()
    val minVal = minValues.min(1)
    val minValAt = minValues.equalsTo(minVal).find()
    val tStrideI = minValAt[1].toInt()
    // Use the first result
    val tStrideSample = cStepLocations[tStrideI]
    val valueAt = (tStrideSample * 0.5).roundToInt()
    val gSymIndex = cStep[valueAt] / kotlin.math.sqrt(3.0)
    return GsiOutcomes(gSymIndex, tStrideSample)
}

internal fun estimateStrideIndex(aVert:SincMatrix, arz:SincMatrix, fs:Double): Double {
    val filteredData = lowPassAt100With3(arz)

    val possibleLocations = filteredData.findpeaks()

    val cwtScale = 16.0
    val aVertInt = (aVert / fs).cumsum()
    val dy = aVertInt.diffWithWavelet(cwtScale, 1 / fs)
    val periodPeaks = (-dy).findpeaks()
    val period = (periodPeaks.diff().median().scalar * 2.0).roundToInt().toDouble()

    val minValues = (possibleLocations-period).abs()
    val minVal = minValues.min(1)
    val minValAt = minValues.equalsTo(minVal).find()
    val strideIndexI = minValAt[1].toInt()

    return possibleLocations[strideIndexI]
}

internal data class GsiOutcomes(val value: Double, val tStride: Double)