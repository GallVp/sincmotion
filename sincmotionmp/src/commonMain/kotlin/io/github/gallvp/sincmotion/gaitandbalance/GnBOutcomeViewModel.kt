package io.github.gallvp.sincmotion.gaitandbalance

import kotlin.math.pow
import kotlin.math.round

data class GnBOutcomeViewModel(
    val personAgeInYears: Double,
    val personMassInKGs: Double,
    val personHeightInCM: Double,
    val task: GnBTaskType,
    val outcome: GnBOutcomeType,
) {
    fun makeOutcomeValueReportable(value: Double): Pair<Double, String> {
        return Pair(
            valueToPrecision(value, normativeModel.significantDigits),
            valueToString(value, normativeModel.significantDigits),
        )
    }

    fun makeReportable(outcomeValue: Double): ReportableOutcome {
        val (reportableValue, reportableValueString) = makeOutcomeValueReportable(outcomeValue)
        val reportableRange = personalisedBounds.normativeRangeAsString

        val isHighlighted =
            when (outcome.normativeRangeType) {
                NormativeRangeType.LOWER -> reportableValue < personalisedBounds.normativeLowerBound

                NormativeRangeType.MIDDLE ->
                    reportableValue < personalisedBounds.normativeRange[0] ||
                        reportableValue > personalisedBounds.normativeRange[1]

                NormativeRangeType.UPPER -> reportableValue > personalisedBounds.normativeUpperBound
            }

        return ReportableOutcome(
            task,
            outcome,
            reportableValueString,
            reportableRange,
            isHighlighted,
        )
    }

    val personalisedBounds: PersonalisedNormativeBounds
        get() {

            val bmi = personMassInKGs / (personHeightInCM / 100.0).pow(2)

            val normativeMean =
                normativeModel.intercept +
                    normativeModel.ageInYearsBeta * personAgeInYears +
                    normativeModel.bmiBeta * bmi +
                    normativeModel.heightInCMBeta * personHeightInCM

            val normativeLowerBound =
                valueToPrecision(
                    normativeMean - 1.645 * normativeModel.normativeSD,
                    normativeModel.significantDigits,
                )
            val normativeLowerBoundAsString =
                valueToString(normativeLowerBound, normativeModel.significantDigits)

            val normativeUpperBound =
                valueToPrecision(
                    normativeMean + 1.645 * normativeModel.normativeSD,
                    normativeModel.significantDigits,
                )
            val normativeUpperBoundAsString =
                valueToString(normativeUpperBound, normativeModel.significantDigits)

            val normativeRange =
                listOf(
                    normativeMean - 1.96 * normativeModel.normativeSD,
                    normativeMean + 1.96 * normativeModel.normativeSD,
                )
            val normativeRangeWithPrecision =
                normativeRange.map {
                    valueToPrecision(it, normativeModel.significantDigits)
                }

            val normativeRangeAsString =
                when (this.outcome.normativeRangeType) {
                    NormativeRangeType.LOWER -> "≥ $normativeLowerBoundAsString"
                    NormativeRangeType.MIDDLE ->
                        "[${
                            valueToString(
                                normativeRange[0],
                                normativeModel.significantDigits,
                            )
                        }, ${
                            valueToString(
                                normativeRange[1],
                                normativeModel.significantDigits,
                            )
                        }]"

                    NormativeRangeType.UPPER -> "≤ $normativeUpperBoundAsString"
                }

            val mdcWithPrecision =
                valueToPrecision(normativeModel.mdc, normativeModel.significantDigits)
            val mdcAsString = valueToString(normativeModel.mdc, normativeModel.significantDigits)

            return PersonalisedNormativeBounds(
                this.task,
                this.outcome,
                normativeLowerBound,
                normativeLowerBoundAsString,
                normativeUpperBound,
                normativeUpperBoundAsString,
                normativeRangeWithPrecision,
                normativeRangeAsString,
                mdcWithPrecision,
                mdcAsString,
            )
        }

    val normativeModel: NormativeModel
        get() {
            val model = normativeModels[this.task]?.get(this.outcome)

            require(model != null) { "Normative model for ${this.outcome} from ${this.task} is not defined" }

            return model
        }

    companion object {
        private fun valueToString(
            value: Double,
            digits: Int,
        ): String =
            if (digits != 0) {
                (round(value * 10.0.pow(digits)) / 10.0.pow(digits)).toString()
            } else {
                round(value).toInt().toString()
            }

        private fun valueToPrecision(
            value: Double,
            digits: Int,
        ): Double =
            if (digits != 0) {
                round(value * 10.0.pow(digits)) / 10.0.pow(digits)
            } else {
                round(value).toInt().toDouble()
            }

        val normativeModels: Map<GnBTaskType, Map<GnBOutcomeType, NormativeModel>> by lazy {
            mapOf(
                Pair(
                    GnBTaskType.FIRM_EO,
                    mapOf(
                        Pair(
                            GnBOutcomeType.STABILITY,
                            NMFirmEOStability,
                        ),
                        Pair(
                            GnBOutcomeType.STABILITY_ML,
                            NMFirmEOStabilityML,
                        ),
                        Pair(
                            GnBOutcomeType.STABILITY_AP,
                            NMFirmEOStabilityAP,
                        ),
                    ),
                ),
                Pair(
                    GnBTaskType.FIRM_EC,
                    mapOf(
                        Pair(
                            GnBOutcomeType.STABILITY,
                            NMFirmECStability,
                        ),
                        Pair(
                            GnBOutcomeType.STABILITY_ML,
                            NMFirmECStabilityML,
                        ),
                        Pair(
                            GnBOutcomeType.STABILITY_AP,
                            NMFirmECStabilityAP,
                        ),
                    ),
                ),
                Pair(
                    GnBTaskType.COMPLIANT_EO,
                    mapOf(
                        Pair(
                            GnBOutcomeType.STABILITY,
                            NMCompliantEOStability,
                        ),
                        Pair(
                            GnBOutcomeType.STABILITY_ML,
                            NMCompliantEOStabilityML,
                        ),
                        Pair(
                            GnBOutcomeType.STABILITY_AP,
                            NMCompliantEOStabilityAP,
                        ),
                    ),
                ),
                Pair(
                    GnBTaskType.COMPLIANT_EC,
                    mapOf(
                        Pair(
                            GnBOutcomeType.STABILITY,
                            NMCompliantECStability,
                        ),
                        Pair(
                            GnBOutcomeType.STABILITY_ML,
                            NMCompliantECStabilityML,
                        ),
                        Pair(
                            GnBOutcomeType.STABILITY_AP,
                            NMCompliantECStabilityAP,
                        ),
                    ),
                ),
                Pair(
                    GnBTaskType.WALK_HF,
                    mapOf(
                        Pair(
                            GnBOutcomeType.GAIT_SYMMETRY_INDEX,
                            NMWalkHFGaitSymmetry,
                        ),
                        Pair(
                            GnBOutcomeType.STEP_LENGTH,
                            NMWalkHFStepLength,
                        ),
                        Pair(
                            GnBOutcomeType.STEP_TIME,
                            NMWalkHFStepTime,
                        ),
                        Pair(
                            GnBOutcomeType.STEP_LENGTH_VAR,
                            NMWalkHFStepLengthVar,
                        ),
                        Pair(
                            GnBOutcomeType.STEP_TIME_VAR,
                            NMWalkHFStepTimeVar,
                        ),
                        Pair(
                            GnBOutcomeType.STEP_LENGTH_ASYMMETRY,
                            NMWalkHFStepLengthAsym,
                        ),
                        Pair(
                            GnBOutcomeType.STEP_TIME_ASYMMETRY,
                            NMWalkHFStepTimeAsym,
                        ),
                        Pair(
                            GnBOutcomeType.STEP_VELOCITY,
                            NMWalkHFStepVelocity,
                        ),
                    ),
                ),
                Pair(
                    GnBTaskType.WALK_HT,
                    mapOf(
                        Pair(
                            GnBOutcomeType.GAIT_SYMMETRY_INDEX,
                            NMWalkHTGaitSymmetry,
                        ),
                        Pair(
                            GnBOutcomeType.STEP_LENGTH,
                            NMWalkHTStepLength,
                        ),
                        Pair(
                            GnBOutcomeType.STEP_TIME,
                            NMWalkHTStepTime,
                        ),
                        Pair(
                            GnBOutcomeType.STEP_LENGTH_VAR,
                            NMWalkHTStepLengthVar,
                        ),
                        Pair(
                            GnBOutcomeType.STEP_TIME_VAR,
                            NMWalkHTStepTimeVar,
                        ),
                        Pair(
                            GnBOutcomeType.STEP_LENGTH_ASYMMETRY,
                            NMWalkHTStepLengthAsym,
                        ),
                        Pair(
                            GnBOutcomeType.STEP_TIME_ASYMMETRY,
                            NMWalkHTStepTimeAsym,
                        ),
                        Pair(
                            GnBOutcomeType.STEP_VELOCITY,
                            NMWalkHTStepVelocity,
                        ),
                    ),
                ),
            )
        }
    }
}

