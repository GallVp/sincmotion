package io.github.gallvp.sincmotion

import io.github.gallvp.sincmotion.gaitandbalance.GnBOutcomeType
import io.github.gallvp.sincmotion.gaitandbalance.GnBOutcomeViewModel
import io.github.gallvp.sincmotion.gaitandbalance.GnBTaskType
import io.github.gallvp.sincmotion.gaitandbalance.PersonalisedNormativeBounds
import kotlin.test.assertEquals

class GnBOutcomeViewModelTests {
    fun evaluateNormativeModels() {
        assertEquals(
            PersonalisedNormativeBounds(
                GnBTaskType.WALK_HT,
                GnBOutcomeType.GAIT_SYMMETRY_INDEX,
                normativeLowerBound = 63.000000,
                normativeLowerBoundAsString = "63",
                normativeUpperBound = 79.000000,
                normativeUpperBoundAsString = "79",
                normativeRange = listOf(62.000000, 80.000000),
                normativeRangeAsString = "≥ 63",
                mdc = 6.000000,
                mdcAsString = "6",
            ),
            getPersonalisedBounds(
                GnBTaskType.WALK_HT,
                GnBOutcomeType.GAIT_SYMMETRY_INDEX,
                32.0,
                79.0,
                180.34,
            ),
        )

        assertEquals(
            PersonalisedNormativeBounds(
                GnBTaskType.WALK_HT, GnBOutcomeType.STEP_LENGTH_VAR,
                normativeLowerBound = -1.000000,
                normativeLowerBoundAsString = "-1",
                normativeUpperBound = 5.000000,
                normativeUpperBoundAsString = "5",
                normativeRange = listOf(-1.000000, 6.000000),
                normativeRangeAsString = "≤ 5",
                mdc = 4.000000,
                mdcAsString = "4",
            ),
            getPersonalisedBounds(
                GnBTaskType.WALK_HT,
                GnBOutcomeType.STEP_LENGTH_VAR,
                17.0,
                100.0,
                150.099,
            ),
        )

        assertEquals(
            PersonalisedNormativeBounds(
                GnBTaskType.FIRM_EO, GnBOutcomeType.STABILITY_AP,
                normativeLowerBound = 3.500000,
                normativeLowerBoundAsString = "3.5",
                normativeUpperBound = 4.200000,
                normativeUpperBoundAsString = "4.2",
                normativeRange = listOf(3.500000, 4.200000),
                normativeRangeAsString = "≥ 3.5",
                mdc = 0.300000,
                mdcAsString = "0.3",
            ),
            getPersonalisedBounds(
                GnBTaskType.FIRM_EO,
                GnBOutcomeType.STABILITY_AP,
                85.0,
                50.0,
                190.0,
            ),
        )

        assertEquals(
            PersonalisedNormativeBounds(
                GnBTaskType.COMPLIANT_EC,
                GnBOutcomeType.STABILITY_AP,
                normativeLowerBound = 3.100000,
                normativeLowerBoundAsString = "3.1",
                normativeUpperBound = 4.100000,
                normativeUpperBoundAsString = "4.1",
                normativeRange = listOf(3.000000, 4.200000),
                normativeRangeAsString = "≥ 3.1",
                mdc = 0.400000,
                mdcAsString = "0.4",
            ),
            getPersonalisedBounds(
                GnBTaskType.COMPLIANT_EC,
                GnBOutcomeType.STABILITY_AP,
                30.0,
                65.0,
                170.18,
            ),
        )

        assertEquals(
            PersonalisedNormativeBounds(
                GnBTaskType.WALK_HF, GnBOutcomeType.STEP_TIME_ASYMMETRY,
                normativeLowerBound = -2.000000,
                normativeLowerBoundAsString = "-2",
                normativeUpperBound = 8.000000,
                normativeUpperBoundAsString = "8",
                normativeRange = listOf(-3.000000, 9.000000),
                normativeRangeAsString = "≤ 8",
                mdc = 5.000000,
                mdcAsString = "5",
            ),
            getPersonalisedBounds(
                GnBTaskType.WALK_HF,
                GnBOutcomeType.STEP_TIME_ASYMMETRY,
                30.0,
                65.0,
                170.18,
            ),
        )
    }

    private fun getPersonalisedBounds(
        task: GnBTaskType,
        outcome: GnBOutcomeType,
        ageInYears: Double,
        massInKGs: Double,
        heightInCM: Double,
    ) = GnBOutcomeViewModel(ageInYears, massInKGs, heightInCM, task, outcome).personalisedBounds
}
