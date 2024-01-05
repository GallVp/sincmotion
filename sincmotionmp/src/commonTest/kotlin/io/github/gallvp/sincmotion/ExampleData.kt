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

class ExampleData() {
    fun evaluateAllExamples(testTol: Double) {
        exampleNames.forEach {
            println("Running tests for file: $it")
            getExampleByName(it).evaluateOutcomes(testTol)
            println("All tests passed on file: $it")
        }
    }

    private fun getExampleByName(name: String): ExampleDatum {
        val filePath = "example_data/gaitandbalance/$name.csv"
        val dataMatrix =
            SincMatrix.csvRead(
                filePath = filePath,
                separator = ",",
                headerInfo = listOf("t", "d", "d", "d", "d", "d", "d", "d", "d", "d", "d"),
            )

        return ExampleDatum(
            (dataMatrix.getCol(1) - dataMatrix[1, 1]) + 1.0 / FS,
            dataMatrix.getCols(intArrayOf(2, 3, 4)),
            dataMatrix.getCols(intArrayOf(5, 6, 7)),
            dataMatrix.getCols(intArrayOf(8, 9, 10, 11)),
            FS,
            exampleParticipantHeight[exampleNames.indexOf(name)],
            isGaitTask[exampleNames.indexOf(name)],
            referenceOutcomes[exampleNames.indexOf(name)],
        )
    }

    companion object {
        const val FS: Double = 100.0
    }
}

expect val ExampleData.exampleNames: List<String>
expect val ExampleData.exampleParticipantHeight: List<Double>
expect val ExampleData.isGaitTask: List<Boolean>
expect val ExampleData.referenceOutcomes: List<Pair<GnBStaticOutcomes?, GnBGaitOutcomes?>>
