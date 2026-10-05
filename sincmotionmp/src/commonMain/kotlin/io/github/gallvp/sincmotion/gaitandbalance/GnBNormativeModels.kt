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

expect object NMFirmEOStability : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMWalkHTStepVelocity : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMWalkHTStepTimeAsym : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMWalkHTStepLengthAsym : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMWalkHTStepTimeVar : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMWalkHTStepLengthVar : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMWalkHTStepTime : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMWalkHTStepLength : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMWalkHTGaitSymmetry : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMWalkHFStepVelocity : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMWalkHFStepTimeAsym : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMWalkHFStepLengthAsym : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMWalkHFStepTimeVar : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMWalkHFStepLengthVar : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMWalkHFStepTime : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMWalkHFStepLength : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMWalkHFGaitSymmetry : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMCompliantECStabilityAP : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMCompliantECStabilityML : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMCompliantECStability : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMCompliantEOStabilityAP : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMCompliantEOStabilityML : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMCompliantEOStability : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMFirmECStabilityAP : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMFirmECStabilityML : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMFirmECStability : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMFirmEOStabilityAP : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}

expect object NMFirmEOStabilityML : NormativeModel {
    override val intercept: Double
    override val ageInYearsBeta: Double
    override val bmiBeta: Double
    override val heightInCMBeta: Double
    override val sigmaBetween: Double
    override val sigmaTest: Double
    override val sigmaWithin: Double

    override val sem: Double
    override val mdc: Double
    override val normativeSD: Double
    override val decimalPlaces: Int
}
