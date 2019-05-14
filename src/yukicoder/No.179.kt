package yukicoder

import java.util.*

fun main() {
    val (h, w) = readLine()!!.split(" ").map(String::toInt)
    val blacks = (0 until h).flatMap { h2 ->
        readLine()!!
            .mapIndexed { index, c -> Pair(index, c) }
            .filter { it.second != '.' }
            .map { Pair(h2, it.first) }
    }

    blacks.forEach { println(it) }
    println("----------")

    if (blacks.size % 2 != 0) {
        println("NO")
        return
    }

    fun isCopyAble(boolArray: BooleanArray): Boolean {
        var reds = mutableListOf<Pair<Int, Int>>()
        var blues = mutableListOf<Pair<Int, Int>>()
        boolArray.forEachIndexed { index, b ->
            if (b) reds.add(blacks[index])
            else blues.add(blacks[index])
        }

        val xx = mutableListOf(
            Pair(0, 0),
            Pair(0, 1),
            Pair(0, 2),
            Pair(1, 0),
            Pair(1, 2),
            Pair(1, 4),
            Pair(2, 0),
            Pair(2, 1),
            Pair(2, 2)
        )
        if (reds == xx) {
            println("kiteru!")
        }

        println("${reds}|${blues}")
        val diffH = reds[0].first - blues[0].first
        val diffW = reds[0].second - blues[0].second
        for (i in 0 until blacks.size / 2) {
            if (reds[i].first - blues[i].first != diffH) {
                return false
            }
            if (reds[i].second - blues[i].second != diffW) {
                return false
            }
        }
        return true
    }

    val redSize = blacks.size / 2
    val start = Date().time

    var boolArray: BooleanArray = BooleanArray(blacks.size)
    (0 until redSize).forEach { boolArray[it] = true }

    while (true) {
        for (i in 0 until blacks.size - 1) {
            if (boolArray[i] && !boolArray[i + 1]) {
                boolArray[i + 1] = true
                boolArray[i] = false
                if (isCopyAble(boolArray)) {
                    println("YES")
                    return
                }
            }
        }

        if (boolArray.indexOfFirst { it } >= redSize) {
            boolArray.forEach { print("${it},") }
            println()
            break
        }
    }
    val end = Date().time
    println(end - start)
    println("NO")

//    when (canPaint()) {
//        true -> println("YES")
//        false -> println("NO")
//    }
}