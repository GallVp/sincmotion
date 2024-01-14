package io.github.gallvp.sincmotion

import io.github.gallvp.sincmaths.SincMatrix
import io.github.gallvp.sincmaths.csvRead
import io.github.gallvp.sincmaths.get
import io.github.gallvp.sincmaths.getCol
import io.github.gallvp.sincmaths.getCols
import io.github.gallvp.sincmaths.minus
import io.github.gallvp.sincmaths.plus
import io.github.gallvp.sincmotion.gaitandbalance.GnBGaitOutcomes
import io.github.gallvp.sincmotion.gaitandbalance.GnBStaticOutcomes

class GnBValidationData(val validationSkipPercentage: Double) {
    fun validate(testTol: Double) {
        for (testCase in this.validationCases) {
            testCase.evaluateOutcomes(testTol)
        }
    }

    private val GnBValidationData.validationCases: Sequence<GnBTestCase>
        get() =
            generateSequence {
                if (!gaitOutcomesCSVRows.hasNext()) {
                    return@generateSequence null
                }
                val r = gaitOutcomesCSVRows.next()
                val appFileName = r.createAppFileName()
                val appData = loadAppDataFile("validation_data/$dataPath/app_data/$appFileName")
                makeTestCaseFrom(appFileName, appData, r)
            } +
                generateSequence {
                    if (!staticOutcomesCSVRows.hasNext()) {
                        return@generateSequence null
                    }
                    val r = staticOutcomesCSVRows.next()
                    val appFileName = r.createAppFileName()
                    val appData = loadAppDataFile("validation_data/$dataPath/app_data/$appFileName")
                    makeTestCaseFrom(appFileName, appData, r)
                }

    private val staticOutcomesCSVRows: Iterator<StaticOutcomesCSVRow> by lazy {
        loadStaticOutcomesCSVRows().asSequence().constrainOnce().iterator()
    }

    private val gaitOutcomesCSVRows: Iterator<GaitOutcomesCSVRow> by lazy {
        loadGaitOutcomesCSVRows().asSequence().constrainOnce().iterator()
    }

    private fun loadAppDataFile(fileName: String) =
        SincMatrix.csvRead(
            filePath = fileName,
            separator = ",",
            headerInfo = listOf("t", "d", "d", "d", "d", "d", "d", "d", "d", "d", "d"),
        )

    companion object {
        private const val FS: Double = 100.0

        fun makeTestCaseFrom(
            name: String,
            appData: SincMatrix,
            r: GaitOutcomesCSVRow,
        ) = GnBTestCase(
            name,
            (appData.getCol(1) - appData[1, 1]) + 1.0 / GnBValidationData.FS,
            appData.getCols(intArrayOf(2, 3, 4)),
            appData.getCols(intArrayOf(5, 6, 7)),
            appData.getCols(intArrayOf(8, 9, 10, 11)),
            FS,
            r.partHeightMeter,
            Pair(
                null,
                GnBGaitOutcomes(
                    r.meanSymIndex,
                    r.meanStepLength,
                    r.meanStepTime,
                    r.stepLengthVariability,
                    r.stepTimeVariability,
                    r.stepLengthAsymmetry,
                    r.stepTimeAsymmetry,
                    r.meanStepVelocity,
                ),
            ),
        )

        fun makeTestCaseFrom(
            name: String,
            appData: SincMatrix,
            r: StaticOutcomesCSVRow,
        ) = GnBTestCase(
            name,
            (appData.getCol(1) - appData[1, 1]) + 1.0 / GnBValidationData.FS,
            appData.getCols(intArrayOf(2, 3, 4)),
            appData.getCols(intArrayOf(5, 6, 7)),
            appData.getCols(intArrayOf(8, 9, 10, 11)),
            GnBValidationData.FS,
            null,
            Pair(GnBStaticOutcomes(r.stabilityR, r.stabilityML, r.stabilityAP), null),
        )
    }
}

expect fun GnBValidationData.loadStaticOutcomesCSVRows(): List<StaticOutcomesCSVRow>

expect fun GnBValidationData.loadGaitOutcomesCSVRows(): List<GaitOutcomesCSVRow>

expect val GnBValidationData.Companion.dataPath: String

data class StaticOutcomesCSVRow(
    val part: Int,
    val test: Int,
    val day: Int,
    val month: String,
    val year: Int,
    val task: String,
    val qualifier: String,
    val stabilityR: Double,
    val stabilityML: Double,
    val stabilityAP: Double,
)

expect fun StaticOutcomesCSVRow.createAppFileName(): String

data class GaitOutcomesCSVRow(
    val part: Int,
    val partHeightMeter: Double,
    val test: Int,
    val day: Int,
    val month: String,
    val year: Int,
    val task: String,
    val qualifier: String,
    val meanSymIndex: Double,
    val meanStepLength: Double,
    val meanStepTime: Double,
    val stepLengthVariability: Double,
    val stepTimeVariability: Double,
    val stepLengthAsymmetry: Double,
    val stepTimeAsymmetry: Double,
    val meanStepVelocity: Double,
)

expect fun GaitOutcomesCSVRow.createAppFileName(): String
