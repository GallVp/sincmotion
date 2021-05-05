package sincmotion

data class NormativeScore(
    val normativeLowerBound: Double,
    val semAsString: String,
    val normativeRangeAsString: List<String>,
    val mdcAsString: String
)