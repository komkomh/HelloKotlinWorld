package yukicoder

fun main() {
    val S = readLine()!!

    val count = S.replace("-", "").chunked(3).filter { it == "min" }.count()
    println(count)
}
