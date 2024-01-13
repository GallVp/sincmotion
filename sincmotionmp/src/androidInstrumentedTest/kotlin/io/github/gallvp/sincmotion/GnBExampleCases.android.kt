package io.github.gallvp.sincmotion

import io.github.gallvp.sincmotion.gaitandbalance.GnBGaitOutcomes
import io.github.gallvp.sincmotion.gaitandbalance.GnBStaticOutcomes

actual val GnBExampleCases.exampleNames
    get() =
        listOf(
            "walk_ht_android",
            "compliant_surface_eyes_open_android",
        )

actual val GnBExampleCases.exampleParticipantHeight
    get() =
        listOf(
            1.68,
            1.68,
        )
actual val GnBExampleCases.referenceOutcomes
    get() =
        listOf(
            Pair(
                null,
                GnBGaitOutcomes(
                    meanSymIndex = 66.2248021220862,
                    meanStepLength = 0.6968765886622427,
                    meanStepTime = 0.5300000000000000,
                    stepLengthVariability = 4.009631939375744,
                    stepTimeVariability = 4.074739269633336,
                    stepLengthAsymmetry = 4.786694470630516,
                    stepTimeAsymmetry = 3.7735849056603805,
                    meanStepVelocity = 1.3267541834580454,
                ),
            ),
            Pair(
                GnBStaticOutcomes(
                    stabilityR = 2.874518852089475,
                    stabilityML = 3.7903004134292195,
                    stabilityAP = 3.800905800032107,
                ),
                null,
            ),
        )
