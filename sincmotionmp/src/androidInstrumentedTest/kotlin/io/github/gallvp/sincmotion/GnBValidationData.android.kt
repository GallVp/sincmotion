package io.github.gallvp.sincmotion

actual val GnBValidationData.validationCases: Sequence<GnBTestCase>
    get() = sequenceOf()

actual class StaticOutcomesCSVRow

actual fun GnBValidationData.loadStaticOutcomesCSVRows(): List<StaticOutcomesCSVRow> {
    return emptyList()
}

actual class GaitOutcomesCSVRow

actual fun GnBValidationData.loadGaitOutcomesCSVRows(): List<GaitOutcomesCSVRow> {
    return emptyList()
}
