package io.github.gallvp.sincmotion.gaitandbalance

import io.github.gallvp.sincmaths.SincMatrix
import io.github.gallvp.sincmaths.copyOf
import io.github.gallvp.sincmaths.getRow
import io.github.gallvp.sincmaths.length
import io.github.gallvp.sincmaths.quat2rotm
import io.github.gallvp.sincmaths.setRow
import io.github.gallvp.sincmaths.t
import io.github.gallvp.sincmaths.times

actual fun applyGnBFrameCorrection(
    accelData: SincMatrix,
    rotData: SincMatrix,
    fs: Double,
): SincMatrix {
    val accelMLxAPxVert = accelData.copyOf()
    for (i in 1..(accelMLxAPxVert).length) {
        val rotMat = rotData.getRow(i).quat2rotm()
        val correctedSample = rotMat * accelMLxAPxVert.getRow(i).t
        accelMLxAPxVert.setRow(i, correctedSample)
    }
    return accelMLxAPxVert
}

actual fun truncateGnBGaitSegment(
    accelMLxAPxVert: SincMatrix,
    gyroData: SincMatrix,
    fs: Double,
): GnBPreprocessedGaitSegment = GnBPreprocessedGaitSegment(accelMLxAPxVert, gyroData)

actual fun truncateGnBStaticStream(
    accelData: SincMatrix,
    rotData: SincMatrix,
    fs: Double,
): TruncatedGnBStaticStream = TruncatedGnBStaticStream(accelData, rotData)
