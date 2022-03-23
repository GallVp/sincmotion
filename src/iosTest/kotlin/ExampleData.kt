import sincmaths.*
import sincmotion.BalanceParameters
import sincmotion.GaitParameters

actual class ExampleData {
    actual val exampleNames = listOf(
        "walk_hf",
        "firm_surface_eyes_closed"
    )
    private val heightForExamples = listOf(
        1.64,
        1.54
    )
    private val isGaitTask = listOf(
        true,
        false
    )
    private val referenceOutcomes = listOf(
        Pair(
            null, GaitParameters(
                meanSymIndex = 68.7643646554932531,
                meanStepLength = 0.6380031475203751,
                meanStepTime = 0.5700000000000000,
                stepLengthVariability = 2.5090538539828726,
                stepTimeVariability = 2.6790809783090652,
                stepLengthAsymmetry = 2.4516609807538421,
                stepTimeAsymmetry = 1.7543859649122824,
                meanStepVelocity = 1.1146040171704668
            )
        ),
        Pair(
            BalanceParameters(
                maaROnNegLog = 3.5146148603432237,
                maaMLOnNegLog = 4.1171672095899510,
                maaAPOnNegLog = 4.0994912455579362,
            ), null
        )
    )
    private val fs: Double = 100.0

    actual fun getExampleByName(name: String): ExampleDatum {
        val filePath = "Example Data/$name.csv"
        val dataMatrix = SincMatrix.csvread(
            filePath = filePath,
            separator = ",",
            headerInfo = listOf("t", "d", "d", "d", "d", "d", "d", "d", "d", "d", "d")
        )

        return ExampleDatum(
            dataMatrix.getCol(1),
            dataMatrix.getCols(intArrayOf(2, 3, 4)),
            dataMatrix.getCols(intArrayOf(5, 6, 7)),
            dataMatrix.getCols(intArrayOf(8, 9, 10, 11)),
            fs,
            heightForExamples[exampleNames.indexOf(name)],
            isGaitTask[exampleNames.indexOf(name)],
            referenceOutcomes[exampleNames.indexOf(name)]
        )
    }
}