package ifm

import java.io.*
import java.nio.charset.Charset

// [0]勘定科目(大分類) = 1 資産の部
// [1]勘定科目(中分類) = 11 流動資産
// [2]勘定科目(小分類) = 1101 現金
// [3]勘定科目(内訳) = 110101 両替資金
// [4]勘定科目別名(内訳) = 現金_両替資金
// [5]取引明細 = 001 流動資産_現金_両替資金_両替資金
// [6]取引明細別名 = 現金_両替資金_流動資産_現金_両替資金_両替資金
// [7]取引明細略名 = 両替資金
// [8]予算画面区分 = 00
// [9]画面表示基準単位 = 1000 上記以外
// [10]有効ﾌﾗｸﾞ(1) = Y
// [11]当初有効日 = 1950/01/01
// [12]有効終了日 = 2049/12/31
fun main() {
    val csvLines = readCsvLines()
    val largeAccounts = loadLargeAccounts(csvLines);
    val mediumAccounts = loadMediumAccounts(csvLines, largeAccounts);
    val smallAccounts = loadSmallAccounts(csvLines, mediumAccounts);
    val accounts = loadAccounts(csvLines, smallAccounts);
    val businessDetails = loadBusinessDetails(csvLines, accounts);

    writeLargeAccounts(largeAccounts)
    writeMediumAccounts(mediumAccounts)
    writeSmallAccounts(smallAccounts)
    writeAccounts(accounts)
    writeBusinessDetails(businessDetails)
}

private fun readCsvLines(): List<String> {
    val filePath = "/Users/curp043/Documents/work/release2019.09/IFM最新マスタ一式20190906/勘定科目情報.csv"
    val lines = File(filePath).readLines(charset("windows-31j"));
    return lines.filterNot { it.startsWith("勘定科目") }
        .filterNot { it.startsWith("MJRGRP_ACCTS") }
        .map { HankakuToZenkaku.hankakuKatakanaToZenkakuKatakana(it) }
}

fun loadLargeAccounts(csvLines: List<String>): Set<LargeAccountDto> =
    csvLines.map { LargeAccountDto.create(it) }.toSet()

fun loadMediumAccounts(csvLines: List<String>, largeAccounts: Set<LargeAccountDto>): Set<MediumAccountDto> =
    csvLines.map {
        val largeCode = it.split(",")[0].split(" ")[0]
        val largeId = largeAccounts.indexOfFirst { it.code == largeCode } + 1
        MediumAccountDto.create(largeId, it)
    }.toSet()

fun loadSmallAccounts(csvLines: List<String>, mediumAccounts: Set<MediumAccountDto>): Set<SmallAccountDto> =
    csvLines.map {
        val mediumCode = it.split(",")[1].split(" ")[0]
        val mediumId = mediumAccounts.indexOfFirst { it.code == mediumCode } + 1
        SmallAccountDto.create(mediumId, it)
    }.toSet()

fun loadAccounts(csvLines: List<String>, smallAccounts: Set<SmallAccountDto>): Set<AccountDto> =
    csvLines.map {
        val smallCode = it.split(",")[2].split(" ")[0]
        val smallId = smallAccounts.indexOfFirst { it.code == smallCode } + 1
        AccountDto.create(smallId, it)
    }.toSet()

fun loadBusinessDetails(csvLines: List<String>, accounts: Set<AccountDto>): Set<BusinessDetailDto> =
    csvLines.map {
        val accountCode = it.split(",")[3].split(" ")[0]
        val accountId = accounts.indexOfFirst { it.code == accountCode } + 1
        BusinessDetailDto.create(accountId, it)
    }.toSet()

fun writeLargeAccounts(largeAccounts: Set<LargeAccountDto>) {
    val fileName =
        "/Users/curp043/Documents/work/release2019.09/IFM最新マスタ一式20190906/master_csv20190919/accounts_large_classifications.csv"
    OutputStreamWriter(FileOutputStream(fileName), charset("windows-31j")).use {
        largeAccounts.forEachIndexed { index, dto ->
            it.write("${index + 1}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000\n")
        }
    }
//    val pw = PrintWriter(BufferedWriter(FileWriter(fileName)))
//    largeAccounts.forEachIndexed { index, dto ->
//        pw.println("${index + 1}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000")
//    }
//    pw.close()
}

