import sincmaths.SincMatrix
import sincmaths.asSincMatrix
import sincmaths.sincmatrix.all
import sincmaths.sincmatrix.lt
import sincmaths.sincmatrix.minus
import sincmotion.BalanceParameters
import sincmotion.GaitParameters
import sincmotion.SincMotionProcessor

data class ExampleDatum(
    val timeVector: SincMatrix,
    val accelData: SincMatrix,
    val gyroData: SincMatrix,
    val rotData: SincMatrix,
    val fs: Double,
    val personHeight: Double,
    val isGaitTask: Boolean,
    val referenceOutcomes: Pair<BalanceParameters?, GaitParameters?>
) {
    fun evaluateOutcomes(testTol: Double, printOutcomes:Boolean = false) = if (isGaitTask) {
        val testOutcomes = SincMotionProcessor().computeGaitParameters(
            timeVector.asRowMajorArray(),
            accelData.asRowMajorArray(),
            rotData.asRowMajorArray(),
            gyroData.asRowMajorArray(),
            fs,
            personHeight
        )
        if(printOutcomes) {
            println("Reference outcomes are:\n${referenceOutcomes.second}")
            println("Test outcomes are:\n$testOutcomes")
        }
        (testOutcomes!!.array.asSincMatrix() - referenceOutcomes.second!!.array.asSincMatrix()).abs().lt(testTol).all()
    } else {
        val testOutcomes = SincMotionProcessor().computeBalanceParameters(
            accelData.asRowMajorArray(),
            rotData.asRowMajorArray(),
            fs
        )
        if(printOutcomes) {
            println("Reference outcomes are:\n${referenceOutcomes.first}")
            println("Test outcomes are:\n$testOutcomes")
        }
        (testOutcomes!!.array.asSincMatrix() - referenceOutcomes.first!!.array.asSincMatrix()).abs().lt(testTol).all()
    }
}