data class PersonalisedNormativeBounds(
    val task: GnBTaskType,
    val outcome: GnBOutcomeType,
    val normativeLowerBound: Double,
    val normativeLowerBoundAsString: String,
    val normativeUpperBound: Double,
    val normativeUpperBoundAsString: String,
    val normativeRange: List<Double>,
    val normativeRangeAsString: String,
    val mdc: Double,
    val mdcAsString: String,
)

data class ReportableOutcome(
    val task: GnBTaskType,
    val outcome: GnBOutcomeType,
    val reportableValueString: String,
    val reportableRange: String,
    val isRangeHighlighted: Boolean,
)

enum class GnBTaskType {
    FIRM_EO,
    FIRM_EC,
    COMPLIANT_EO,
    COMPLIANT_EC,
    WALK_HF,
    WALK_HT,
}

enum class GnBOutcomeType(
    val tag: String,
    val displayName: String,
    val displayUnits: String,
    val normativeRangeType: NormativeRangeType,
) {
    STABILITY("stb-r", "Stability", "-ln[m/s²]", NormativeRangeType.LOWER),
    STABILITY_ML("stb-ml", "Stability ML", "-ln[m/s²]", NormativeRangeType.LOWER),
    STABILITY_AP("stb-ap", "Stability AP", "-ln[m/s²]", NormativeRangeType.LOWER),
    GAIT_SYMMETRY_INDEX(
        "sym",
        "Walking balance",
        "%",
        NormativeRangeType.LOWER,
    ),
    STEP_LENGTH(
        "s-len",
        "Step length",
        "m",
        NormativeRangeType.LOWER,
    ),
    STEP_TIME(
        "s-time",
        "Step time",
        "s",
        NormativeRangeType.LOWER,
    ),
    STEP_LENGTH_VAR(
        "slv",
        "Step length variability",
        "%",
        NormativeRangeType.UPPER,
    ),
    STEP_TIME_VAR(
        "stv",
        "Step time variability",
        "%",
        NormativeRangeType.UPPER,
    ),
    STEP_LENGTH_ASYMMETRY(
        "sla",
        "Step length asymmetry",
        "%",
        NormativeRangeType.UPPER,
    ),
    STEP_TIME_ASYMMETRY(
        "sta",
        "Step time asymmetry",
        "%",
        NormativeRangeType.UPPER,
    ),
    STEP_VELOCITY(
        "s-vel",
        "Walking speed",
        "m/s",
        NormativeRangeType.LOWER,
    ),
}
