import sincmotion.NormativeDatabase
import sincmotion.NormativeScore
import kotlin.test.assertEquals

class NormativeModelTests {
    fun evaluateNormativeModels() {
        assertEquals(
            getNormativeScore(5, 0, 32.0, 79.0, 180.34),
            NormativeScore(
                normativeLowerBound = 66.0,
                semAsString = "1",
                normativeRangeAsString = listOf("65", "74"),
                mdcAsString = "3"
            )
        )

        assertEquals(
            getNormativeScore(5, 3, 17.0, 100.0, 150.099),
            NormativeScore(
                normativeLowerBound = 1.9,
                semAsString = "0.8",
                normativeRangeAsString = listOf("1.6", "5.4"),
                mdcAsString = "2.3"
            )
        )

        assertEquals(
            getNormativeScore(0, 2, 85.0, 50.0, 190.0),
            NormativeScore(
                normativeLowerBound = 3.7,
                semAsString = "0.1",
                normativeRangeAsString = listOf("3.6", "4.4"),
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