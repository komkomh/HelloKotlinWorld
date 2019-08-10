package yukicoder

fun main() {
    val a = readLine()!!
    val b = readLine()!!

    val mapedA = a.groupBy { it }
    val mapedB = b.groupBy { it }
    when (mapedA == mapedB) {
        true -> println("YES")
        false -> println("NO")
    }
}