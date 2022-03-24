package sincmotion.internals

import sincmaths.*
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sqrt

internal fun gsi(accelMat: SincMatrix, fs: Double): GsiOutcomes {

    val accelSignalLen = accelMat.length()
    val lenForAcf = max(fs*4.0, accelSignalLen - 1.0).toInt()

    val arx = accelMat.getCol(1).acf(lenForAcf)
    val ary = accelMat.getCol(2).acf(lenForAcf)
    val arz = accelMat.getCol(3).acf(lenForAcf)

    arx.setWithLV(arx.lessThan(0.0), 0.0)
    ary.setWithLV(ary.lessThan(0.0), 0.0)
    arz.setWithLV(arz.lessThan(0.0), 0.0)

    val cStep = ((arx + ary) + arz).sqrt()
    val locs  = cStep.findpeaks()
    val locAmps = cStep[locs.asIntArray()]

    val cstepLows = ((cStep lt 0.25*sqrt(3.0)) or (cStep et 0.25*sqrt(3.0))).find()
    val validityStart = cstepLows.first

    val pLocs       = locs.getWithLV(locs gt 2.0*validityStart)
    val tstrideAI   = cStep[pLocs.asIntArray()].maxI()
    val tstrideA    = pLocs[tstrideAI.asIntArray()]
    val tstepA      = (tstrideA*0.5).scalar.roundToInt()
    val valueA = if(tstepA < validityStart) {
        0.0
    } else {
        cStep[tstepA] / sqrt(3.0)
    }

    val atLocB      = estimateStrideIndex(accelMat.getCol(3), arz, fs)
    val tstrideIB   = ((locs-atLocB).abs()).minI()
    val tstrideB    = locs[tstrideIB.asIntArray()]
    val tstepB      = (tstrideB*0.5).scalar.roundToInt()
    val valueB = if(tstepB < validityStart) {
        0.0
    } else {
        cStep[tstepB] / sqrt(3.0)
    }

    val tstrideCI   = locAmps.maxI()
    val tstrideC    = locs[tstrideCI.asIntArray()]
    val tstepC      = (tstrideC*0.5).scalar.roundToInt()
    val valueC = if(tstepC < validityStart) {
        0.0
    } else {
        cStep[tstepC] / sqrt(3.0)
    }

    val maxValue = rowVectorOf(valueA, valueB, valueC).max().scalar
    val maxValInd = rowVectorOf(valueA, valueB, valueC).maxI().scalar

    val tStrideVect     = rowVectorOf(tstrideA.scalar, tstrideB.scalar, tstrideC.scalar)

    val tStride = if(maxValue == 0.0) {
        tStrideVect.max().scalar
    } else {
        tStrideVect[maxValInd.toInt()]
    }

    val tStep = (tStride*0.5).roundToInt()
    val gSymIndex = cStep[tStep] / sqrt(3.0)

    return GsiOutcomes(gSymIndex, tStride)
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