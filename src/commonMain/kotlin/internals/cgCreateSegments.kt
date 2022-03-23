package sincmotion.internals

import sincmaths.*

internal fun cgCreateSegments(
    timeVectorIn: SincMatrix,
    accelDataIn: SincMatrix,
    rotDataIn: SincMatrix,
    gyroDataIn: SincMatrix,
    fs: Double,
    isAndroid: Boolean
): CGSegments {

    val timeVector: SincMatrix
    val accelData: SincMatrix
    val rotData: SincMatrix
    val gyroData: SincMatrix

    if (isAndroid) {
        val selectedIndices = fs.toInt() + 1..timeVectorIn.numel()
        timeVector = timeVectorIn[selectedIndices]
        accelData = accelDataIn.getRows(selectedIndices)
        rotData = rotDataIn.getRows(selectedIndices)
        gyroData = gyroDataIn.getRows(selectedIndices)
    } else {
        timeVector = timeVectorIn
        accelData = accelDataIn
        rotData = rotDataIn
        gyroData = gyroDataIn
    }

    val dataPauseStarts = timeVector.diff().gt(1.0).find()
    val accelDataSegments = listOf(
        accelData.getRows(1..dataPauseStarts[1].toInt()),
        accelData.getRows(dataPauseStarts[1].toInt() + 1..dataPauseStarts[2].toInt()), // +1 to go to first sample of the lap
        accelData.getRows(dataPauseStarts[2].toInt() + 1..dataPauseStarts[3].toInt()),
        accelData.getRows(dataPauseStarts[3].toInt() + 1..timeVector.numel())
    )
    val rotDataSegments = listOf(
        rotData.getRows(1..dataPauseStarts[1].toInt()),
        rotData.getRows(dataPauseStarts[1].toInt() + 1..dataPauseStarts[2].toInt()),
        rotData.getRows(dataPauseStarts[2].toInt() + 1..dataPauseStarts[3].toInt()),
        rotData.getRows(dataPauseStarts[3].toInt() + 1..timeVector.numel())
    )
    val gyroDataSegments = listOf(
        gyroData.getRows(1..dataPauseStarts[1].toInt()),
        gyroData.getRows(dataPauseStarts[1].toInt() + 1..dataPauseStarts[2].toInt()),
        gyroData.getRows(dataPauseStarts[2].toInt() + 1..dataPauseStarts[3].toInt()),
        gyroData.getRows(dataPauseStarts[3].toInt() + 1..timeVector.numel())
    )

    return CGSegments(accelDataSegments, rotDataSegments, gyroDataSegments)
}


internal data class CGSegments(
    val accelDataSegments: List<SincMatrix>,
    val rotDataSegments: List<SincMatrix>,
    val gyroDataSegments: List<SincMatrix>
)

