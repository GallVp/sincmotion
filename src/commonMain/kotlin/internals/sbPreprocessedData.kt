package sincmotion.internals

import sincmaths.SincMatrix
import sincmaths.sincmatrix.*

internal fun sbPreprocessedData(
    accelDataIn: SincMatrix,
    rotDataIn: SincMatrix,
    fs: Double,
    isAndroid: Boolean
): SincMatrix {
    // Convert acceleration units to m/sec/sec
    var accelDataSI = accelDataIn * 9.8

    // If Android: discard first second of data to avoid Gyro transients
    val rotData: SincMatrix
    if (isAndroid) {
        accelDataSI = accelDataSI.get { endR, _, _, allC ->
            Pair(fs.toInt() + 1..endR, allC)
        }
        rotData = rotDataIn.get { endR, _, _, allC ->
            Pair(fs.toInt() + 1..endR, allC)
        }
    } else {
        rotData = rotDataIn
    }

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

    // Filter Data
    val fsInt = fs.toInt()
    val fsInt10 = 10 * fsInt
    val paddedData = accelDataRotated.getRows(fsInt..fsInt10).flip().cat(
        1,
        accelDataRotated,
        accelDataRotated.getRows(accelDataRotated.numRows() - fsInt10 + 1..accelDataRotated.numRows()).flip()
    )

    val pFilteredData = bandPassAt100From0d3To45(paddedData)
    val filteredData = pFilteredData.get { endR, _, _, allC ->
        Pair(fsInt10 + 1..endR - fsInt10, allC)
    }

    return filteredData
}
