package yukicoder

fun main() {
    val l = readLine()!!.toInt()
    val n = readLine()!!.toInt()
    val w = readLine()!!.split(" ").map(String::toInt)

    fun getCount(): Int {
        var count = 0
        var sum = 0
        for (sotedW in w.sorted()) {
            sum += sotedW
            when (sum <= l) {
                true -> count++
                false -> return count
            }
        }
        return count
    }
    println(getCount())
}
