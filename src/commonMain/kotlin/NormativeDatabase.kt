package sincmotion

import sincmotion.internals.NormativeModel
import sincmotion.internals.NormativeModels
import kotlin.math.pow
import kotlin.math.round

data class NormativeDatabase(val ageInYears: Double, val massInKGs: Double, val heightInCM: Double) {

    /**
     * Table (Task:Qualifier = Int):
     *
     * FirmEO = 0
     *
     * FirmEC = 1
     *
     * ComplaintEO = 2
     *
     * ComplaintEC = 3
     *
     * WalkHS = 4
     *
     * WalkHT = 5
     *
     *
     * Table (Outcome = Int)
     *
     * MAA_R = 0
     *
     * MAA_ML = 1
     *
     * MAA_AP = 2
     *
     * Table (Outcome = Int);Walk
     *
     * SymIndex = 0
     *
     * StepLength = 1
     *
     * StepTime = 2
     *
     * StepLengthVariability = 3
     *
     * StepTimeVariability = 4
     *
     * StepLengthAsymmetry = 5
     *
     * StepTimeAsymmetry = 6
     *
     * StepVelocity = 7
     */
    fun getNormativeScore(taskQualifier: Int, outcome: Int): NormativeScore = database[taskQualifier][outcome]

    private val database = NormativeModels.modelList.map { taskQualifiers ->
        taskQualifiers.map { model ->
            model2Score(model)
        }
    }

    private fun model2Score(
        model: NormativeModel
    ): NormativeScore {
        val bmi = massInKGs / (heightInCM / 2.0).pow(2)

        val normativeMean = model.intercept +
                model.ageInYearsBeta * ageInYears +
                model.bmiBeta * bmi +
                model.heightInCMBeta * heightInCM

        val normativeRange = doubleArrayOf(
            normativeMean - 1.96 * model.normativeSD,
            normativeMean + 1.96 * model.normativeSD
        )
        val normativeLowerBound = normativeMean - 1.645 * model.normativeSD

        val semAsString = valueToString(model.sem, model.significantDigits)
        val normativeRangeAsString = listOf(
            valueToString(normativeRange[0], model.significantDigits),
            valueToString(normativeRange[1], model.significantDigits)
        )
        val mdcAsString = valueToString(model.mdc, model.significantDigits)
        return NormativeScore(normativeLowerBound, semAsString, normativeRangeAsString, mdcAsString)
    }

    private fun valueToString(value: Double, digits: Int): String = (
            round(
                value * 10.0.pow(digits)
            ) / 10.0.pow(digits)
            ).toString()
}