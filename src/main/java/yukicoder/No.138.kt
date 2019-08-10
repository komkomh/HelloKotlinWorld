package yukicoder

fun main() {
    val kasekiVersion = readLine()!!.split(".").map(String::toInt)
    val checkVersion = readLine()!!.split(".").map(String::toInt)

    for (i in 0 until 3) {
        if (checkVersion[i] < kasekiVersion[i]) {
            println("YES")
            return
        } else if (checkVersion[i] > kasekiVersion[i]){
            println("NO")
            return
        }
    }
    println("YES")
}