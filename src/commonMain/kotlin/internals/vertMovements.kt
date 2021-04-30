package sincmotion.internals

import sincmaths.SincMatrix
import sincmaths.asSincMatrix
import sincmaths.sincmatrix.*

internal fun vertMovements(
    aVert: SincMatrix,
    lLength: Double,
    fLength: Double,
    fs: Double,
    tStride: Int,
    ICs: SincMatrix,
    isLeftIC: SincMatrix
): VertMovements {
    val OPTIMAL_K = 0.584830

    var v = (-aVert / fs).cumsum()

    v -= v.movmean(tStride)

    var dp = (v / fs).cumsum()

    dp -= dp.movmean(tStride)

    var dAppended = dp.flip().cat(1, dp, dp.flip())
    dAppended = bandPassAt100From0d1To45(dAppended)

    val dpLength = dp.numel()
    val d = dAppended["$dpLength + 1 : end - $dpLength"]


    val hs = SincMatrix.ones(1, ICs.numel() - 1)
    val hsLeftList = ArrayList<Double>(hs.numel())
    val hsRightList = ArrayList<Double>(hs.numel())

    for (i in 2..ICs.numel()) {
        val H = d[ICs[i - 1].toInt()..ICs[i].toInt()]
        hs[i - 1] = H.max(1).scalar - (H[1] + H[H.numel()]) / 2.0

        if (isLeftIC[i] != 0.0) {
            hsLeftList.add(hs[i - 1])
        } else {
            hsRightList.add(hs[i - 1])
        }
    }

    val hsLeft = hsLeftList.asSincMatrix()
    val hsRight = hsRightList.asSincMatrix()

    val stepLengths = 2.0 * (2.0 * lLength * hs - hs.elPow(2.0)).sqrt() + OPTIMAL_K * fLength
    val leftStepLengths = 2.0 * (2.0 * lLength * hsLeft - hsLeft.elPow(2.0)).sqrt() + OPTIMAL_K * fLength
    val rightStepLengths = 2.0 * (2.0 * lLength * hsRight - hsRight.elPow(2.0)).sqrt() + OPTIMAL_K * fLength

    val distance = stepLengths.sum().scalar + 2.0 * stepLengths.median().scalar

    return VertMovements(stepLengths, leftStepLengths, rightStepLengths, distance)
}

internal data class VertMovements(
    val stepLengths: SincMatrix,
    val leftStepLengths: SincMatrix,
    val rightStepLengths: SincMatrix,
    val distance: Double
)