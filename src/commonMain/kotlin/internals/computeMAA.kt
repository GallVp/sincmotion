package sincmotion.internals

import sincmaths.SincMatrix
import sincmaths.sincmatrix.*

internal fun computeMAA(accelMat:SincMatrix):MAAComputes {
    val maaML               = accelMat.getCol(1).abs().mean().scalar
    val maaAP               = accelMat.getCol(2).abs().mean().scalar

    val resultantVect       = (accelMat.elPow(2.0) * SincMatrix.ones(3, 1)).sqrt()
    val maaR                = resultantVect.abs().mean().scalar

    return MAAComputes(maaML, maaAP, maaR)
}

internal data class MAAComputes(val maaML:Double, val maaAP:Double, val maaR:Double)