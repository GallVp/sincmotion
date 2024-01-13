package io.github.gallvp.sincmotion

import io.github.gallvp.sincmaths.SincMatrix
import io.github.gallvp.sincmotion.gaitandbalance.GnBGaitOutcomes
import io.github.gallvp.sincmotion.gaitandbalance.GnBStaticOutcomes

/**
 * This and the other functions are only placeholders. Android testing is only supported for the
 * androidInstrumentedTest source set as [SincMatrix] requires Java JNI libraries which can
 * only be loaded by an Android simulator/device.
 */
actual val GnBExampleCases.exampleNames: List<String>
    get() = TODO("Not yet implemented")
actual val GnBExampleCases.exampleParticipantHeight: List<Double>
    get() = TODO("Not yet implemented")
actual val GnBExampleCases.referenceOutcomes: List<Pair<GnBStaticOutcomes?, GnBGaitOutcomes?>>
    get() = TODO("Not yet implemented")
