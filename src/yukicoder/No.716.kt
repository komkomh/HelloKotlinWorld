package yukicoder

fun main() {
    val N = readLine()!!.toInt()
    val A = readLine()!!.split(" ").map(String::toInt)

    val min = (1 until A.size).map { A[it] - A[it - 1] }.min()
    val max = A.max()!! - A.min()!!

    println(min)
    println(max)
}