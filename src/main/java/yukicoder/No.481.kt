package yukicoder

fun main() {
    val b = readLine()!!.split(" ").map(String::toInt)
    for (i in (1..10)) {
        if (b.none { it == i }) {
            println(i)
        }
    }
}