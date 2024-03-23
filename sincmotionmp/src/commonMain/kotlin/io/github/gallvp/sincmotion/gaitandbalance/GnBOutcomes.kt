package io.github.gallvp.sincmotion.gaitandbalance

data class GnBStaticOutcomes(
    val stabilityR: Double,
    val stabilityML: Double,
    val stabilityAP: Double,
)

data class GnBGaitOutcomes(
    val meanSymIndex: Double,
    val meanStepLength: Double,
    val meanStepTime: Double,
    val stepLengthVariability: Double,
    val stepTimeVariability: Double,
    val stepLengthAsymmetry: Double,
    val stepTimeAsymmetry: Double,
    val meanStepVelocity: Double,
)
