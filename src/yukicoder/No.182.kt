package yukicoder

fun main() {
    val N = readLine()!!.toInt()
    val A: List<Int> = readLine()!!.split(" ").map(String::toInt)

    var aMap = mutableMapOf<Int, Int>()
    A.forEach { a ->
        aMap[a]?.also { aMap[a] = it + 1 } ?: run { aMap[a] = 0 }
    }
    println(aMap.count { it.value == 0 })
}
