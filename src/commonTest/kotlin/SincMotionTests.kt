import kotlin.test.Test

class SincMotionTests {

    private val testTol = 1.0E-10

    @Test
    fun runAllTests() {
        ExampleData().evaluateAllExamples(testTol)
        StudyData().validateAllStudies(testTol, 5)

        NormativeModelTests().evaluateNormativeModels()
    }
}