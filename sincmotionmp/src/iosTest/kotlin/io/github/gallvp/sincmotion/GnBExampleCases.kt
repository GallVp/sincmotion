package io.github.gallvp.sincmotion

import io.github.gallvp.sincmotion.gaitandbalance.GnBGaitOutcomes
import io.github.gallvp.sincmotion.gaitandbalance.GnBStaticOutcomes

actual val GnBExampleCases.exampleNames
    get() =
        listOf(
            "walk_hf_ios",
            "firm_surface_eyes_closed_ios",
        )

actual val GnBExampleCases.exampleParticipantHeight
    get() =
        listOf(
            1.64,
            1.64,
        )
actual val GnBExampleCases.referenceOutcomes
    get() =
        listOf(
            Pair(
                null,
                GnBGaitOutcomes(
                    meanSymIndex = 68.76436465549325,
                    meanStepLength = 0.6380031475203751,
                    meanStepTime = 0.5700000000000000,
                    stepLengthVariability = 2.5090538539828726,
                    stepTimeVariability = 2.679080978309065,
                    stepLengthAsymmetry = 2.451660980753842,
                    stepTimeAsymmetry = 1.7543859649122824,
                    meanStepVelocity = 1.1146040171704668,
                ),
            ),
            Pair(
                GnBStaticOutcomes(
                    3.5146148603432237,
                    4.1171672095899510,
                    4.099491245557936,
                ),
                null,
            ),
        )
