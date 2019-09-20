package ifm

import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

const val INPUT_DIR = "/Users/curp043/Documents/work/release2019.09/IFM最新マスタ一式20190906"
const val OUTPUT_DIR = "/Users/curp043/Documents/work/release2019.09/IFM最新マスタ一式20190906/master_csv20190920"
const val MASKING = false
const val OUTPUT_ENCODING = "windows-31j"

fun main() {
    val csvLines = readCsvLines()
    val largeActivities = loadLargeActivities(csvLines)
    val mediumActivities = loadMediumActivities(csvLines, largeActivities)
    val smallActivities = loadSmallActivities(csvLines, mediumActivities)
    val activities = loadActivities(csvLines, smallActivities)

    writeLargeActivities(largeActivities)
    writeMediumActivities(mediumActivities)
    writeSmallActivities(smallActivities)
    writeActivities(activities)
}

private fun readCsvLines(): List<String> {
    val lines = File("$INPUT_DIR/活動情報.csv").readLines(charset("windows-31j"))
    return lines.filterNot { it.startsWith("活動") }
        .filterNot { it.startsWith("ACTY_") }
        .filter {
            val now = LocalDate.now()
            val date = LocalDate.parse(it.split(",")[7], DateTimeFormatter.ofPattern("yyyy/MM/dd"))
            now.isBefore(date)
        }
        .map { HankakuToZenkaku.hankakuKatakanaToZenkakuKatakana(it) }
        .map { it.replace("  ", " ") }
}

fun loadLargeActivities(csvLines: List<String>): Set<LargeActivityDto> =
    csvLines.map { LargeActivityDto.create(it) }.toSet()

fun loadMediumActivities(csvLines: List<String>, largeActivities: Set<LargeActivityDto>): Set<MediumActivityDto> =
    csvLines.map {
        val largeCode = it.split(",")[0].split(" ")[0]
        val largeId = largeActivities.indexOfFirst { it.code == largeCode } + 1
        MediumActivityDto.create(largeId, it)
    }.toSet()

fun loadSmallActivities(csvLines: List<String>, mediumActivities: Set<MediumActivityDto>): Set<SmallActivityDto> =
    csvLines.map {
        val mediumCode = it.split(",")[1].split(" ")[0]
        val mediumId = mediumActivities.indexOfFirst { it.code == mediumCode } + 1
        SmallActivityDto.create(mediumId, it)
    }.toSet()

fun loadActivities(csvLines: List<String>, smallActivities: Set<SmallActivityDto>): Set<ActivityDto> =
    csvLines.map {
        val smallCode = it.split(",")[2].split(" ")[0]
        val smallId = smallActivities.indexOfFirst { it.code == smallCode } + 1
        ActivityDto.create(smallId, it)
    }.toSet()

fun writeLargeActivities(largeActivities: Set<LargeActivityDto>) {
    val fileName = "$OUTPUT_DIR/activities_large_classifications.csv"
    OutputStreamWriter(FileOutputStream(fileName), charset(OUTPUT_ENCODING)).use {
        largeActivities.forEachIndexed { index, dto ->
            it.write("${index + 1}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000\n")
        }
    }
}

fun writeMediumActivities(mediumActivities: Set<MediumActivityDto>) {
    val fileName = "$OUTPUT_DIR/activities_medium_classifications.csv"
    OutputStreamWriter(FileOutputStream(fileName), charset(OUTPUT_ENCODING)).use {
        mediumActivities.forEachIndexed { index, dto ->
            it.write("${index + 1}\t${dto.largeId}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000\n")
        }
    }
}

fun writeSmallActivities(smallActivities: Set<SmallActivityDto>) {
    val fileName = "$OUTPUT_DIR/activities_small_classifications.csv"
    OutputStreamWriter(FileOutputStream(fileName), charset(OUTPUT_ENCODING)).use {
        smallActivities.forEachIndexed { index, dto ->
            it.write("${index + 1}\t${dto.mediumId}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000\n")
        }
    }
}

fun writeActivities(activities: Set<ActivityDto>) {
    val fileName = "$OUTPUT_DIR/activities.csv"
    OutputStreamWriter(FileOutputStream(fileName), charset(OUTPUT_ENCODING)).use {
        activities.forEachIndexed { index, activityDto ->
            it.write("${index + 1}\t${activityDto.smallId}\t${activityDto.code}\t${activityDto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000\n")
        }
    }
}

fun mask(original: String): String = original.mapIndexed { index, c ->
    when (MASKING && index >= 3) {
        true -> '*'
        false -> c
    }
}.joinToString("")

data class LargeActivityDto(val code: String, val name: String) {
    companion object {
        fun create(csvLine: String): LargeActivityDto {
            val array = csvLine.split(",")[0].split(" ")
            return LargeActivityDto(array[0], mask(array[1]))
        }
    }
}

data class MediumActivityDto(val largeId: Int, val code: String, val name: String) {
    companion object {
        fun create(largeId: Int, csvLine: String): MediumActivityDto {
            val array = csvLine.split(",")[1].split(" ")
            return MediumActivityDto(largeId, array[0], mask(array[1]))
        }
    }
}

data class SmallActivityDto(val mediumId: Int, val code: String, val name: String) {
    companion object {
        fun create(mediumId: Int, csvLine: String): SmallActivityDto {
            val array = csvLine.split(",")[2].split(" ")
            return SmallActivityDto(mediumId, array[0], mask(array[1]))
        }
    }
}

data class ActivityDto(val smallId: Int, val code: String, val name: String) {
    companion object {
        fun create(smallId: Int, csvLine: String): ActivityDto {
            val array = csvLine.split(",")[3].split(" ")
            return ActivityDto(smallId, array[0], mask(array[1]))
        }
    }
}