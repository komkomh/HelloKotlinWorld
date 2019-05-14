package yukicoder

import java.util.*
import javax.swing.text.html.HTML.Attribute.N


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

    val redSize = blacks.size / 2
    val start = Date().time

    bitLoop@ for (bit in (1 shl redSize) - 1 until (1 shl blacks.size)) {
        var strBit = bit.toString(2)
        if (strBit.count { it == '1' } != redSize) {
            continue
        }
        strBit = strBit.padStart(blacks.size, '0')

//        println(strBit.mapIndexed { index, c -> Pair(index, c) })
        val reds = strBit.mapIndexed { index, c -> Pair(index, c) }.filter { it.second == '1' }.map { blacks[it.first] }
//        val reds = strBit.filter { it == '1' }.mapIndexed { index, c -> blacks[index] }
        val blues = strBit.mapIndexed { index, c -> Pair(index, c) }.filter { it.second == '0' }.map { blacks[it.first] }
//        val blues = strBit.filter { it == '0' }.mapIndexed { index, c -> blacks[index] }
        println("${reds}|${blues}")
        val diffH = reds[0].first - blues[0].first
        val diffW = reds[0].second - blues[0].second

        for (i in 0 until reds.size) {
            if (diffH != (reds[i].first - blues[i].first)) {
                continue@bitLoop
            }
            if (diffW != (reds[i].second - blues[i].second)) {
                continue@bitLoop
            }
        }
        println("YES")
        return
    }

    val end = Date().time
    println("--> ${end - start}")
    println("NO")

//    when (canPaint()) {
//        true -> println("YES")
//        false -> println("NO")
//    }
}