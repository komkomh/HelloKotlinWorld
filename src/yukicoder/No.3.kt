package yukicoder

fun main() {
    val n: Int = readLine()!!.toInt()
    val sugoroku: List<Int> = (1..n).map { getBit2(it) }
    val queue: MutableList<Pair<Int, Int>> = mutableListOf(Pair(0, 0))
    val cache: MutableSet<Int> = mutableSetOf(0)

    while (queue.isNotEmpty()) {
        val pair = queue.removeAt(0)
        if (pair.first == n - 1) {
            println(pair.second + 1)
            return
        }

        val plusIndex = pair.first + sugoroku[pair.first]
        if (plusIndex < n && cache.add(plusIndex)) {
            queue.add(Pair(plusIndex, pair.second + 1))
        }

        val minusIndex = pair.first - sugoroku[pair.first]
        if (minusIndex > 0 && cache.add(minusIndex)) {
            queue.add(Pair(minusIndex, pair.second + 1))
        }
    }
    println(-1)
}

fun getBit2(number: Int): Int = Integer.toString(number, 2).count { num -> num == '1' }