package io.github.gallvp.sincmotion.core

import io.github.gallvp.sincmaths.SincMatrix
import io.github.gallvp.sincmaths.abs
import io.github.gallvp.sincmaths.acf
import io.github.gallvp.sincmaths.asIntArray
import io.github.gallvp.sincmaths.asSincMatrix
import io.github.gallvp.sincmaths.cat
import io.github.gallvp.sincmaths.colVectorOf
import io.github.gallvp.sincmaths.cumSum
import io.github.gallvp.sincmaths.diff
import io.github.gallvp.sincmaths.diffWithWavelet
import io.github.gallvp.sincmaths.div
import io.github.gallvp.sincmaths.elPow
import io.github.gallvp.sincmaths.equalsTo
import io.github.gallvp.sincmaths.et
import io.github.gallvp.sincmaths.find
import io.github.gallvp.sincmaths.findPeaks
import io.github.gallvp.sincmaths.first
import io.github.gallvp.sincmaths.flip
import io.github.gallvp.sincmaths.get
import io.github.gallvp.sincmaths.getCol
import io.github.gallvp.sincmaths.getWithLV
import io.github.gallvp.sincmaths.gt
import io.github.gallvp.sincmaths.length
import io.github.gallvp.sincmaths.lt
import io.github.gallvp.sincmaths.max
import io.github.gallvp.sincmaths.maxI
import io.github.gallvp.sincmaths.mean
import io.github.gallvp.sincmaths.median
import io.github.gallvp.sincmaths.min
import io.github.gallvp.sincmaths.minI
import io.github.gallvp.sincmaths.minus
import io.github.gallvp.sincmaths.movMean
import io.github.gallvp.sincmaths.not
import io.github.gallvp.sincmaths.numel
import io.github.gallvp.sincmaths.ones
import io.github.gallvp.sincmaths.or
import io.github.gallvp.sincmaths.plus
import io.github.gallvp.sincmaths.repMat
import io.github.gallvp.sincmaths.rowVectorOf
import io.github.gallvp.sincmaths.scalar
import io.github.gallvp.sincmaths.set
import io.github.gallvp.sincmaths.setWithLV
import io.github.gallvp.sincmaths.sqrt
import io.github.gallvp.sincmaths.sum
import io.github.gallvp.sincmaths.times
import io.github.gallvp.sincmaths.unaryMinus
import kotlin.math.ln
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun estimatePosturalStability(accelMLxAPxVert: SincMatrix): EstimatedPosturalStability {
    val stabilityML = -ln(accelMLxAPxVert.getCol(1).abs().mean().scalar)
    val stabilityAP = -ln(accelMLxAPxVert.getCol(2).abs().mean().scalar)

    val resultantVector = (accelMLxAPxVert.elPow(2.0) * SincMatrix.ones(3, 1)).sqrt()
    val stabilityR = -ln(resultantVector.abs().mean().scalar)

    return EstimatedPosturalStability(stabilityR, stabilityML, stabilityAP)
}

data class EstimatedPosturalStability(
    val stabilityR: Double,
    val stabilityML: Double,
    val
    stabilityAP: Double,
)

fun estimateGaitOutcomes(
    accelMLxAPxVert: SincMatrix,
    gyroAboutAP: SincMatrix,
    fs: Double,
    personHeight: Double,
): EstimatedGaitOutcomes {
    val (symIndex, tStrideSample) = estimateGaitSymIndex(accelMLxAPxVert, fs)

    var accelVert = accelMLxAPxVert["$tStrideSample + 1:end, 3"]
    accelVert -= accelVert.mean().scalar
    val gAP = gyroAboutAP["$tStrideSample + 1:end"]

    val halfOfHeight = personHeight * 0.5
    val footLength = personHeight * 0.16

    val (initialContacts, isLeftIC) = estimateFootEvents(accelVert, gAP, fs)

    val (stepLengths, leftStepLengths, rightStepLengths) =
        estimateStepLengths(
            accelVert,
            halfOfHeight,
            footLength,
            fs,
            tStrideSample.toInt(),
            initialContacts,
            isLeftIC,
        )

    val stepTimes = initialContacts.diff() / fs
    val leftStepTimes = stepTimes.getWithLV(isLeftIC[2..isLeftIC.numel])
    val rightStepTimes = stepTimes.getWithLV(!isLeftIC[2..isLeftIC.numel])

    return EstimatedGaitOutcomes(
        symIndex,
        stepLengths,
        leftStepLengths,
        rightStepLengths,
        stepTimes,
        leftStepTimes,
        rightStepTimes,
    )
}

