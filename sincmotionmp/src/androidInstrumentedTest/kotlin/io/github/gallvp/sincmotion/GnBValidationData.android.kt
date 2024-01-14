package io.github.gallvp.sincmotion

import io.github.gallvp.sincmaths.SincMatrix
import kotlin.math.roundToInt

actual fun StaticOutcomesCSVRow.createAppFileName(): String {
    val addSpaceForParticipants = listOf(21, 23, 24)
    val testNumMap = listOf(-1, 1, 3, 5) // 0 => undefined, 1 => 1, 2 => 3, 3 => 5
    var spacer = ""
    if (this.part in addSpaceForParticipants) {
        spacer = " "
    }
    return "Sub" +
        "${this.part}".padStart(
            2, '0',
        ) + " X/" + "Sub$spacer" +
        "${this.part}".padStart(
            2, '0',
        ) + " Test no ${testNumMap[this.test]} on " +
        "${this.day}".padStart(
            2, '0',
        ) + "-${this.month}-${this.year} " + this.task + " ${this.qualifier}.csv"
}

actual fun GaitOutcomesCSVRow.createAppFileName(): String {
    val addSpaceForParticipants = listOf(21, 23, 24)
    val testNumMap = listOf(-1, 1, 3, 5) // 0 => undefined, 1 => 1, 2 => 3, 3 => 5
    var spacer = ""
    if (this.part in addSpaceForParticipants) {
        spacer = " "
    }
    return "Sub" +
        "${this.part}".padStart(
            2, '0',
        ) + " X/" + "Sub$spacer" +
        "${this.part}".padStart(
            2, '0',
        ) + " Test no ${testNumMap[this.test]} on " +
        "${this.day}".padStart(
            2, '0',
        ) + "-${this.month}-${this.year} " + this.task + " ${this.qualifier}.csv"
}

actual val GnBValidationData.Companion.dataPath
    get() = "android_navs"

actual fun GnBValidationData.loadStaticOutcomesCSVRows(): List<StaticOutcomesCSVRow> {
    val outcomesCSV =
        "validation_data/${GnBValidationData.Companion.dataPath}/data_tables" + "/StaticBalanceOutcomes_ad2685f.csv"
    val fileData = readFile(outcomesCSV)!!
    val allCasesList =
        fileData.trim().split("\n").drop(1).filter { r ->
            val e = r.split(",").map { it.trim() }
            e[7] == "X" // Device
        }.filter { r ->
            val e = r.split(",").map { it.trim() }
            e[6].takeLast(2) != "HT" // HT static task
        }.map { r ->
            val e = r.split(",").map { it.trim() }
            StaticOutcomesCSVRow(
                e[0].toInt(),
                e[1].toInt(),
                e[2].toInt(),
                e[3],
                e[4].toInt(),
                e[5],
                e[6],
                e[9].toDouble(),
                e[10].toDouble(),
                e[11].toDouble(),
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
        readFile(partCharacteristicsCSV)!!.trim().split("\n").drop(1).associate { r ->
            val e = r.split(",").map { it.trim() }
            Pair(e[0], e[2].toDouble() / 100.0)
        }

    val outcomesCSV =
        "validation_data/${GnBValidationData.Companion.dataPath}/data_tables/ComfortableGaitOutcomes_a1e4b14.csv"
    val outcomesCSVData = readFile(outcomesCSV)!!

    val allCasesList =
        outcomesCSVData.trim().split("\n").drop(1).filter { r ->
            val e = r.split(",").map { it.trim() }
            e[7] == "X" // Device
        }.map { r ->
            val e = r.split(",").map { it.trim() }
            val partHeight = partCharacteristics[e[0]]!!
            GaitOutcomesCSVRow(
                e[0].toInt(),
                partHeight,
                e[1].toInt(),
                e[2].toInt(),
                e[3],
                e[4].toInt(),
                e[5],
                e[6],
                e[9].toDouble(),
                e[10].toDouble(),
                e[11].toDouble(),
                e[12].toDouble(),
                e[13].toDouble(),
                e[14].toDouble(),
                e[15].toDouble(),
                e[16].toDouble(),
            )
        }

    return allCasesList.shuffled().take(
        (allCasesList.size.toDouble() * (100.0 - validationSkipPercentage) / 100.0).roundToInt(),
    )
}

private fun readFile(filePath: String): String? = readFileAsTextUsingInputStream(filePath)

private fun readFileAsTextUsingInputStream(fileName: String): String? =
    SincMatrix.Companion::class.java.getResourceAsStream(fileName)?.readBytes()
        ?.toString(Charsets.UTF_8)
