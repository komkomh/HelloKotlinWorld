package yukicoder

fun main() {
    val n = readLine()!!.toInt()

    val primeNumbers: List<Int> = (2..n).filter { isPrimeNumber(it) }
    val cache: MutableMap<Int, Boolean> = mutableMapOf()

    fun isWin(currentNumber: Int): Boolean {
        if (!cache.contains(currentNumber)) {
            val result = primeNumbers
                .filter { it < currentNumber }
                .map { currentNumber - it }
                .filter { it > 1 }
                .any { !isWin(it) }
            cache.put(currentNumber, result)
        }
        return cache.get(currentNumber)!!
    }

    when (isWin(n)) {
        true -> println("Win")
        false -> println("Lose")
    }
}

fun isPrimeNumber(n: Int): Boolean = !(2 until n).any { n % it == 0 }

