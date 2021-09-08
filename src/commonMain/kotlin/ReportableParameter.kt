package sincmotion

data class ReportableParameter(
    val reportableName: String,
    val reportableValue: String,
    val reportableRange: String,
    val reportableUnits: String,
    val isRangeHighlighted: Boolean
)