import SincMotionTests.Companion.testTol

expect class ExampleData() {
    val exampleNames: List<String>
    fun getExampleByName(name: String): ExampleDatum
}

fun ExampleData.evaluateAllExamples() {
    exampleNames.forEach {
        println("Running tests for file: $it")
        SincMotionTests.assert(getExampleByName(it).evaluateOutcomes(testTol))
        println("All tests passed on file: $it")
    }
}