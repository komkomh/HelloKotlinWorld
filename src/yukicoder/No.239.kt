package yukicoder

fun main() {
    val N = readLine()!!.toInt()
    var A = mutableListOf<List<String>>()
    for (i in 0 until N) {
        A.add(readLine()!!.split(" "))
    }

    fun isRenchon(i: Int): Boolean {
        for (j in 0 until N) {
            if (i == j) {
                continue
            }
            if (A[j][i] != "nyanpass") {
                return false
            }
        }
        return true
    }

    var renChanIndex = -2
    for (i in 0 until N) {
        if (isRenchon(i)) {
            if (renChanIndex != -2) {
                println("-1")
                return
            }
            renChanIndex = i
        }
    }
    println(renChanIndex + 1)
}