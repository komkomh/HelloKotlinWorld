package yukicoder

fun main() {
    val (d, p) = readLine()!!.split(" ").map(String::toInt)
    println(d * (100 + p) / 100)
}