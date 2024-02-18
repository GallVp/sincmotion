package io.github.gallvp.sincmotion

import kotlin.test.Test

class SincMotionTest {
    private val testTol = 1.0E-10
    private val validationSkipPercentage: Double = 95.0

    @Test
    fun evaluateAlgorithms() {
        GnBExampleCases().evaluateAll(testTol)
        GnBValidationData(validationSkipPercentage).validate(testTol)
    }

    @Test
    fun evaluateGnBOutcomeViewModel() {
        GnBOutcomeViewModelTests().evaluateNormativeModels()
    }
}
