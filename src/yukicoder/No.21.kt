package yukicoder

fun main() {
    val N = readLine()!!.toInt()
    val K = readLine()!!.toInt()
    val numbers: List<Int> = (0 until N).map { readLine()!!.toInt() }

    val max = numbers.max()!!
    val min = numbers.min()!!
    println(max - min)
}