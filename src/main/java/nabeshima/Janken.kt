package nabeshima

import kotlin.random.Random

fun main() {
    println("0=グー, 1=チョキ, 2=パー")
    val あなたの手 = 手.values()[(readLine()!!.toInt())]
    val CPUの手 = 手.values()[Random.nextInt(3)]
    println("あなたの手:${あなたの手} / CPUの手:${CPUの手}")
    val sa = CPUの手.ordinal - あなたの手.ordinal
    println("あなたの${結果.values()[(sa % -2) + (sa / -2) + 1]}")

    fun xx(me: 手, you: 手): Int {
        return me.ordinal - you.ordinal
    }
}
enum class 手 { グー, チョキ, パー }
enum class 結果 { 負け, あいこ, 勝ち }