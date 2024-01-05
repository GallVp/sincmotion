package io.github.gallvp.sincmotion

import io.github.gallvp.sincmaths.SincMatrix
import io.github.gallvp.sincmaths.abs
import io.github.gallvp.sincmaths.all
import io.github.gallvp.sincmaths.asSincMatrix
import io.github.gallvp.sincmaths.lt
import io.github.gallvp.sincmaths.minus
import io.github.gallvp.sincmotion.gaitandbalance.GnBGaitOutcomes
import io.github.gallvp.sincmotion.gaitandbalance.GnBStaticOutcomes
import io.github.gallvp.sincmotion.gaitandbalance.estimateGnBGaitOutcomes
import io.github.gallvp.sincmotion.gaitandbalance.estimateGnBStaticOutcomes
import kotlin.test.assertTrue

data class ExampleDatum(
    val timeVector: SincMatrix,
    val accelData: SincMatrix,
    val gyroData: SincMatrix,
    val rotData: SincMatrix,
    val fs: Double,
    val personHeight: Double,
    val isGaitTask: Boolean,
    val referenceOutcomes: Pair<GnBStaticOutcomes?, GnBGaitOutcomes?>,
) {
    fun evaluateOutcomes(testTol: Double) {
        val outcomeDifferences: SincMatrix
        val testOutcomes: Any
        if (isGaitTask) {
            testOutcomes =
                estimateGnBGaitOutcomes(
                    timeVector,
                    accelData,
                    rotData,
                    gyroData,
                    fs,
                    personHeight,
                )
            outcomeDifferences =
                (testOutcomes.array.asSincMatrix() - referenceOutcomes.second!!.array.asSincMatrix()).abs()
        } else {
            testOutcomes =
                estimateGnBStaticOutcomes(
                    accelData,
                    rotData,
                    fs,
                )
            outcomeDifferences =
                (testOutcomes.array.asSincMatrix() - referenceOutcomes.first!!.array.asSincMatrix()).abs()
        }

        assertTrue {
            val assertValue = outcomeDifferences.lt(testTol).all()
            if (!assertValue) {
                println("Reference outcomes are:\n${referenceOutcomes.theOne}")
                println("Test outcomes are:\n$testOutcomes")
                println("Differences are: $outcomeDifferences")
            }
            assertValue
        }
    }

    private val Pair<GnBStaticOutcomes?, GnBGaitOutcomes?>.theOne: Any
        get() =
            if (this.first != null) {
                this.first!!
            } else {
                this.second!!
            }
}
