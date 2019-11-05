package ifm

import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val INPUT_DIR = "/Users/curp043/Documents/work/release2019.09/マスタ/input"
private const val OUTPUT_DIR = "/Users/curp043/Documents/work/release2019.09/マスタ/output"
private const val MASKING = false
private const val OUTPUT_ENCODING = "windows-31j"

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
    val lines = File("$INPUT_DIR/活動マスタ.csv").readLines(charset("UTF8"))
    return lines.filterNot { it.contains("大分類コード,") }
        .filter {
            val now = LocalDate.now()
            val date = LocalDate.parse(it.split(",")[11], DateTimeFormatter.ofPattern("yyyy/MM/dd"))
            now.isBefore(date)
        }
        .map { HankakuToZenkaku.hankakuKatakanaToZenkakuKatakana(it) }
        .map { it.replace("  ", " ") }
}

private fun loadLargeActivities(csvLines: List<String>): Set<LargeActivityDto> =
    csvLines.map { LargeActivityDto.create(it) }.toSet()

private fun loadMediumActivities(csvLines: List<String>, largeActivities: Set<LargeActivityDto>): Set<MediumActivityDto> =
    csvLines.map { MediumActivityDto.create(it, largeActivities) }.toSet()

private fun loadSmallActivities(csvLines: List<String>, mediumActivities: Set<MediumActivityDto>): Set<SmallActivityDto> =
    csvLines.map { SmallActivityDto.create(it, mediumActivities) }.toSet()

private fun loadActivities(csvLines: List<String>, smallActivities: Set<SmallActivityDto>): Set<ActivityDto> =
    csvLines.map { ActivityDto.create(it, smallActivities) }.toSet()

private fun writeLargeActivities(largeActivities: Set<LargeActivityDto>) {
    val fileName = "$OUTPUT_DIR/activities_large_classifications.csv"
    OutputStreamWriter(FileOutputStream(fileName), charset(OUTPUT_ENCODING)).use {
        largeActivities.forEachIndexed { index, dto ->
            it.write("${index + 1}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000\n")
        }
    }
}

private fun writeMediumActivities(mediumActivities: Set<MediumActivityDto>) {
    val fileName = "$OUTPUT_DIR/activities_medium_classifications.csv"
    OutputStreamWriter(FileOutputStream(fileName), charset(OUTPUT_ENCODING)).use {
        mediumActivities.forEachIndexed { index, dto ->
            it.write("${index + 1}\t${dto.largeId}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000\n")
        }
    }
}

private fun writeSmallActivities(smallActivities: Set<SmallActivityDto>) {
    val fileName = "$OUTPUT_DIR/activities_small_classifications.csv"
    OutputStreamWriter(FileOutputStream(fileName), charset(OUTPUT_ENCODING)).use {
        smallActivities.forEachIndexed { index, dto ->
            it.write("${index + 1}\t${dto.mediumId}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000\n")
        }
    }
}

private fun writeActivities(activities: Set<ActivityDto>) {
    val fileName = "$OUTPUT_DIR/activities.csv"
    OutputStreamWriter(FileOutputStream(fileName), charset(OUTPUT_ENCODING)).use {
        activities.forEachIndexed { index, activityDto ->
            it.write("${index + 1}\t${activityDto.smallId}\t${activityDto.code}\t${activityDto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000\n")
        }
    }
}

private fun mask(original: String): String = original.mapIndexed { index, c ->
  when (MASKING && index >= 3) {
    true -> '*'
    false -> c
  }
}.joinToString("")

private data class LargeActivityDto(val code: String, val name: String) {
    companion object {
        fun create(csvLine: String): LargeActivityDto {
            val csv = csvLine.split(",")
            return LargeActivityDto(csv[0], mask(csv[1]))
        }
    }
}

private data class MediumActivityDto(val largeId: Int, val code: String, val name: String) {
    companion object {
        fun create(csvLine: String, largeActivities: Set<LargeActivityDto>): MediumActivityDto {
            val csv = csvLine.split(",")
            val largeCode = csv[0]
            val largeId = largeActivities.indexOfFirst { it.code == largeCode } + 1
            return MediumActivityDto(largeId, csv[2], mask(csv[3]))
        }

    }
}

private data class SmallActivityDto(val mediumId: Int, val code: String, val name: String) {
    companion object {
        fun create(csvLine: String, mediumActivities: Set<MediumActivityDto>): SmallActivityDto {
            val csv = csvLine.split(",")
            val mediumCode = csv[2]
            val mediumId = mediumActivities.indexOfFirst { it.code == mediumCode } + 1
            return SmallActivityDto(mediumId, csv[4], mask(csv[5]))
        }
    }
}

private data class ActivityDto(val smallId: Int, val code: String, val name: String) {
    companion object {
        fun create(csvLine: String, smallActivities: Set<SmallActivityDto>): ActivityDto {
            val csv = csvLine.split(",")
            val smallCode = csv[4]
            val smallId = smallActivities.indexOfFirst { it.code == smallCode } + 1
            return ActivityDto(smallId, csv[6], mask(csv[7]))
        }
    }
}
