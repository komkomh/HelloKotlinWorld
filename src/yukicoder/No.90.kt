package yukicoder

fun main() {
    val (n, m) = readLine()!!.split(" ").map(String::toInt)
    val scores = (0 until m).map {
        val (item1, item2, score) = readLine()!!.split(" ").map(String::toInt)
        Triple(item1, item2, score)
    }

    fun getScore(items: IntArray): Int {
        val itemMap: Map<Int, Int> = items.mapIndexed { index, i -> i to index }.toMap()
        return scores.filter { itemMap.get(it.first)!! < itemMap.get(it.second)!! }.sumBy { it.third }
    }

    fun getMaxScore(vararg items: Int): Int = when (items.size >= n) {
        true -> getScore(items)
        false -> (0 until n).filter { !items.contains(it) }.map { getMaxScore(*items, it) }.max()!!
    }
    println(getMaxScore())
}