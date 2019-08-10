package yukicoder


fun main() {
    val (h, w) = readLine()!!.split(" ").map(String::toInt)
    val blacks = (0 until h).flatMap { h2 ->
        readLine()!!
            .mapIndexed { index, c -> Pair(index, c) }
            .filter { it.second != '.' }
            .map { Pair(h2, it.first) }
    }

    if (blacks.isEmpty() || blacks.size % 2 != 0) {
        println("NO")
        return
    }

    fun canCopy(): Boolean {
        for (moveH in -h until h) {
            for (moveW in -w until w) {
                if (moveH == 0 && moveW == 0) {
                    continue
                }

                var tmpBlacks = blacks.toMutableSet()
                for (red in blacks) {
                    if (!tmpBlacks.contains(red)) {
                        continue
                    }
                    val blue = Pair(red.first + moveH, red.second + moveW)
                    if (!tmpBlacks.contains(blue)) {
                        break
                    }
                    tmpBlacks.remove(red)
                    tmpBlacks.remove(blue)
                }
                if (tmpBlacks.isEmpty()) {
                    return true
                }
            }
        }
        return false
    }

    when (canCopy()) {
        true -> println("YES")
        false -> println("NO")
    }
}
