package io.github.gallvp.sincmotion.gaitandbalance

interface NormativeModel {
    val intercept: Double
    val ageInYearsBeta: Double
    val bmiBeta: Double
    val heightInCMBeta: Double
    val sigmaBetween: Double
    val sigmaTest: Double
    val sigmaWithin: Double

    val sem: Double
    val mdc: Double
    val normativeSD: Double
    val decimalPlaces: Int
}

enum class NormativeRangeType {
    LOWER,
    MIDDLE,
    UPPER,
}

expect object NMFirmEOStability : NormativeModel

expect object NMWalkHTStepVelocity : NormativeModel

expect object NMWalkHTStepTimeAsym : NormativeModel

expect object NMWalkHTStepLengthAsym : NormativeModel

expect object NMWalkHTStepTimeVar : NormativeModel

expect object NMWalkHTStepLengthVar : NormativeModel

expect object NMWalkHTStepTime : NormativeModel

expect object NMWalkHTStepLength : NormativeModel

expect object NMWalkHTGaitSymmetry : NormativeModel

expect object NMWalkHFStepVelocity : NormativeModel

expect object NMWalkHFStepTimeAsym : NormativeModel

expect object NMWalkHFStepLengthAsym : NormativeModel

expect object NMWalkHFStepTimeVar : NormativeModel

expect object NMWalkHFStepLengthVar : NormativeModel

expect object NMWalkHFStepTime : NormativeModel

expect object NMWalkHFStepLength : NormativeModel

expect object NMWalkHFGaitSymmetry : NormativeModel

expect object NMCompliantECStabilityAP : NormativeModel

expect object NMCompliantECStabilityML : NormativeModel

expect object NMCompliantECStability : NormativeModel

expect object NMCompliantEOStabilityAP : NormativeModel

expect object NMCompliantEOStabilityML : NormativeModel

expect object NMCompliantEOStability : NormativeModel

expect object NMFirmECStabilityAP : NormativeModel

expect object NMFirmECStabilityML : NormativeModel

expect object NMFirmECStability : NormativeModel

expect object NMFirmEOStabilityAP : NormativeModel

expect object NMFirmEOStabilityML : NormativeModel
