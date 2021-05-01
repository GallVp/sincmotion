package sincmotion.internals

import sincmaths.SincMatrix
import sincmaths.sincmatrix.*
import kotlin.math.roundToInt

internal fun gsi(accelMat: SincMatrix, fs: Double): GsiOutcomes {
    val arx = accelMat.getCol(1).acf((fs * 4.0).toInt())
    val ary = accelMat.getCol(2).acf((fs * 4.0).toInt())
    val arz = accelMat.getCol(3).acf((fs * 4.0).toInt())
    // Low pass at 3 Hz
    val filteredData = lowPassAt100With3(arz)
    val peakLocations = filteredData.findpeaks()
    val atLoc = peakLocations[2]
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

internal data class GsiOutcomes(val value: Double, val tStride: Double)