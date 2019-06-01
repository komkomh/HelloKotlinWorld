package yukicoder

fun main() {
    val N = readLine()!!.toInt()
    val A: List<Int> = readLine()!!.split(" ").map(String::toInt)

    var aMap = mutableMapOf<Int, Int>()
    for (a in A) {
        aMap.get(a)?.also {
            aMap.set(a, it + 1)
        } ?: run {
            aMap.set(a, 0)
        }
    }
    println(aMap.count { it.value == 0 })
}
