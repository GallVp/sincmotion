package io.github.gallvp.sincmotion.gaitandbalance

import io.github.gallvp.sincmaths.SincMatrix
import io.github.gallvp.sincmaths.asSincMatrix
import io.github.gallvp.sincmaths.copyOf
import io.github.gallvp.sincmaths.cross
import io.github.gallvp.sincmaths.div
import io.github.gallvp.sincmaths.dot
import io.github.gallvp.sincmaths.elPow
import io.github.gallvp.sincmaths.get
import io.github.gallvp.sincmaths.getRow
import io.github.gallvp.sincmaths.isRow
import io.github.gallvp.sincmaths.length
import io.github.gallvp.sincmaths.matrixOf
import io.github.gallvp.sincmaths.plus
import io.github.gallvp.sincmaths.quat2rotm
import io.github.gallvp.sincmaths.scalar
import io.github.gallvp.sincmaths.setRow
import io.github.gallvp.sincmaths.sqrt
import io.github.gallvp.sincmaths.sum
import io.github.gallvp.sincmaths.t
import io.github.gallvp.sincmaths.times
import io.github.gallvp.sincmaths.unaryMinus

actual fun applyGnBFrameCorrection(
    accelDataSI: SincMatrix,
    rotData: SincMatrix,
): SincMatrix {
    val accelMLxAPxVert = accelDataSI.copyOf()
    val initialToReference = rotData.getRow(1).quat2rotm()
    val referenceToInitial = initialToReference.t
    val gUser = accelMLxAPxVert.getRow(1)
    val zVector = -gUser
    val initialToXArbitraryZVertical = gravityVector2RotMat(zVector)
    val referenceToXArbitraryZVertical = initialToXArbitraryZVertical * referenceToInitial

    for (i in 1..(accelMLxAPxVert).length) {
        val frameToReference = rotData.getRow(i).quat2rotm()
        val frameToXArbitraryZVertical = referenceToXArbitraryZVertical * frameToReference
        val correctedSample = frameToXArbitraryZVertical * accelMLxAPxVert.getRow(i).t
        accelMLxAPxVert.setRow(i, correctedSample)
    }
    return accelMLxAPxVert
}

/**
 * Return user to reference pre-multiplying rotation matrix
 */
fun gravityVector2RotMat(zVector: SincMatrix): SincMatrix {
    var zVectorTaken =
        if (zVector.isRow) {
            zVector.t
        } else {
            zVector
        }

    zVectorTaken /= zVectorTaken.elPow(2.0).sum().sqrt().scalar

    val zUnit = doubleArrayOf(0.0, 0.0, 1.0).asSincMatrix(m = 3, n = 1)

    // Take zVector to unit vector
    val v = zVectorTaken.cross(zUnit)
    val c = zVectorTaken.dot(zUnit).scalar

    val vCross =
        doubleArrayOf(0.0, -v[3], v[2], v[3], 0.0, -v[1], -v[2], v[1], 0.0).asSincMatrix(3, 3)

    return matrixOf(
        3, 3, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0,
    ) + vCross + (vCross * vCross) * (1.0 / (c + 1.0))
}

actual fun truncateGnBGaitSegment(
    accelMLxAPxVert: SincMatrix,
    gyroData: SincMatrix,
    fs: Double,
): GnBPreprocessedGaitSegment =
    GnBPreprocessedGaitSegment(
        accelMLxAPxVert["(2*$fs)+1:end,:"],
        gyroData["(2*$fs)+1:end,:"],
    )

actual fun truncateGnBStaticStream(
    accelData: SincMatrix,
    rotData: SincMatrix,
    fs: Double,
): TruncatedGnBStaticStream {
    val truncatedAccelData: SincMatrix =
        accelData.get { endR, _, _, allC ->
            Pair((3.0 * fs).toInt() + 1..endR, allC)
        }
    val truncatedRotData: SincMatrix =
        rotData.get { endR, _, _, allC ->
            Pair((3.0 * fs).toInt() + 1..endR, allC)
        }

    return TruncatedGnBStaticStream(truncatedAccelData, truncatedRotData)
}
