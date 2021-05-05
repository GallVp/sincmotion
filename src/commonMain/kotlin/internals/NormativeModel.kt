package sincmotion.internals

internal interface NormativeModel {
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
    val significantDigits: Int
}