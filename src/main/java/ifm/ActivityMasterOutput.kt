package ifm

import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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
    val filePath = "/Users/curp043/Documents/work/release2019.09/IFM最新マスタ一式20190906/活動情報.csv"
    val lines = File(filePath).readLines(charset("windows-31j"))
    return lines.filterNot { it.startsWith("活動") }
        .filterNot { it.startsWith("ACTY_") }
        .filter {
            val now = LocalDate.now()
            val date = LocalDate.parse(it.split(",")[7], DateTimeFormatter.ofPattern("yyyy/MM/dd"))
            println("now = ${now} || date = ${date}")
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
    val fileName =
        "/Users/curp043/Documents/work/release2019.09/IFM最新マスタ一式20190906/master_csv20190919/activities_large_classifications.csv"
    OutputStreamWriter(FileOutputStream(fileName), charset("windows-31j")).use {
        largeActivities.forEachIndexed { index, dto ->
            it.write("${index + 1}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000\n")
        }
    }
//    val pw = PrintWriter(BufferedWriter(FileWriter(fileName)))
//    largeActivities.forEachIndexed { index, dto ->
//        pw.println("${index + 1}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000")
//    }
//    pw.close()
}

fun writeMediumActivities(mediumActivities: Set<MediumActivityDto>) {
    val fileName =
        "/Users/curp043/Documents/work/release2019.09/IFM最新マスタ一式20190906/master_csv20190919/activities_medium_classifications.csv"
    OutputStreamWriter(FileOutputStream(fileName), charset("windows-31j")).use {
        mediumActivities.forEachIndexed { index, dto ->
            it.write("${index + 1}\t${dto.largeId}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000\n")
        }
    }
//    val pw = PrintWriter(BufferedWriter(FileWriter(fileName)))
//    mediumActivities.forEachIndexed { index, dto ->
//        pw.println("${index + 1}\t${dto.largeId}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000")
//    }
//    pw.close()
}

fun writeSmallActivities(smallActivities: Set<SmallActivityDto>) {
    val fileName =
        "/Users/curp043/Documents/work/release2019.09/IFM最新マスタ一式20190906/master_csv20190919/activities_small_classifications.csv"
    OutputStreamWriter(FileOutputStream(fileName), charset("windows-31j")).use {
        smallActivities.forEachIndexed { index, dto ->
            it.write("${index + 1}\t${dto.mediumId}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000\n")
        }
    }
//    val pw = PrintWriter(BufferedWriter(FileWriter(fileName)))
//    smallActivities.forEachIndexed { index, dto ->
//        pw.println("${index + 1}\t${dto.mediumId}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000")
//    }
//    pw.close()
}

fun writeActivities(activities: Set<ActivityDto>) {
    val fileName = "/Users/curp043/Documents/work/release2019.09/IFM最新マスタ一式20190906/master_csv20190919/activities.csv"
    OutputStreamWriter(FileOutputStream(fileName), charset("windows-31j")).use {
        activities.forEachIndexed { index, activityDto ->
            it.write("${index + 1}\t${activityDto.smallId}\t${activityDto.code}\t${activityDto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000\n")
        }
    }
//    val pw = PrintWriter(BufferedWriter(FileWriter(fileName)))
//    activities.forEachIndexed { index, activityDto ->
//        pw.println("${index + 1}\t${activityDto.smallId}\t${activityDto.code}\t${activityDto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000")
//    }
//    pw.close()
}

data class LargeActivityDto(val code: String, val name: String) {
    companion object {
        fun create(csvLine: String): LargeActivityDto {
            val array = csvLine.split(",")[0].split(" ")
            return LargeActivityDto(array[0], array[1])
        }
    }
}

data class MediumActivityDto(val largeId: Int, val code: String, val name: String) {
    companion object {
        fun create(largeId: Int, csvLine: String): MediumActivityDto {
            val array = csvLine.split(",")[1].split(" ")
            return MediumActivityDto(largeId, array[0], array[1])
        }
    }
}

data class SmallActivityDto(val mediumId: Int, val code: String, val name: String) {
    companion object {
        fun create(mediumId: Int, csvLine: String): SmallActivityDto {
            val array = csvLine.split(",")[2].split(" ")
            return SmallActivityDto(mediumId, array[0], array[1])
        }
    }
}

data class ActivityDto(val smallId: Int, val code: String, val name: String) {
    companion object {
        fun create(smallId: Int, csvLine: String): ActivityDto {
            val array = csvLine.split(",")[3].split(" ")
            return ActivityDto(smallId, array[0], array[1])
        }
    }
}