package io.github.gallvp.sincmotion.gaitandbalance

import kotlin.math.pow
import kotlin.math.round

/** A data class for a person's [GnBOutcomeType] from a [GnBTaskType]
 * @property value Outcome value which has been rounded to correct decimal places
 * @property valueString [value] as [String]
 * @property normativeRange Normative range representation as [String] such as < 1.5, [1 2], etc.
 * @property valueIsOutsideNorms true if the outcome value is outside the normative range
 */
data class GnBPersonOutcome(
    val task: GnBTaskType,
    val outcome: GnBOutcomeType,
    val value: Double,
    val valueString: String,
    val normativeRange: String,
    val valueIsOutsideNorms: Boolean,
)

/** A data class representing normative bounds for a person for specific [GnBOutcomeType] and
 * [GnBTaskType]. Both the [Double] and [String] properties are rounded to correct decimal places
 * digits.
 *
 */
data class GnBPersonNorms(
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
    val decimalPlaces: Int,
)

enum class GnBTaskType(val outcomeTypes: List<GnBOutcomeType>) {
    FIRM_EO(GnBOutcomeType.staticOutcomeTypes),
    FIRM_EC(GnBOutcomeType.staticOutcomeTypes),
    COMPLIANT_EO(GnBOutcomeType.staticOutcomeTypes),
    COMPLIANT_EC(GnBOutcomeType.staticOutcomeTypes),
    WALK_HF(GnBOutcomeType.gaitOutcomeTypes),
    WALK_HT(GnBOutcomeType.gaitOutcomeTypes),
}

enum class GnBOutcomeType(
    val tag: String,
    val displayName: String,
    val displayUnits: String,
    val normativeRangeType: NormativeRangeType,
    val isGaitOutcome: Boolean,
) {
    STABILITY("stb-r", "Stability", "-ln[m/s²]", NormativeRangeType.LOWER, false),
    STABILITY_ML("stb-ml", "Stability ML", "-ln[m/s²]", NormativeRangeType.LOWER, false),
    STABILITY_AP("stb-ap", "Stability AP", "-ln[m/s²]", NormativeRangeType.LOWER, false),
    GAIT_SYMMETRY_INDEX(
        "sym",
        "Walking balance",
        "%",
        NormativeRangeType.LOWER,
        true,
    ),
    STEP_LENGTH(
        "s-len",
        "Step length",
        "m",
        NormativeRangeType.LOWER,
        true,
    ),
    STEP_TIME(
        "s-time",
        "Step time",
        "s",
        NormativeRangeType.LOWER,
        true,
    ),
    STEP_LENGTH_VAR(
        "slv",
        "Step length variability",
        "%",
        NormativeRangeType.UPPER,
        true,
    ),
    STEP_TIME_VAR(
        "stv",
        "Step time variability",
        "%",
        NormativeRangeType.UPPER,
        true,
    ),
    STEP_LENGTH_ASYMMETRY(
        "sla",
        "Step length asymmetry",
        "%",
        NormativeRangeType.UPPER,
        true,
    ),
    STEP_TIME_ASYMMETRY(
        "sta",
        "Step time asymmetry",
        "%",
        NormativeRangeType.UPPER,
        true,
    ),
    STEP_VELOCITY(
        "s-vel",
        "Walking speed",
        "m/s",
        NormativeRangeType.LOWER,
        true,
    ),
    ;

    fun rawValueFromGnBGaitOutcomes(outcomes: GnBGaitOutcomes) =
        when (this) {
            STABILITY -> null
            STABILITY_ML -> null
            STABILITY_AP -> null
            GAIT_SYMMETRY_INDEX -> outcomes.meanSymIndex
            STEP_LENGTH -> outcomes.meanStepLength
            STEP_TIME -> outcomes.meanStepTime
            STEP_LENGTH_VAR -> outcomes.stepLengthVariability
            STEP_TIME_VAR -> outcomes.stepTimeVariability
            STEP_LENGTH_ASYMMETRY -> outcomes.stepLengthAsymmetry
            STEP_TIME_ASYMMETRY -> outcomes.stepTimeAsymmetry
            STEP_VELOCITY -> outcomes.meanStepVelocity
        }

    fun rawValueFromGnBStaticOutcomes(outcomes: GnBStaticOutcomes) =
        when (this) {
            STABILITY -> outcomes.stabilityR
            STABILITY_ML -> outcomes.stabilityML
            STABILITY_AP -> outcomes.stabilityAP
            GAIT_SYMMETRY_INDEX -> null
            STEP_LENGTH -> null
            STEP_TIME -> null
            STEP_LENGTH_VAR -> null
            STEP_TIME_VAR -> null
            STEP_LENGTH_ASYMMETRY -> null
            STEP_TIME_ASYMMETRY -> null
            STEP_VELOCITY -> null
        }

    fun rawValueFrom(
        static: GnBStaticOutcomes?,
        gait: GnBGaitOutcomes?,
    ) = if (static != null) {
        rawValueFromGnBStaticOutcomes(static)
    } else {
        gait?.let { rawValueFromGnBGaitOutcomes(it) }
    }

    companion object {
        val gaitOutcomeTypes: List<GnBOutcomeType> = GnBOutcomeType.entries.filter { it.isGaitOutcome }
        val staticOutcomeTypes: List<GnBOutcomeType> = GnBOutcomeType.entries.filter { !it.isGaitOutcome }

        fun csvRowFromGnBGaitOutcomes(outcomes: GnBGaitOutcomes) =
            entries.map {
                it.rawValueFromGnBGaitOutcomes(outcomes)
            }.joinToString(",") { if (it != null) "$it" else "" }

        fun csvRowFromGnBStaticOutcomes(outcomes: GnBStaticOutcomes) =
            entries.map {
                it.rawValueFromGnBStaticOutcomes(outcomes)
            }.joinToString(",") { if (it != null) "$it" else "" }
    }
}

