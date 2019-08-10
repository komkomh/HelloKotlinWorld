package yukicoder

fun main() {
    val (A, C) = readLine()!!.split(" ").map(String::toLong)
    val A2: String = A.toString(2)
    val C2: String = C.toString(2)

    val maxLength = when (A2.length > C2.length) {
        true -> A2.length
        false -> C2.length
    }

    val padA2 = A2.padStart(maxLength, '0')
    val padC2 = C2.padStart(maxLength, '0')

    val chars: CharArray = (0 until maxLength).map {
        val a = padA2[it]
        val c = padC2[it]
        when (a == c) {
            true -> '0'
            false -> '1'
        }
    }.toCharArray()

    val ans:Long = String(chars).toLong(2)
    println(ans)
}