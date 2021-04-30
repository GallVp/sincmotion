package sincmotion.internals

import sincmaths.SincMatrix
import sincmaths.sincmatrix.*

internal fun footEvents(aVert: SincMatrix, gAP: SincMatrix, fs: Double): FootEvents {
    val cwtScale = 16.0

    val aVertInt = (aVert / fs).cumsum()

    val dy = aVertInt.diffWithWavelet(cwtScale, 1 / fs)
    val dyy = dy.diffWithWavelet(cwtScale, 1 / fs)

    val gAPSmooth = lowPassAt100With2(gAP)

    val ICs = (-dy).findpeaks()

    val FCs = dyy.findpeaks()

    var isLeftIC = gAPSmooth[ICs.asIntArray()] lt 0.0

    // Detect anomaly in isLeftIC and apply pattern based correction.
    if (isLeftIC.diff().abs().sum().scalar.toInt() != (isLeftIC.numel() - 1)) {
        var candidateA = colVectorOf(1, 0).repmat(20, 1)
        var candidateB = colVectorOf(0, 1).repmat(20, 1)

        candidateA = candidateA[1..isLeftIC.numel()]
        candidateB = candidateB[1..isLeftIC.numel()]

        val errorA = (candidateA - isLeftIC).elPow(2.0).sum().sqrt().scalar
        val errorB = (candidateB - isLeftIC).elPow(2.0).sum().sqrt().scalar

        isLeftIC = if (errorA < errorB) {
            candidateA et 1.0
        } else {
            candidateB et 1.0
        }
    }

    val leftICs = ICs.getWithLV(isLeftIC)
    val rightICs = ICs.getWithLV(!isLeftIC)

    return FootEvents(ICs, isLeftIC, FCs)
}


internal data class FootEvents(val ICs: SincMatrix, val isLeftIC: SincMatrix, val FCs: SincMatrix)