data class EstimatedGaitOutcomes(
    val symIndex: Double,
    val stepLengths: SincMatrix,
    val leftStepLengths: SincMatrix,
    val rightStepLengths: SincMatrix,
    val stepTimes: SincMatrix,
    val leftStepTimes: SincMatrix,
    val rightStepTimes: SincMatrix,
)

fun estimateGaitSymIndex(
    accelMLxAPxVert: SincMatrix,
    fs: Double,
): EstimatedGaitSymIndex {
    fun estimateStrideIndex(
        aVert: SincMatrix,
        arVert: SincMatrix,
        fs: Double,
    ): Double {
        val filteredData = lowPassAt100With3(arVert)

        val possibleLocations = filteredData.findPeaks()

        val cwtScale = 16.0
        val aVertInt = (aVert / fs).cumSum()
        val dy = aVertInt.diffWithWavelet(cwtScale, 1 / fs)
        val periodPeaks = (-dy).findPeaks()
        val period = (periodPeaks.diff().median().scalar * 2.0).roundToInt().toDouble()

        val minValues = (possibleLocations - period).abs()
        val minVal = minValues.min(1)
        val minValAt = minValues.equalsTo(minVal).find()
        val strideIndexI = minValAt[1].toInt()

        return possibleLocations[strideIndexI]
    }

    val accelSignalLen = accelMLxAPxVert.length
    val lenForAcf = min(fs * 4.0, accelSignalLen - 1.0).toInt()

    val arML = accelMLxAPxVert.getCol(1).acf(lenForAcf)
    val arAP = accelMLxAPxVert.getCol(2).acf(lenForAcf)
    val arVert = accelMLxAPxVert.getCol(3).acf(lenForAcf)

    arML.setWithLV(arML lt 0.0, 0.0)
    arAP.setWithLV(arAP lt 0.0, 0.0)
    arVert.setWithLV(arVert lt 0.0, 0.0)

    val cStep = ((arML + arAP) + arVert).sqrt()
    val cStepPeaks = cStep.findPeaks()
    val cStepPeakAmps = cStep[cStepPeaks.asIntArray()]

    val cStepLows = ((cStep lt 0.25 * sqrt(3.0)) or (cStep et 0.25 * sqrt(3.0))).find()
    val validityStart = cStepLows.first

    val validLocations = cStepPeaks.getWithLV(cStepPeaks gt 2.0 * validityStart)
    val tStrideAI = cStep[validLocations.asIntArray()].maxI()
    val tStrideA = validLocations[tStrideAI.asIntArray()]
    val tStepA = (tStrideA * 0.5).scalar.roundToInt()
    val valueA =
        if (tStepA < validityStart) {
            0.0
        } else {
            cStep[tStepA] / sqrt(3.0)
        }

    val atLocB = estimateStrideIndex(accelMLxAPxVert.getCol(3), arVert, fs)
    val tStrideIB = ((cStepPeaks - atLocB).abs()).minI()
    val tStrideB = cStepPeaks[tStrideIB.asIntArray()]
    val tStepB = (tStrideB * 0.5).scalar.roundToInt()
    val valueB =
        if (tStepB < validityStart) {
            0.0
        } else {
            cStep[tStepB] / sqrt(3.0)
        }

    val tStrideCI = cStepPeakAmps.maxI()
    val tStrideC = cStepPeaks[tStrideCI.asIntArray()]
    val tStepC = (tStrideC * 0.5).scalar.roundToInt()
    val valueC =
        if (tStepC < validityStart) {
            0.0
        } else {
            cStep[tStepC] / sqrt(3.0)
        }

    val maxValue = rowVectorOf(valueA, valueB, valueC).max().scalar
    val maxValInd = rowVectorOf(valueA, valueB, valueC).maxI().scalar

    val tStrideVector = rowVectorOf(tStrideA.scalar, tStrideB.scalar, tStrideC.scalar)

    val tStride =
        if (maxValue == 0.0) {
            tStrideVector.max().scalar
        } else {
            tStrideVector[maxValInd.toInt()]
        }

    val tStep = (tStride * 0.5).roundToInt()
    val gSymIndex = cStep[tStep] / sqrt(3.0)

    return EstimatedGaitSymIndex(gSymIndex, tStride)
}

