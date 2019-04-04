fun main() {
    val N: Int = readLine()!!.toInt()
    var count: Int = 0
    var originalMaisu = N

    while (originalMaisu > 1) {
        originalMaisu = originalMaisu / 2 + originalMaisu % 2
        count++
    }
    println(count)
}