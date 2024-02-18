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

data class GnBTestCase(
    val name: String,
    val timeVector: SincMatrix,
    val accelData: SincMatrix,
    val gyroData: SincMatrix,
    val rotData: SincMatrix,
    val fs: Double,
    val personHeight: Double?,
    val referenceOutcomes: Pair<GnBStaticOutcomes?, GnBGaitOutcomes?>,
) {
    fun evaluateOutcomes(testTol: Double) {
        println("Running tests for: $name")

        val outcomeDifferences: SincMatrix
        val testOutcomes: Any
        if (referenceOutcomes.second != null) {
            testOutcomes =
                estimateGnBGaitOutcomes(
                    timeVector,
                    accelData,
                    rotData,
                    gyroData,
                    fs,
                    personHeight!!,
                )
            outcomeDifferences =
                (testOutcomes.matrix - referenceOutcomes.second!!.matrix).abs()
        } else {
            testOutcomes =
                estimateGnBStaticOutcomes(
                    accelData,
                    rotData,
                    fs,
                )
            outcomeDifferences =
                (testOutcomes.matrix - referenceOutcomes.first!!.matrix).abs()
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

        println("All tests passed for: $name")
    }

    private val Pair<GnBStaticOutcomes?, GnBGaitOutcomes?>.theOne: Any
        get() =
            if (this.first != null) {
                this.first!!
            } else {
                this.second!!
            }

    private val GnBGaitOutcomes.matrix: SincMatrix
        get() =
            doubleArrayOf(
                this.meanSymIndex,
                this.meanStepLength,
                this.meanStepTime,
                this.stepLengthVariability,
                this.stepTimeVariability,
                this.stepLengthAsymmetry,
                this.stepTimeAsymmetry,
                this.meanStepVelocity,
            ).asSincMatrix()

    private val GnBStaticOutcomes.matrix: SincMatrix
        get() =
            doubleArrayOf(
                this.stabilityR,
                this.stabilityML,
                this.stabilityAP,
            ).asSincMatrix()
}
