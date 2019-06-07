package yukicoder

fun main() {
    var s = readLine()!!

    val bind: Int = 'Z' - 'A' + 1
    for (i in 0 until s.length) {
        val index = i % bind
        var c: Char = s[i] - index - 1
        while (c < 'A') {
            c += bind
        }
        print(c)
    }
    println()
}
