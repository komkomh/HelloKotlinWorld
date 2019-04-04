package yukicoder

fun main() {
    val N: Int = readLine()!!.toInt()
    val V: List<Int> = readLine()!!.split(" ").map(String::toInt)
    println(getMaxOishisa(-2, V))
}

fun getMaxOishisa(i: Int, V: List<Int>): Int {
    if (i >= V.size) {
        return 0
    }
    val currentOishisa = if (i < 0) 0 else V[i]
    val oishisa1 = getMaxOishisa(i + 2, V)
    val oishisa2 = getMaxOishisa(i + 3, V)
    return when (oishisa1 > oishisa2) {
        true -> oishisa1 + currentOishisa
        false -> oishisa2 + currentOishisa
    }
}
