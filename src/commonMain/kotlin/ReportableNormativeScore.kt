package sincmotion

data class ReportableNormativeScore(
    val normativeLowerBound: Double,
    val normativeLowerBoundAsString: String,
    val normativeUpperBound: Double,
    val normativeUpperBoundAsString: String,
    val normativeRange: List<Double>,
    val normativeRangeAsString: String,
    val mdc: Double,
    val mdcAsString: String
)
enum class NormativeRangeType {
    LOWER, MIDDLE, UPPER
}