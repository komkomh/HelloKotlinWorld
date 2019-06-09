package yukicoder

fun main() {
    val S = readLine()!!.split(" ").map(String::toFloat)

    val ans = (S.sum()!! - S.min()!! - S.max()!!) / 4.0
    println("%.2f".format(ans))
}
