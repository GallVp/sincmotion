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
     * WalkHF = 4
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

    fun makePresentable(taskQualifier: Int, parameters: GaitParameters) : GaitParameters {
        val digits = significantDigits[taskQualifier]
        return GaitParameters(GaitParameters.keys.zip(parameters.array.mapIndexed { index, d ->
            valueToPrecision(d, digits[index])
        }).toMap())
    }

    fun makePresentable(taskQualifier: Int, parameters: BalanceParameters) : BalanceParameters {
        val digits = significantDigits[taskQualifier]
        return BalanceParameters(BalanceParameters.keys.zip(parameters.array.mapIndexed { index, d ->
            valueToPrecision(d, digits[index])
        }).toMap())
    }

    fun makePresentable(taskQualifier: Int, key: String, value: Double) : Double {
        val digits = significantDigits[taskQualifier]
        val keyIndex = if(taskQualifier >=4) {
            GaitParameters.keys.indexOf(key)
        } else {
            BalanceParameters.keys.indexOf(key)
        }

        return valueToPrecision(value, digits[keyIndex])
    }

    private val database by lazy {
        NormativeModels.modelList.map { taskQualifiers ->
            taskQualifiers.map { model ->
                model2Score(model)
            }
        }
    }

    private val significantDigits by lazy {
        NormativeModels.modelList.map { taskQualifiers ->
            taskQualifiers.map { model ->
                model.significantDigits
            }
        }
    }

    private fun model2Score(
        model: NormativeModel
    ): NormativeScore {
        val bmi = massInKGs / (heightInCM / 100.0).pow(2)

        val normativeMean = model.intercept +
                model.ageInYearsBeta * ageInYears +
                model.bmiBeta * bmi +
                model.heightInCMBeta * heightInCM

        val normativeRange = doubleArrayOf(
            normativeMean - 1.96 * model.normativeSD,
            normativeMean + 1.96 * model.normativeSD
        )
        val normativeLowerBound = valueToPrecision(normativeMean - 1.645 * model.normativeSD, model.significantDigits)

        val semAsString = valueToString(model.sem, model.significantDigits)
        val normativeRangeAsString = listOf(
            valueToString(normativeRange[0], model.significantDigits),
            valueToString(normativeRange[1], model.significantDigits)
        )
        val mdcAsString = valueToString(model.mdc, model.significantDigits)
        return NormativeScore(normativeLowerBound, semAsString, normativeRangeAsString, mdcAsString)
    }

    private fun valueToString(value: Double, digits: Int): String = if (digits != 0) {
        (round(value * 10.0.pow(digits)) / 10.0.pow(digits)).toString()
    } else {
        round(value).toInt().toString()
    }

    private fun valueToPrecision(value: Double, digits: Int): Double = if (digits != 0) {
        round(value * 10.0.pow(digits)) / 10.0.pow(digits)
    } else {
        round(value).toInt().toDouble()
    }
}