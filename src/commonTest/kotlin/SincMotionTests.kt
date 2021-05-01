import sincmaths.SincMatrix
import sincmaths.sincmatrix.boolean
import kotlin.test.Test

class SincMotionTests {
    @Test
    fun runAllTests() {
        ExampleData().evaluateAllExamples()
    }

    companion object {
        fun assert(condition: Boolean) {
            if (!condition) {
                throw Exception()
            }
        }

        fun assert(condition: SincMatrix) {
            if (!condition.boolean) {
                throw Exception()
            }
        }

        const val testTol = 1.0E-12
    }
}