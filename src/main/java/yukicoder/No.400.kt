package yukicoder

fun main() {
    val S = readLine()!!

    val ans = S
        .replace('<', '_')
        .replace('>', '<')
        .replace('_', '>')
        .reversed()
    println(ans)
}