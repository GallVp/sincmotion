import kotlin.test.assertTrue

expect class ExampleData() {
    val exampleNames: List<String>
    fun getExampleByName(name: String): ExampleDatum
}

fun ExampleData.evaluateAllExamples(testTol: Double) {
    exampleNames.forEach {
        println("Running tests for file: $it")
        assertTrue(getExampleByName(it).evaluateOutcomes(testTol))
        println("All tests passed on file: $it")
    }
}