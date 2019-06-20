package yukicoder

fun main() {
    val C1: String = readLine()!!
    val C2: String = readLine()!!

    var C = mutableListOf<Char>()
    C.addAll(C1.toCharArray().toList())
    C.addAll(C2.toCharArray().toList())
    C.add('x')

    var count = 0
    var maxCount = 0
    for (i in 0 until C.size) {
        when (C[i]) {
            'o' -> count++
            'x' -> {
                if (maxCount < count) {
                    maxCount = count
                }
                count = 0
            }
        }
    }
    println(maxCount)
}