import sincmotion.NormativeDatabase
import sincmotion.ReportableNormativeScore
import kotlin.test.assertEquals

class NormativeModelTests {
    fun evaluateNormativeModels() {
        assertEquals(
            ReportableNormativeScore(
                normativeLowerBound = 63.000000,
                normativeLowerBoundAsString = "63",
                normativeUpperBound = 79.000000,
                normativeUpperBoundAsString = "79",
                normativeRange = listOf(62.000000, 80.000000),
                normativeRangeAsString = "≥ 63",
                mdc = 6.000000,
                mdcAsString = "6"
            ),
            getReportableNormativeScore(5, "sym", 32.0, 79.0, 180.34)
        )

        assertEquals(
            ReportableNormativeScore(
                normativeLowerBound = -1.000000,
                normativeLowerBoundAsString = "-1",
                normativeUpperBound = 5.000000,
                normativeUpperBoundAsString = "5",
                normativeRange = listOf(-1.000000, 6.000000),
                normativeRangeAsString = "≤ 5",
                mdc = 4.000000,
                mdcAsString = "4"
            ),
            getReportableNormativeScore(5, "slv", 17.0, 100.0, 150.099)
        )

        assertEquals(
            ReportableNormativeScore(
                normativeLowerBound = 3.500000,
                normativeLowerBoundAsString = "3.5",
                normativeUpperBound = 4.200000,
                normativeUpperBoundAsString = "4.2",
                normativeRange = listOf(3.500000, 4.200000),
                normativeRangeAsString = "≥ 3.5",
                mdc = 0.300000,
                mdcAsString = "0.3"
            ),
            getReportableNormativeScore(0, "maa-ap", 85.0, 50.0, 190.0)
        )

        assertEquals(
            ReportableNormativeScore(
                normativeLowerBound = 3.100000,
                normativeLowerBoundAsString = "3.1",
                normativeUpperBound = 4.100000,
                normativeUpperBoundAsString = "4.1",
                normativeRange = listOf(3.000000, 4.200000),
                normativeRangeAsString = "≥ 3.1",
                mdc = 0.400000,
                mdcAsString = "0.4"
            ),
            getReportableNormativeScore(3, "maa-ap", 30.0, 65.0, 170.18)
        )

        assertEquals(
            ReportableNormativeScore(
                normativeLowerBound = -2.000000,
                normativeLowerBoundAsString = "-2",
                normativeUpperBound = 8.000000,
                normativeUpperBoundAsString = "8",
                normativeRange = listOf(-3.000000, 9.000000),
                normativeRangeAsString = "≤ 8",
                mdc = 5.000000,
                mdcAsString = "5"
            ),
            getReportableNormativeScore(4, "sta", 30.0, 65.0, 170.18)
        )
    }

    private fun getReportableNormativeScore(
        taskQualifier: Int,
        outcomeKey: String,
        ageInYears: Double,
        massInKGs: Double,
        heightInCM: Double
    ) = NormativeDatabase(ageInYears, massInKGs, heightInCM).getNormativeScore(taskQualifier, outcomeKey)
}