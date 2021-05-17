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
     */
    fun getNormativeScore(taskQualifier: Int, outcomeKey: String): ReportableNormativeScore? =
        database[taskQualifier][outcomeKey]

    fun getSignificantDigits(taskQualifier: Int, outcomeKey: String): Int? =
        significantDigits[taskQualifier][outcomeKey]

    /**
     * Returns string and double with precision.
     */
    fun makePresentable(taskQualifier: Int, key: String, value: Double): Pair<String, Double> {
        val digits = significantDigits[taskQualifier][key]!!
        return Pair(valueToString(value, digits), valueToPrecision(value, digits))
    }

    fun makeReportableParameters(taskQualifier: Int, gaitParameters: GaitParameters): Map<String, ReportableParameter> {
        val digits = significantDigits[taskQualifier]
        val normativeScores = database[taskQualifier]
        return gaitParameters.map.map {
            val key = it.key
            val value = it.value

            val reportableName = GaitParameters.names[key]!!
            val scoreValue = valueToPrecision(value, digits[key]!!)
            val reportableValue = valueToString(value, digits[key]!!)
            val reportableSEM = normativeScores[key]!!.semAsString
            val reportableUnits = GaitParameters.units[key]!!
            val reportableRange = normativeScores[key]!!.normativeRangeAsString

            val isHighlighted = if (GaitParameters.isLowerBoundedOnly[key]!!) {
                scoreValue < normativeScores[key]!!.normativeLowerBound
            } else {
                scoreValue < normativeScores[key]!!.normativeRange[0] || scoreValue > normativeScores[key]!!.normativeRange[1]
            }

            Pair(
                key, ReportableParameter(
                    reportableName,
                    reportableValue,
                    reportableSEM,
                    reportableRange,
                    reportableUnits,
                    isHighlighted
                )
            )
        }.toMap()
    }

    fun makeReportableParameters(
        taskQualifier: Int,
        balanceParameters: BalanceParameters
    ): Map<String, ReportableParameter> {
        val digits = significantDigits[taskQualifier]
        val normativeScores = database[taskQualifier]
        return balanceParameters.map.map {

            val key = it.key
            val value = it.value

            val reportableName = BalanceParameters.names[key]!!
            val scoreValue = valueToPrecision(value, digits[key]!!)
            val reportableValue = valueToString(value, digits[key]!!)
            val reportableSEM = normativeScores[key]!!.semAsString
            val reportableUnits = BalanceParameters.units[key]!!
            val reportableRange = normativeScores[key]!!.normativeRangeAsString

            val isHighlighted: Boolean = if (BalanceParameters.isLowerBoundedOnly[key]!!) {
                scoreValue < normativeScores[key]!!.normativeLowerBound
            } else {
                scoreValue < normativeScores[key]!!.normativeRange[0] || scoreValue > normativeScores[key]!!.normativeRange[1]
            }

            Pair(
                key, ReportableParameter(
                    reportableName,
                    reportableValue,
                    reportableSEM,
                    reportableRange,
                    reportableUnits,
                    isHighlighted
                )
            )
        }.toMap()
    }

    private val database by lazy {
        NormativeModels.modelList.map { taskQualifier ->
            taskQualifier.map { keyModel ->
                model2Score(keyModel)
            }.toMap()
        }
    }

    private val significantDigits by lazy {
        NormativeModels.modelList.map { taskQualifier ->
            taskQualifier.map { keyModel ->
                Pair(keyModel.key, keyModel.value.significantDigits)
            }.toMap()
        }
    }

    private fun model2Score(
        keyModel: Map.Entry<String, NormativeModel>
    ): Pair<String, ReportableNormativeScore> {

        val key = keyModel.key
        val model = keyModel.value

        val bmi = massInKGs / (heightInCM / 100.0).pow(2)

        val normativeMean = model.intercept +
                model.ageInYearsBeta * ageInYears +
                model.bmiBeta * bmi +
                model.heightInCMBeta * heightInCM

        val normativeLowerBound = valueToPrecision(normativeMean - 1.645 * model.normativeSD, model.significantDigits)
        val normativeLowerBoundAsString = valueToString(normativeLowerBound, model.significantDigits)

        val semWithPrecision = valueToPrecision(model.sem, model.significantDigits)
        val semAsString = valueToString(model.sem, model.significantDigits)

        val normativeRange = listOf(
            normativeMean - 1.96 * model.normativeSD,
            normativeMean + 1.96 * model.normativeSD
        )
        val normativeRangeWithPrecision = normativeRange.map {
            valueToPrecision(it, model.significantDigits)
        }

        val isLowerBoundedOnly =
            GaitParameters.isLowerBoundedOnly[key] == true || BalanceParameters.isLowerBoundedOnly[key] == true

        val normativeRangeAsString = if (isLowerBoundedOnly) {
            "≥ $normativeLowerBoundAsString"
        } else {
            "[${valueToString(normativeRange[0], model.significantDigits)}, ${
                valueToString(
                    normativeRange[1],
                    model.significantDigits
                )
            }]"
        }

        val mdcWithPrecision = valueToPrecision(model.mdc, model.significantDigits)
        val mdcAsString = valueToString(model.mdc, model.significantDigits)

        return Pair(
            key, ReportableNormativeScore(
                normativeLowerBound,
                normativeLowerBoundAsString,
                semWithPrecision,
                semAsString,
                normativeRangeWithPrecision,
                normativeRangeAsString,
                mdcWithPrecision,
                mdcAsString
            )
        )
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