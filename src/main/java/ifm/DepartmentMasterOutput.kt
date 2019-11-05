package ifm

import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val INPUT_DIR = "/Users/curp043/Documents/work/release/マスタ/input"
private const val OUTPUT_DIR = "/Users/curp043/Documents/work/release/マスタ/output"
private const val MASKING = false
private const val OUTPUT_ENCODING = "windows-31j"
private const val DEPARTMENT_START_INDEX = 108
private const val GROUP_START_INDEX = 546

fun main() {
  val csvLines = readCsvLines()
//  csvLines.forEach { println(it) }
  val departments = loadDepartments(csvLines)
//  departments.forEach { println(it) }
  val groups = loadGroups(csvLines, departments)
//  groups.forEach { println(it) }
  writeDepartments(departments)
  writeGroups(groups)
}

private val TargetDepartments = setOf<String>(

)

private fun readCsvLines(): List<String> {
  val lines = File("$INPUT_DIR/組織情報20191105.csv").readLines(charset("windows-31j"))
  return lines.filterNot { it.contains("ﾛｹｰｼｮﾝ(部･室)") } // 先頭行を取り込みから除外
    .filterNot { it.contains("DIV_DEPT_LCTN_CD,") } // 先頭2行目を取り込みから除外
    .filter {
      // 過去日付けのものは除外
      val now = LocalDate.now()
      val date = LocalDate.parse(it.split(",")[24], DateTimeFormatter.ofPattern("yyyy/M/d"))
      now.isBefore(date)
    }
    .filter { TargetDepartments.contains(it.split(",")[0]) }
    .map { HankakuToZenkaku.hankakuKatakanaToZenkakuKatakana(it) }
    .map { it.replace("  ", " ") }
}

private fun loadDepartments(csvLines: List<String>): Set<DepartmentDto> {
  return csvLines.map { DepartmentDto.create(it) }
    .sortedBy { d -> TargetDepartments.indexOfFirst { it.startsWith(d.code) } }
    .toSet()
}

private fun loadGroups(csvLines: List<String>, departments: Set<DepartmentDto>): Set<GroupDto> {
  return csvLines.map { GroupDto.create(it, departments) }.toSet()
}

private fun writeDepartments(departments: Set<DepartmentDto>) {
  val srcFile = File("${INPUT_DIR}/departments.csv")
  val destFile = File("${OUTPUT_DIR}/departments.csv")
  srcFile.copyTo(destFile, overwrite = true)
  OutputStreamWriter(FileOutputStream(destFile, true), charset(OUTPUT_ENCODING)).use {
    departments.forEachIndexed { index, dto ->
      it.write("${index + DEPARTMENT_START_INDEX}\t${dto.code}\t${dto.name}\t0\t${now()}\t${now()}\n")
    }
  }
}

private fun writeGroups(groups: Set<GroupDto>) {
  val srcFile = File("${INPUT_DIR}/departments_groups.csv")
  val destFile = File("${OUTPUT_DIR}/departments_groups.csv")
  srcFile.copyTo(destFile, overwrite = true)
  OutputStreamWriter(FileOutputStream(destFile, true), charset(OUTPUT_ENCODING)).use {
    groups.forEachIndexed { index, dto ->
      it.write("${index + GROUP_START_INDEX}\t${dto.departmentId}\t${dto.code}\t${dto.name}\t0\t${now()}\t${now()}\n")
    }
  }
}

private fun mask(original: String): String = original.mapIndexed { index, c ->
  when (MASKING && index >= 3) {
    true -> '*'
    false -> c
  }
}.joinToString("")

private fun now(): String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
private data class DepartmentDto(val code: String, val name: String) {
  companion object {
    fun create(csvLine: String): DepartmentDto {
      val csv = csvLine.split(",")
      val code = csv[0].split(" ")[0]
      val name = csv[0].split(" ")[1]
      return DepartmentDto(code, mask(name))
    }
  }
}

private data class GroupDto(val departmentId: Int, val code: String, val name: String) {
  companion object {
    fun create(csvLine: String, departments: Set<DepartmentDto>): GroupDto {
      val csv = csvLine.split(",")
      val departmentCode = csv[0].split(" ")[0]
      val departmentId = departments.indexOfFirst { it.code == departmentCode } + DEPARTMENT_START_INDEX
      val code = csv[1].split(" ")[0]
      val name = csv[1].split(" ")[1]
      return GroupDto(departmentId, code, mask(name))
    }
  }
}
