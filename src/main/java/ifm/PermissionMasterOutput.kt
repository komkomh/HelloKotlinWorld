package ifm

import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val INPUT_DIR = "/Users/curp043/Documents/work/release2019.12/マスタ/input"
private const val OUTPUT_DIR = "/Users/curp043/Documents/work/release2019.12/マスタ/output"
private const val MASKING = false
private const val OUTPUT_ENCODING = "UTF8" // "windows-31j" //

fun main() {
  val newPermissions = loadNewPermissions()
  val oldPermissions = loadOldPermissions()
//  oldPermissions.forEach { println(it) }

  val permissions = PermissionDto.merge(newPermissions, oldPermissions)
  permissions.forEach { println(it) }
  writePermissions(permissions)
}

private fun loadNewPermissions(): List<PermissionDto> {
  return File("$INPUT_DIR/【技術本部】在籍ﾃﾞｰﾀ191101.tsv").readLines(charset("UTF8"))
    .map { HankakuToZenkaku.hanToZen(it) }
    .map { it.replace("  ", " ") }
    .map { PermissionDto.createNewPermission(it) }
}

private fun loadOldPermissions(): List<PermissionDto> {
  return File("$INPUT_DIR/users_permissions.csv").readLines(charset("UTF8"))
    .filterNot { it.startsWith("id") }
    .map { PermissionDto.createOldPermission(it) }
}

private fun writePermissions(permissions: List<PermissionDto>) {
  val destFile = File("${OUTPUT_DIR}/users_permissions.csv")
  OutputStreamWriter(FileOutputStream(destFile), charset(OUTPUT_ENCODING)).use {
    permissions.forEachIndexed { index, permissionDto -> it.write("${index + 1}\t${permissionDto.output()}") }
  }
}

private data class PermissionDto(
  val employeeNumber: String,
  val permission: Int,
  val usageStatus: Int,
  val remarks: String?,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime
) {
  companion object {
    fun createOldPermission(tsvLine: String): PermissionDto {
      val tsv = tsvLine.split("\t")
      val employeeNumber = tsv[1]
      val permission = tsv[2].toInt()
      val usageStatus = tsv[3].toInt()
      val remarks = tsv[4]
      val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
      val createdAt = LocalDateTime.parse(tsv[5], formatter)
      val updatedAt = LocalDateTime.parse(tsv[6], formatter)
      return PermissionDto(
        employeeNumber,
        permission, // 権限
        usageStatus, // 利用中
        remarks,
        createdAt,
        updatedAt
      )
    }

    fun createNewPermission(tsvLine: String): PermissionDto {
      val tsv = tsvLine.split("\t")
      val employeeNumber = tsv[0]
      return PermissionDto(
        employeeNumber,
        1, // 権限
        1, // 利用中
        null,
        LocalDateTime.now(),
        LocalDateTime.now()
      )
    }

    fun merge(newPermissions: List<PermissionDto>, oldPermissions: List<PermissionDto>): List<PermissionDto> {
      val systemPermissions = oldPermissions.filter { it.isSystemUser() }
      val permissions = newPermissions.map { newPermission ->
        oldPermissions
          .find { oldPermission -> oldPermission.employeeNumber == newPermission.employeeNumber }
          ?: run { newPermission }
      }
      return systemPermissions + permissions
    }
  }

  fun isSystemUser(): Boolean = employeeNumber.startsWith("Z")

  fun output(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    return "${employeeNumber}\t" +
      "${permission}\t" +
      "${usageStatus}\t" +
      "\t" +
      "${createdAt.format(formatter)}\t" +
      "${updatedAt.format(formatter)}\n"
  }
}
