package object_study

fun main() {
    println("------------ original ------------")
    method()
    println("------------ refactoredMethod ------------")
    refactoredMethod()
}

fun method() {

    var str = ""

    for (i in 0..9) {

        for (j in 0..9) {

            if (i == j) {
                str = "$str■"
            } else {
                str = "$str●"
            }
        }
        str = str + "\n"
    }

    println(str)
}

// 全行分の文字列を取得する
fun refactoredMethod() = println((0..9).map { getLine(it) }.reduce { left, right -> left + "\n" + right })
// 行を取得する
fun getLine(i: Int): String = (0..9).map { getMark(i, it) }.reduce { left, right -> left + right }
// マークを取得する
fun getMark(i: Int, j: Int): String = if (i == j) "■" else "●"

fun getGrade(score: Int): String {
    return when {
        score > 80 -> "A"
        score > 60 -> "B"
        else -> "C"
    }
}