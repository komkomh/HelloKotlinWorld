package yukicoder

fun main() {
    val (N, M) = readLine()!!.split(" ").map(String::toInt)
    val C = readLine()!!.split(" ").map(String::toInt)

    var boxNumber = 0
    var sumCandies = 0
    for (candyCount in C.sorted()) {
        sumCandies += candyCount
        if (sumCandies > M) {
            println(boxNumber)
            return
        }
        boxNumber++
    }
    println(boxNumber)
}
