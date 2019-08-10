package yukicoder

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun main() {
    val S = readLine()!!

    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val date = LocalDate.parse(S, formatter)
    val ans = date.plusDays(2).format(formatter)
    println(ans)
}
