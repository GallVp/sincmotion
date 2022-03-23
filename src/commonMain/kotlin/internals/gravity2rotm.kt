package sincmotion.internals

import sincmaths.*

/**
 * Return user to reference pre-multiplying rotation matrix
 */
internal fun gravity2rotm(zVector: SincMatrix): SincMatrix {

    var zVectorTaken = if (zVector.isrow()) {
        zVector.transpose()
    } else {
        zVector
    }

    zVectorTaken /= zVectorTaken.elPow(2.0).sum().sqrt().scalar

    val zUnit = doubleArrayOf(0.0, 0.0, 1.0).asSincMatrix(m = 3, n = 1)

    // Take zVector to unit vector
    val v = zVectorTaken.cross(zUnit)
    val c = zVectorTaken.dot(zUnit).scalar

    val vCross = doubleArrayOf(0.0, -v[3], v[2], v[3], 0.0, -v[1], -v[2], v[1], 0.0).asSincMatrix(3, 3)

    return matrixOf(3, 3, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0) + vCross + (vCross * vCross) * (1.0 / (c + 1.0))
}