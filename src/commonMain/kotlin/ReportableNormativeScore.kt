package sincmotion

data class ReportableNormativeScore(
    val normativeLowerBound: Double,
    val normativeLowerBoundAsString: String,
    val sem: Double,
    val semAsString: String,
    val normativeRange: List<Double>,
    val normativeRangeAsString: String,
    val mdc: Double,
    val mdcAsString: String
)