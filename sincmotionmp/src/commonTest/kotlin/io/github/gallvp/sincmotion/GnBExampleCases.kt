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

class GnBExampleCases() {
    fun evaluateAll(testTol: Double) {
        exampleNames.forEach {
            getExampleByName(it).evaluateOutcomes(testTol)
        }
    }

    private fun getExampleByName(name: String): GnBTestCase {
        val filePath = "example_data/gaitandbalance/$name.csv"
        val dataMatrix =
            SincMatrix.csvRead(
                filePath = filePath,
                separator = ",",
                headerInfo = listOf("t", "d", "d", "d", "d", "d", "d", "d", "d", "d", "d"),
            )

        return GnBTestCase(
            name,
            (dataMatrix.getCol(1) - dataMatrix[1, 1]) + 1.0 / FS,
            dataMatrix.getCols(intArrayOf(2, 3, 4)),
            dataMatrix.getCols(intArrayOf(5, 6, 7)),
            dataMatrix.getCols(intArrayOf(8, 9, 10, 11)),
            FS,
            exampleParticipantHeight[exampleNames.indexOf(name)],
            referenceOutcomes[exampleNames.indexOf(name)],
        )
    }

    companion object {
        const val FS: Double = 100.0
    }
}

expect val GnBExampleCases.exampleNames: List<String>
expect val GnBExampleCases.exampleParticipantHeight: List<Double>
expect val GnBExampleCases.referenceOutcomes: List<Pair<GnBStaticOutcomes?, GnBGaitOutcomes?>>
