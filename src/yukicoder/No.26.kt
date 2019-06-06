package yukicoder

fun main() {
    val N = readLine()!!.toInt()
    val M = readLine()!!.toInt()
    val P: List<Pair<Int, Int>> = (0 until M).map {
        val array = readLine()!!.split(" ").map(String::toInt)
        Pair(array[0], array[1])
    }

    var ans = N
    for (p in P) {
        when (ans) {
            p.first -> ans = p.second
            p.second -> ans = p.first
        }
    }
    println(ans)
}