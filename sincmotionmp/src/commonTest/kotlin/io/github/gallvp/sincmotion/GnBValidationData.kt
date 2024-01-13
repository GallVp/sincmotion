package io.github.gallvp.sincmotion

import io.github.gallvp.sincmaths.SincMatrix
import io.github.gallvp.sincmaths.csvRead

class GnBValidationData(val validationSkipPercentage: Double) {
    val staticOutcomesCSVRows: Iterator<StaticOutcomesCSVRow> by lazy {
        loadStaticOutcomesCSVRows().asSequence().constrainOnce().iterator()
    }

    val gaitOutcomesCSVRows: Iterator<GaitOutcomesCSVRow> by lazy {
        loadGaitOutcomesCSVRows().asSequence().constrainOnce().iterator()
    }

    fun validate(testTol: Double) {
        for (testCase in this.validationCases) {
            testCase.evaluateOutcomes(testTol)
        }
    }

    fun loadAppDataFile(fileName: String) =
        SincMatrix.csvRead(
            filePath = fileName,
            separator = ",",
            headerInfo = listOf("t", "d", "d", "d", "d", "d", "d", "d", "d", "d", "d"),
        )

    companion object {
        const val FS: Double = 100.0
    }
}

expect val GnBValidationData.validationCases: Sequence<GnBTestCase>

expect class StaticOutcomesCSVRow

expect class GaitOutcomesCSVRow

expect fun GnBValidationData.loadStaticOutcomesCSVRows(): List<StaticOutcomesCSVRow>

expect fun GnBValidationData.loadGaitOutcomesCSVRows(): List<GaitOutcomesCSVRow>
