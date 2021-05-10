import sincmotion.NormativeDatabase
import sincmotion.NormativeScore
import kotlin.test.assertEquals

class NormativeModelTests {
    fun evaluateNormativeModels() {
        assertEquals(
            getNormativeScore(5, 0, 32.0, 79.0, 180.34),
            NormativeScore(
                normativeLowerBound = 66.0,
                normativeLowerBoundAsString = "66",
                sem = 1.0,
                semAsString = "1",
                normativeRange = listOf(65.0, 74.0),
                normativeRangeAsString = listOf("65", "74"),
                mdc = 3.0,
                mdcAsString = "3"
            )
        )

        assertEquals(
            getNormativeScore(5, 3, 17.0, 100.0, 150.099),
            NormativeScore(
                normativeLowerBound = 1.9,
                normativeLowerBoundAsString = "1.9",
                sem = 0.8,
                semAsString = "0.8",
                normativeRange = listOf(1.6, 5.4),
                normativeRangeAsString = listOf("1.6", "5.4"),
                mdc = 2.3,
                mdcAsString = "2.3"
            )
        )

        assertEquals(
            getNormativeScore(0, 2, 85.0, 50.0, 190.0),
            NormativeScore(
                normativeLowerBound = 3.7,
                normativeLowerBoundAsString = "3.7",
                sem = 0.1,
                semAsString = "0.1",
                normativeRange = listOf(3.6, 4.4),
                normativeRangeAsString = listOf("3.6", "4.4"),
                mdc = 0.3,
                mdcAsString = "0.3"
            )
        )
    }

    private fun getNormativeScore(
        taskQualifier: Int,
        outcome: Int,
        ageInYears: Double,
        massInKGs: Double,
        heightInCM: Double
    ) = NormativeDatabase(ageInYears, massInKGs, heightInCM).getNormativeScore(taskQualifier, outcome)
}