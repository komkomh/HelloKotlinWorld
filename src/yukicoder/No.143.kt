package yukicoder

fun main() {
    val (K, N, F) = readLine()!!.split(" ").map(String::toInt)
    val A: List<Int> = readLine()!!.split(" ").map(String::toInt)

    val mameAmount = K * N
    val tabeAmount = A.sum()
    val result = if (mameAmount >= tabeAmount) {
        mameAmount - tabeAmount
    } else {
        -1
    }
    println(result)
}