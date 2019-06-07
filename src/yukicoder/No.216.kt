package yukicoder

fun main() {
    val N = readLine()!!.toInt()
    val A = readLine()!!.split(" ").map(String::toInt)
    val B = readLine()!!.split(" ").map(String::toInt)

    val userScores: List<Pair<Int, Int>> = (0 until N)
        .map { Pair(B[it], A[it]) }
        .groupBy { it.first }
        .map { Pair(it.key, it.value.sumBy { pair -> pair.second }) }

    val maxUserScore: Int = userScores.filter { it.first != 0 }.maxBy { it.second }?.let { it.second } ?: run { 0 }
    val notSolvedScore = userScores.find { it.first == 0 }?.let { it.second } ?: run { 0 }
    val ans = when (maxUserScore > notSolvedScore) {
        true -> "NO"
        false -> "YES"
    }
    println(ans)
}