data class EstimatedGaitSymIndex(val value: Double, val tStride: Double)

fun estimateFootEvents(
    aVert: SincMatrix,
    gAP: SincMatrix,
    fs: Double,
): EstimatedFootEvents {
    val cwtScale = 16.0

    val aVertInt = (aVert / fs).cumSum()

    val dy = aVertInt.diffWithWavelet(cwtScale, 1 / fs)
    val dyy = dy.diffWithWavelet(cwtScale, 1 / fs)

    val gAPSmooth = lowPassAt100With2(gAP)

    val initialContacts = (-dy).findPeaks()

    val finalContacts = dyy.findPeaks()

    var isLeftIC = gAPSmooth[initialContacts.asIntArray()] lt 0.0

    // Detect anomaly in isLeftIC and apply pattern based correction.
    if (isLeftIC.diff().abs().sum().scalar.toInt() != (isLeftIC.numel - 1)) {
        var candidateA = colVectorOf(1, 0).repMat(20, 1)
        var candidateB = colVectorOf(0, 1).repMat(20, 1)

        candidateA = candidateA[1..isLeftIC.numel]
        candidateB = candidateB[1..isLeftIC.numel]

        val errorA = (candidateA - isLeftIC).elPow(2.0).sum().sqrt().scalar
        val errorB = (candidateB - isLeftIC).elPow(2.0).sum().sqrt().scalar

        isLeftIC =
            if (errorA < errorB) {
                candidateA et 1.0
            } else {
                candidateB et 1.0
            }
    }

    return EstimatedFootEvents(initialContacts, isLeftIC, finalContacts)
}

data class EstimatedFootEvents(
    val initialContacts: SincMatrix,
    val isLeftIC: SincMatrix,
    val finalContacts: SincMatrix,
)

fun estimateStepLengths(
    aVert: SincMatrix,
    halfOfHeight: Double,
    footLength: Double,
    fs: Double,
    tStride: Int,
    initialContacts: SincMatrix,
    isLeftIC: SincMatrix,
): EstimatedStepLengths {
    val optimalK = 0.584830

    var v = (-aVert / fs).cumSum()

    v -= v.movMean(tStride)

    var dp = (v / fs).cumSum()

    dp -= dp.movMean(tStride)

    var dAppended = dp.flip().cat(1, dp, dp.flip())
    dAppended = bandPassAt100From0d1To45(dAppended)

    val dpLength = dp.numel
    val d = dAppended["$dpLength + 1 : end - $dpLength"]

    val hs = SincMatrix.ones(1, initialContacts.numel - 1)
    val hsLeftList = ArrayList<Double>(hs.numel)
    val hsRightList = ArrayList<Double>(hs.numel)

    for (i in 2..initialContacts.numel) {
        val dValues = d[initialContacts[i - 1].toInt()..initialContacts[i].toInt()]
        hs[i - 1] = dValues.max(1).scalar - (dValues[1] + dValues[dValues.numel]) / 2.0

        if (isLeftIC[i] != 0.0) {
            hsLeftList.add(hs[i - 1])
        } else {
            hsRightList.add(hs[i - 1])
        }
    }

    val hsLeft = hsLeftList.asSincMatrix()
    val hsRight = hsRightList.asSincMatrix()

    val stepLengths = 2.0 * (2.0 * halfOfHeight * hs - hs.elPow(2.0)).sqrt() + optimalK * footLength
    val leftStepLengths =
        2.0 * (2.0 * halfOfHeight * hsLeft - hsLeft.elPow(2.0)).sqrt() + optimalK * footLength
    val rightStepLengths =
        2.0 * (2.0 * halfOfHeight * hsRight - hsRight.elPow(2.0)).sqrt() + optimalK * footLength

    val distance = stepLengths.sum().scalar + 2.0 * stepLengths.median().scalar

    return EstimatedStepLengths(stepLengths, leftStepLengths, rightStepLengths, distance)
}

data class EstimatedStepLengths(
    val stepLengths: SincMatrix,
    val leftStepLengths: SincMatrix,
    val rightStepLengths: SincMatrix,
    val distance: Double,
)
