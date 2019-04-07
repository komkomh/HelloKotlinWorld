package yukicoder

fun main() {
    val n: Int = readLine()!!.toInt()
    val w: List<Int> = readLine()!!.split(" ").map(String::toInt)

    val sum: Int = w.sum()
    if (sum % 2 != 0) {
        println("impossible")
        return
    }

    val halfW = sum / 2
    var cache: MutableMap<Pair<Int, Int>, Boolean> = mutableMapOf()
    fun search(index: Int, sum: Int): Boolean {
        val currentSum = sum + w[index]
        return when {
            currentSum > halfW -> false
            currentSum == halfW -> true
            else -> {
                for (i in index + 1 until n) {
                    val key = Pair(i, currentSum)
                    val value = { search(i, currentSum) }
                    if (cache.getOrPut(key, value)) {
                        return true
                    }
                }
                false
            }
        }
    }

    when (search(0, 0)) {
        true -> println("possible")
        false -> println("impossible")
    }
}
