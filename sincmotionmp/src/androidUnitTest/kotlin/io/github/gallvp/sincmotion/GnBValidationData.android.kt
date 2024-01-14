package io.github.gallvp.sincmotion

actual fun GnBValidationData.loadStaticOutcomesCSVRows(): List<StaticOutcomesCSVRow> = emptyList()

actual fun GnBValidationData.loadGaitOutcomesCSVRows(): List<GaitOutcomesCSVRow> = emptyList()

actual fun StaticOutcomesCSVRow.createAppFileName(): String = ""

actual val GnBValidationData.Companion.dataPath: String
    get() = ""

actual fun GaitOutcomesCSVRow.createAppFileName(): String = ""
