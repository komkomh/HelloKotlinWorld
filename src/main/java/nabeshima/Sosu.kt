package nabeshima

fun main() {
    println((2..100).filter { (2 until it).none { num -> it % num == 0 } } .sum())
}