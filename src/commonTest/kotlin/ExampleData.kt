import SincMotionTests.Companion.testTol

expect class ExampleData() {
    val exampleNames: List<String>
    fun getExampleByName(name: String): ExampleDatum
}

fun ExampleData.evaluateAllExamples() {
    exampleNames.forEach {
        SincMotionTests.assert(getExampleByName(it).evaluateOutcomes(testTol))
    }
}