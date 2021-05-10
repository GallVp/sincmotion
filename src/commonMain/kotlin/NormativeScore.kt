package sincmotion

data class NormativeScore(
    val normativeLowerBound: Double,
    val normativeLowerBoundAsString: String,
    val sem:Double,
    val semAsString: String,
    val normativeRange: List<Double>,
    val normativeRangeAsString: List<String>,
    val mdc:Double,
    val mdcAsString: String
)