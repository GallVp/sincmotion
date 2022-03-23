package sincmotion.internals

import sincmaths.*

internal fun cgsPreprocessedData(
    accelData: SincMatrix,
    rotData: SincMatrix,
    gyroData: SincMatrix,
    fs: Double,
    isAndroid: Boolean
): CGSPreprocessedData {
    // Convert acceleration units to m/sec/sec
    val accelDataSI = accelData * 9.8

    // Correct for orientation <X-ML, Y-AP, Z-Verticle>
    val accelDataRotated = accelDataSI.copyOf()
    if (isAndroid) {
        val initialToReference = rotData.getRow(1).quat2rotm()
        val referenceToInitial = initialToReference.t
        val gUser = accelDataRotated.getRow(1)
        val zVector = -gUser
        val initialToXArbitZVerticle = gravity2rotm(zVector)
        val referenceToXArbitZVerticle = initialToXArbitZVerticle * referenceToInitial

        for (i in 1..(accelDataRotated).length()) {
            val frameToReference = rotData.getRow(i).quat2rotm()
            val frameToToXArbitZVerticle = referenceToXArbitZVerticle * frameToReference
            val correctedSample = frameToToXArbitZVerticle * accelDataRotated.getRow(i).t
            accelDataRotated.setRow(i, correctedSample)
        }
    } else {
        for (i in 1..(accelDataRotated).length()) {
            val rotMat = rotData.getRow(i).quat2rotm()
            val correctedSample = rotMat * accelDataRotated.getRow(i).t
            accelDataRotated.setRow(i, correctedSample)
        }
    }

    // If Android: Remove initial segment where there is no walking
    return if (isAndroid) {
        val vertAccel = accelDataRotated.getCol(3).abs()
        val vertAccelSegment = accelDataRotated.getCol(3)[1..fs.toInt()].abs()
        val meanOfVertAccel = vertAccelSegment.mean().scalar
        val sdOfVertAccel = vertAccelSegment.std().scalar
        val walkingStartsAt = vertAccel.greaterThan(meanOfVertAccel + sdOfVertAccel * 5.0).find().first

        CGSPreprocessedData(accelDataRotated["$walkingStartsAt:end, :"], gyroData["$walkingStartsAt:end, :"])
    } else {
        CGSPreprocessedData(accelDataRotated, gyroData)
    }
}

internal data class CGSPreprocessedData(val accelDataRotated: SincMatrix, val gyroData: SincMatrix)
