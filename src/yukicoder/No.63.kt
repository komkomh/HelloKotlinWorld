package yukicoder

fun main() {
    val (l, k) = readLine()!!.split(" ").map(String::toInt)
    val forward = k * 2
    val count = l / forward
    var nonTouchCount = if (l % forward == 0) count - 1 else count
    println(k * nonTouchCount)
}