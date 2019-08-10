package yukicoder

fun main() {
    val (n, h, m, t) = readLine()!!.split(" ").map(String::toInt)

    val upM = (n - 1) * t + m
    val upH = upM / 60 + h
    println(upH % 24)
    println(upM % 60)
}