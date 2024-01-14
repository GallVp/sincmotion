package io.github.gallvp.sincmotion

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile
import kotlin.math.ln
import kotlin.math.roundToInt

actual fun StaticOutcomesCSVRow.createAppFileName() =
    "S" +
        "${this.part}".padStart(
            2, '0',
        ) + " Test set ${this.test} on ${this.day} ${this.month} ${this.year} " + "${this.task} " + "${this.qualifier}.csv"

actual fun GaitOutcomesCSVRow.createAppFileName() =
    "S" +
        "${this.part}".padStart(
            2, '0',
        ) + " Test set ${this.test} on ${this.day} ${this.month} ${this.year} " + "${this.task} " + "${this.qualifier}.csv"

actual val GnBValidationData.Companion.dataPath
    get() = "ios_savs"

actual fun GnBValidationData.loadStaticOutcomesCSVRows(): List<StaticOutcomesCSVRow> {
    val outcomesCSV =
        "validation_data/${GnBValidationData.Companion.dataPath}/data_tables" + "/StaticBalanceOutcomes_58c4761.csv"
    val fileData = readFile(outcomesCSV, null)!!
    val allCasesList =
        fileData.trim().split("\n").drop(1).map { r ->
            val e = r.split(",").map { it.trim() }
            StaticOutcomesCSVRow(
                e[0].toInt(),
                e[1].toInt(),
                e[2].toInt(),
                e[3],
                e[4].toInt(),
                e[5],
                e[6],
                -ln(e[15].toDouble()),
                -ln(e[13].toDouble()),
                -ln(e[14].toDouble()),
            )
        }

    return allCasesList.shuffled().take(
        (allCasesList.size.toDouble() * (100.0 - validationSkipPercentage) / 100.0).roundToInt(),
    )
}

actual fun GnBValidationData.loadGaitOutcomesCSVRows(): List<GaitOutcomesCSVRow> {
    val partCharacteristicsCSV =
        "validation_data/${GnBValidationData.Companion.dataPath}/data_tables/PartCharacteristics.csv"
    val partCharacteristics =
        readFile(partCharacteristicsCSV, null)!!.trim().split("\n").drop(1).associate { r ->
            val e = r.split(",").map { it.trim() }
            Pair(e[0], e[3].toDouble() / 100.0)
        }

    val outcomesCSV =
        "validation_data/${GnBValidationData.Companion.dataPath}/data_tables/ComfortableGaitOutcomes_58c4761.csv"
    val outcomesCSVData = readFile(outcomesCSV, null)!!

    val allCasesList =
        outcomesCSVData.trim().split("\n").drop(1).map { r ->
            val e = r.split(",").map { it.trim() }
            val partNum = e[0].toInt()
            val partHeight = partCharacteristics["$partNum".padStart(2, '0')]!!
            GaitOutcomesCSVRow(
                partNum,
                partHeight,
                e[1].toInt(),
                e[2].toInt(),
                e[3],
                e[4].toInt(),
                e[5],
                e[6],
                e[7].toDouble(),
                e[8].toDouble(),
                e[9].toDouble(),
                e[10].toDouble(),
                e[11].toDouble(),
                e[12].toDouble(),
                e[13].toDouble(),
                e[14].toDouble(),
            )
        }

    return allCasesList.shuffled().take(
        (allCasesList.size.toDouble() * (100.0 - validationSkipPercentage) / 100.0).roundToInt(),
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun readFile(
    filePath: String,
    bundleID: String?,
): String? {
    val filePathInBundle = getFilePath(filePath, bundleID)
    require(filePathInBundle != null) { "Failed to get path of file: $filePath in bundle: $bundleID" }
    return NSString.stringWithContentsOfFile(filePathInBundle, NSUTF8StringEncoding, null)
}

private fun getFilePath(
    filePath: String,
    bundleID: String?,
): String? {
    val fileTokens = filePath.split(".")
    require(fileTokens.count() == 2) {
        "File name: $filePath does not comply with format: filename.ext"
    }
    // Check for the named bundle
    var selectedBundle: NSBundle? = null
    if (bundleID != null) {
        for (bundle in NSBundle.allBundles()) {
            if ((bundle as NSBundle).bundleIdentifier == bundleID) {
                selectedBundle = bundle
                break
            }
        }
    }
    if (selectedBundle == null) {
        selectedBundle = NSBundle.mainBundle
    }

    if (!selectedBundle.loaded) {
        selectedBundle.load()
    }

    return selectedBundle.pathForResource(fileTokens.first(), fileTokens.last())
}
