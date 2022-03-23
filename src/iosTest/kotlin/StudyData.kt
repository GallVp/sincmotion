import platform.Foundation.NSBundle
import sincmaths.*
import sincmotion.BalanceParameters
import sincmotion.GaitParameters

actual class StudyData {
    actual val studyNames = listOf("SAVS")
    private val fs: Double = 100.0

    actual fun validateStudyFile(forStudy: String, fileName: String, testTol: Double) {
        val filePath = "$forStudy/$fileName"
        val studyOutcomes = getOutcomeMatrices(forStudy)

        validateStudyFile(forStudy, filePath, testTol, studyOutcomes)
    }

    actual fun validateStudyData(forStudy: String, testTol: Double, sampleSize: Int) {
        val filePaths = getStudyFilePaths(forStudy)
        val studyOutcomes = getOutcomeMatrices(forStudy)

        val sample: List<Int>
        val selectedFilePaths = if (sampleSize != 0) {
            sample = (filePaths.indices).shuffled().take(sampleSize)
            filePaths.slice(sample)
        } else {
            sample = filePaths.indices.toList()
            filePaths
        }
        selectedFilePaths.mapIndexed { index: Int, path: String ->
            println("Index: ${sample[index]}; File: ${path.split("/").last()}")
            validateStudyFile(forStudy, path, testTol, studyOutcomes)
        }
    }

    private fun validateStudyFile(
        forStudy: String,
        filePath: String,
        testTol: Double,
        studyOutcomes: Pair<SincMatrix, SincMatrix>
    ) {
        val (fileName, fileParameters) = extractFileParameters(filePath)
        val fileMatrix = getFileMatrixByName(forStudy, fileName)
        val (referenceOutcomes, personHeight) = getReferenceOutcomes(studyOutcomes, fileParameters)
        val exampleDatum = ExampleDatum(
            fileMatrix.getCol(1),
            fileMatrix.getCols(intArrayOf(2, 3, 4)),
            fileMatrix.getCols(intArrayOf(5, 6, 7)),
            fileMatrix.getCols(intArrayOf(8, 9, 10, 11)),
            fs,
            personHeight,
            fileParameters.isGaitTask,
            referenceOutcomes
        )
        exampleDatum.evaluateOutcomes(testTol)
    }

    private fun getFileMatrixByName(studyName: String, fileName: String): SincMatrix {
        val filePath = "$studyName/$fileName.csv"
        return SincMatrix.csvread(
            filePath = filePath,
            separator = ",",
            headerInfo = listOf("t", "d", "d", "d", "d", "d", "d", "d", "d", "d", "d")
        )
    }

    private fun getOutcomeMatrices(studyName: String): Pair<SincMatrix, SincMatrix> {
        val forStatic = "$studyName/Outcomes/AppValidationData.csv"
        val forGait = "$studyName/Outcomes/AppValidationGaitData.csv"

        val staticOutcomes = SincMatrix.csvread(
            filePath = forStatic,
            separator = ",",
            headerInfo = listOf("d", "d", "d", "d", "d", "d", "d", "d")
        )

        val gaitOutcomes = SincMatrix.csvread(
            filePath = forGait,
            separator = ",",
            headerInfo = listOf("d", "d", "d", "d", "d", "d", "d", "d", "d", "d", "d", "d")
        )

        return Pair(staticOutcomes, gaitOutcomes)
    }

    private fun getStudyFilePaths(studyName: String): List<String> =
        NSBundle.mainBundle.pathsForResourcesOfType(".csv", studyName) as List<String>


    private fun extractFileParameters(filePath: String): Pair<String, StudyFileParameters> {
        val fileName = filePath.split("/").last().split(".").first()
        val fileGroups = filePattern.matchEntire(fileName)!!.groups
        return Pair(
            fileName, StudyFileParameters(
                partID = fileGroups[1]!!.value,
                testNum = fileGroups[2]!!.value.toInt(),
                testType = fileGroups[6]!!.value,
                testQualifier = fileGroups[7]!!.value
            )
        )
    }

    private fun getReferenceOutcomes(
        studyOutcomes: Pair<SincMatrix, SincMatrix>,
        fileParameters: StudyFileParameters
    ): Pair<Pair<BalanceParameters?, GaitParameters?>, Double> = if (fileParameters.isGaitTask) {
        val out = studyOutcomes.second
        val rowSelector = (out.getCol(1) et fileParameters.partID.toDouble()) and
                (out.getCol(2) et fileParameters.testQualifierInt.toDouble()) and
                (out.getCol(3) et fileParameters.testNum.toDouble())
        val matrixRow = out.getRow(rowSelector.find().scalar.toInt())
        Pair(
            Pair(
                null, GaitParameters(
                    matrixRow[4],
                    matrixRow[5],
                    matrixRow[6],
                    matrixRow[7],
                    matrixRow[8],
                    matrixRow[9],
                    matrixRow[10],
                    matrixRow[11],
                )
            ), matrixRow[12]
        )
    } else {
        val out = studyOutcomes.first
        val rowSelector = (out.getCol(1) et fileParameters.partID.toDouble()) and
                (out.getCol(2) et fileParameters.testTypeInt.toDouble()) and
                (out.getCol(3) et fileParameters.testQualifierInt.toDouble()) and
                (out.getCol(4) et fileParameters.testNum.toDouble())
        val matrixRow = out.getRow(rowSelector.find().scalar.toInt())
        Pair(
            Pair(
                BalanceParameters(
                    matrixRow[7],
                    matrixRow[5],
                    matrixRow[6]
                ), null
            ), matrixRow[8]
        )
    }

    private val filePattern =
        Regex("S([0-9]+) Test set ([1-3]+) on ([0-9]+) ([a-zA-Z]+) ([0-9]+) ([a-zA-Z]+) ([a-zA-Z]+)")
}