data class GnBPersonOutcomeComposer(
    val personAgeInYears: Double,
    val personMassInKGs: Double,
    val personHeightInCM: Double,
    val task: GnBTaskType,
    val outcome: GnBOutcomeType,
) {
    fun compose(rawValue: Double): GnBPersonOutcome {
        val (roundedValue, roundedValueString) = roundOutcomeValue(rawValue)
        val reportableRange = personNorms.normativeRangeAsString

        val isHighlighted =
            when (outcome.normativeRangeType) {
                NormativeRangeType.LOWER -> roundedValue < personNorms.normativeLowerBound

                NormativeRangeType.MIDDLE ->
                    roundedValue < personNorms.normativeRange[0] ||
                        roundedValue > personNorms.normativeRange[1]

                NormativeRangeType.UPPER -> roundedValue > personNorms.normativeUpperBound
            }

        return GnBPersonOutcome(
            task,
            outcome,
            roundedValue,
            roundedValueString,
            reportableRange,
            isHighlighted,
        )
    }

    val personNorms: GnBPersonNorms
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
                    normativeModel.decimalPlaces,
                )
            val normativeLowerBoundAsString =
                valueToString(normativeLowerBound, normativeModel.decimalPlaces)

            val normativeUpperBound =
                valueToPrecision(
                    normativeMean + 1.645 * normativeModel.normativeSD,
                    normativeModel.decimalPlaces,
                )
            val normativeUpperBoundAsString =
                valueToString(normativeUpperBound, normativeModel.decimalPlaces)

            val normativeRange =
                listOf(
                    normativeMean - 1.96 * normativeModel.normativeSD,
                    normativeMean + 1.96 * normativeModel.normativeSD,
                )
            val normativeRangeWithPrecision =
                normativeRange.map {
                    valueToPrecision(it, normativeModel.decimalPlaces)
                }

            val normativeRangeAsString =
                when (this.outcome.normativeRangeType) {
                    NormativeRangeType.LOWER -> "≥ $normativeLowerBoundAsString"
                    NormativeRangeType.MIDDLE ->
                        "[${
                            valueToString(
                                normativeRange[0],
                                normativeModel.decimalPlaces,
                            )
                        }, ${
                            valueToString(
                                normativeRange[1],
                                normativeModel.decimalPlaces,
                            )
                        }]"

                    NormativeRangeType.UPPER -> "≤ $normativeUpperBoundAsString"
                }

            val mdcWithPrecision =
                valueToPrecision(normativeModel.mdc, normativeModel.decimalPlaces)
            val mdcAsString = valueToString(normativeModel.mdc, normativeModel.decimalPlaces)

            return GnBPersonNorms(
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
                normativeModel.decimalPlaces,
            )
        }

    private val normativeModel: NormativeModel
        get() {
            val model = normativeModels[this.task]?.get(this.outcome)

            require(model != null) { "Normative model for ${this.outcome} from ${this.task} is not defined" }

            return model
        }

    private fun roundOutcomeValue(value: Double): Pair<Double, String> {
        return Pair(
            valueToPrecision(value, normativeModel.decimalPlaces),
            valueToString(value, normativeModel.decimalPlaces),
        )
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