fun writeMediumAccounts(mediumAccounts: Set<MediumAccountDto>) {
    val fileName =
        "/Users/curp043/Documents/work/release2019.09/IFM最新マスタ一式20190906/master_csv20190919/accounts_medium_classifications.csv"
    OutputStreamWriter(FileOutputStream(fileName), charset("windows-31j")).use {
        mediumAccounts.forEachIndexed { index, dto ->
            it.write("${index + 1}\t${dto.largeId}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000\n")
        }
    }
//    val pw = PrintWriter(BufferedWriter(FileWriter(fileName)))
//    mediumAccounts.forEachIndexed { index, dto ->
//        pw.println("${index + 1}\t${dto.largeId}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000")
//    }
//    pw.close()
}

fun writeSmallAccounts(smallAccounts: Set<SmallAccountDto>) {
    val fileName =
        "/Users/curp043/Documents/work/release2019.09/IFM最新マスタ一式20190906/master_csv20190919/accounts_small_classifications.csv"
    OutputStreamWriter(FileOutputStream(fileName), charset("windows-31j")).use {
        smallAccounts.forEachIndexed { index, dto ->
            it.write("${index + 1}\t${dto.mediumId}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000\n")
        }
    }
//    val pw = PrintWriter(BufferedWriter(FileWriter(fileName)))
//    smallAccounts.forEachIndexed { index, dto ->
//        pw.println("${index + 1}\t${dto.mediumId}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000")
//    }
//    pw.close()
}

fun writeAccounts(accounts: Set<AccountDto>) {
    val fileName = "/Users/curp043/Documents/work/release2019.09/IFM最新マスタ一式20190906/master_csv20190919/accounts.csv"
    OutputStreamWriter(FileOutputStream(fileName), charset("windows-31j")).use {
        accounts.forEachIndexed { index, dto ->
            it.write("${index + 1}\t${dto.smallId}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000\n")
        }
    }
//    val pw = PrintWriter(BufferedWriter(FileWriter(fileName)))
//    accounts.forEachIndexed { index, dto ->
//        pw.println("${index + 1}\t${dto.smallId}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000")
//    }
//    pw.close()
}

fun writeBusinessDetails(businessDetails: Set<BusinessDetailDto>) {
    val fileName =
        "/Users/curp043/Documents/work/release2019.09/IFM最新マスタ一式20190906/master_csv20190919/business_details.csv"
    OutputStreamWriter(FileOutputStream(fileName), charset("windows-31j")).use {
        businessDetails.forEachIndexed { index, dto ->
            it.write("${index + 1}\t${dto.accountId}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000\n")
        }
    }
//    val fil = FileWriter(fileName)
//    val pw = PrintWriter(BufferedWriter(fil))
//    businessDetails.forEachIndexed { index, dto ->
//        pw.println("${index + 1}\t${dto.accountId}\t${dto.code}\t${dto.name}\t0\t2019-03-01 10:00:00.000\t2019-03-01 10:00:00.000")
//    }
//    pw.close()
}

data class LargeAccountDto(val code: String, val name: String) {
    companion object {
        fun create(csvLine: String): LargeAccountDto {
            val array = csvLine.split(",")[0].split(" ")
            return LargeAccountDto(array[0], array[1])
        }
    }
}

data class MediumAccountDto(val largeId: Int, val code: String, val name: String) {
    companion object {
        fun create(largeId: Int, csvLine: String): MediumAccountDto {
            val array = csvLine.split(",")[1].split(" ")
            return MediumAccountDto(largeId, array[0], array[1])
        }
    }
}

data class SmallAccountDto(val mediumId: Int, val code: String, val name: String) {
    companion object {
        fun create(mediumId: Int, csvLine: String): SmallAccountDto {
            val array = csvLine.split(",")[2].split(" ")
            return SmallAccountDto(mediumId, array[0], array[1])
        }
    }
}

data class AccountDto(val smallId: Int, val code: String, val name: String) {
    companion object {
        fun create(smallId: Int, csvLine: String): AccountDto {
            val array = csvLine.split(",")[3].split(" ")
            return AccountDto(smallId, array[0], array[1])
        }
    }
}

data class BusinessDetailDto(val accountId: Int, val code: String, val name: String) {
    companion object {
        fun create(accountId: Int, csvLine: String): BusinessDetailDto {
            val array = csvLine.split(",")[5].split(" ")
            return BusinessDetailDto(accountId, array[0], array[1])
        }
    }
}