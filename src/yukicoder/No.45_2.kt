package yukicoder

fun main() {
    val N: Int = readLine()!!.toInt()
    var V: IntArray = readLine()!!.split(" ").map(String::toInt).toIntArray()

    fun getMinusValue(targetIndex: Int): Int = if (targetIndex < 0) 0 else V[targetIndex]

    for (i in (0 until V.size)) {
        val minus2 = getMinusValue(i - 2)
        val minus3 = getMinusValue(i - 3)
        V[i] += if (minus2 > minus3) minus2 else minus3
    }
    println(V.max())
}
