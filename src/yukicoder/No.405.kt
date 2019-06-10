package yukicoder

fun main() {
    val S1 = readLine()!!.split(" ")
    val S = S1[0]
    val T = S1[1].toInt()

    val roma12 = arrayOf("I","II","III","IIII","V","VI","VII","VIII","IX","X","XI","XII")
    var index: Int = roma12.mapIndexed { index, s -> Pair(index, s) }.filter { it.second == S }[0].first
    index += (T % 12)
    when {
        index > 11 -> index -= 12
        index < 0 -> index += 12
    }
    println(roma12[index])
}
