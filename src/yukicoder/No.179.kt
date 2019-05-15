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

    fun canCopy(redIndexes: IntArray, blueIndexes: List<Int>): Boolean {
        val firstRed = blacks[redIndexes[0]]
        val firstBlue = blacks[blueIndexes[0]]
        val diffH = firstRed.first - firstBlue.first
        val diffW = firstRed.second - firstBlue.second

        for (i in 1 until blueIndexes.size) {
            val red = blacks[redIndexes[i]]
            val blue = blacks[blueIndexes[i]]
            if (red.first - blue.first != diffH || red.second - blue.second != diffW) {
                return false
            }
        }
        return true
    }

    fun searchCopyPoint(vararg redIndexes: Int): Boolean {
//        redIndexes.forEach { print("${it},") }
//        println()
        val blueIndexes = (0 until redIndexes.size).filter { !redIndexes.contains(it) }
        if (blueIndexes.size > 1) {
            if (!canCopy(redIndexes, blueIndexes)) {
                blueIndexes.forEach { print("${it},") }
                print("| ${redIndexes.last()}")
                println()
                return false
            }
        }

        return when (redIndexes.size == blacks.size / 2) {
            true -> true
            false -> {
                val last = if (redIndexes.isEmpty()) 0 else redIndexes.last()
                ((last + 1) until blacks.size).map { searchCopyPoint(*redIndexes, it) }.any { it }
            }
        }
    }

    val start = Date().time
    when (searchCopyPoint()) {
        true -> {
            val end = Date().time
            println(end - start)
            println("YES")
        }
        false -> {
            val end = Date().time
            println(end - start)
            println("NO")
        }
    }
}
