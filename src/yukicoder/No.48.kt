package yukicoder

import kotlin.math.absoluteValue

fun main() {
    val x = readLine()!!.toInt()
    val y = readLine()!!.toInt()
    val l = readLine()!!.toInt()

    var count = 0 // 回数
    if (x != 0) count++
    if (y < 0) count ++
    if (y < 0 && x == 0) count++

    count += y.absoluteValue / l // 高速
    if (y.absoluteValue % l > 0) {
        count++ // 残り
    }
    count += x.absoluteValue / l // 高速
    if (x.absoluteValue % l > 0) {
        count++ // 残り
    }
    println(count)
}