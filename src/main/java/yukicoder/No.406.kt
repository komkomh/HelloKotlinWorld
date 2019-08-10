package yukicoder

fun main() {
    val N = readLine()!!.toInt()
    val X = readLine()!!.split(" ").map(String::toInt)

    if (X.size != X.toSet().size) {
        println("NO")
        return
    }

    val hatos = X.sorted()
    val diff = hatos[1] - hatos[0]
    for (index in 1 until hatos.size) {
        if (hatos[index] - hatos[index -1] != diff) {
            println("NO")
            return
        }
    }
    println("YES")
}
