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
                sem = 1.0,
                semAsString = "1",
                normativeRange = listOf(65.0, 74.0),
                normativeRangeAsString = "≥ 66",
                mdc = 3.0,
                mdcAsString = "3"
            )
        )

        assertEquals(
            getReportableNormativeScore(5, "slv", 17.0, 100.0, 150.099),
            ReportableNormativeScore(
                normativeLowerBound = 1.9,
                normativeLowerBoundAsString = "1.9",
                sem = 0.8,
                semAsString = "0.8",
                normativeRange = listOf(1.6, 5.4),
                normativeRangeAsString = "[1.6, 5.4]",
                mdc = 2.3,
                mdcAsString = "2.3"
            )
        )

        assertEquals(
            getReportableNormativeScore(0, "maa-ap", 85.0, 50.0, 190.0),
            ReportableNormativeScore(
                normativeLowerBound = 3.7,
                normativeLowerBoundAsString = "3.7",
                sem = 0.1,
                semAsString = "0.1",
                normativeRange = listOf(3.6, 4.4),
                normativeRangeAsString = "≥ 3.7",
                mdc = 0.3,
                mdcAsString = "0.3"
            )
        )

        assertEquals(
            getReportableNormativeScore(3, "maa-ap", 30.0, 65.0, 170.18),
            ReportableNormativeScore(
                normativeLowerBound = 3.1,
                normativeLowerBoundAsString = "3.1",
                sem = 0.1,
                semAsString = "0.1",
                normativeRange = listOf(3.0, 4.0),
                normativeRangeAsString = "≥ 3.1",
                mdc = 0.4,
                mdcAsString = "0.4"
            )
        )

        assertEquals(
            getReportableNormativeScore(4, "sta", 30.0, 65.0, 170.18),
            ReportableNormativeScore(
                normativeLowerBound = -1.0,
                normativeLowerBoundAsString = "-1",
                sem = 1.0,
                semAsString = "1",
                normativeRange = listOf(-2.0, 7.0),
                normativeRangeAsString = "[-2, 7]",
                mdc = 4.0,
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