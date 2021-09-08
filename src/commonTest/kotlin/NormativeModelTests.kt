import sincmotion.NormativeDatabase
import sincmotion.ReportableNormativeScore
import kotlin.test.assertEquals

class NormativeModelTests {
    fun evaluateNormativeModels() {
        assertEquals(
            getReportableNormativeScore(5, "sym", 32.0, 79.0, 180.34),
            ReportableNormativeScore(
                normativeLowerBound = 66.0,
                normativeLowerBoundAsString = "66",
                normativeUpperBound = 73.0,
                normativeUpperBoundAsString = "73",
                normativeRange = listOf(65.0, 74.0),
                normativeRangeAsString = "≥ 66",
                mdc = 3.0,
                mdcAsString = "3"
            )
        )

        assertEquals(
            getReportableNormativeScore(5, "slv", 17.0, 100.0, 150.099),
            ReportableNormativeScore(
                normativeLowerBound = 1.900000,
                normativeLowerBoundAsString = "1.9",
                normativeUpperBound = 5.100000,
                normativeUpperBoundAsString = "5.1",
                normativeRange = listOf(1.600000, 5.400000),
                normativeRangeAsString = "≤ 5.1",
                mdc = 2.300000,
                mdcAsString = "2.3"
            )
        )

        assertEquals(
            getReportableNormativeScore(0, "maa-ap", 85.0, 50.0, 190.0),
            ReportableNormativeScore(
                normativeLowerBound = 3.700000,
                normativeLowerBoundAsString = "3.7",
                normativeUpperBound = 4.400000,
                normativeUpperBoundAsString = "4.4",
                normativeRange = listOf(3.600000, 4.400000),
                normativeRangeAsString = "≥ 3.7",
                mdc = 0.300000,
                mdcAsString = "0.3"
            )
        )

        assertEquals(
            getReportableNormativeScore(3, "maa-ap", 30.0, 65.0, 170.18),
            ReportableNormativeScore(
                normativeLowerBound = 3.100000,
                normativeLowerBoundAsString = "3.1",
                normativeUpperBound = 3.900000,
                normativeUpperBoundAsString = "3.9",
                normativeRange = listOf(3.000000, 4.000000),
                normativeRangeAsString = "≥ 3.1",
                mdc = 0.400000,
                mdcAsString = "0.4"
            )
        )

        assertEquals(
            getReportableNormativeScore(4, "sta", 30.0, 65.0, 170.18),
            ReportableNormativeScore(
                normativeLowerBound = -1.000000,
                normativeLowerBoundAsString = "-1",
                normativeUpperBound = 6.000000,
                normativeUpperBoundAsString = "6",
                normativeRange = listOf(-2.000000, 7.000000),
                normativeRangeAsString = "≤ 6",
                mdc = 4.000000,
                mdcAsString = "4"
            )
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