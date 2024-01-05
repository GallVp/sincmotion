package io.github.gallvp.sincmotion

import kotlin.test.Test

class SincMotionTest {
    private val testTol = 1.0E-10

    @Test
    fun runAllTests() {
        ExampleData().evaluateAllExamples(testTol)
    }
}
