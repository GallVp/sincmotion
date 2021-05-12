package sincmotion

data class ReportableParameter(
    val reportableName: String,
    val reportableValue: String,
    val reportableSEM: String,
    val reportableRange: String,
    val reportableUnits: String,
    val isHighlighted: Boolean
)