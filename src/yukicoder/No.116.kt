package yukicoder

fun main() {
    val N = readLine()!!.toInt()
    val A = readLine()!!.split(" ").map(String::toInt)

    var count = 0
    for (index in (1 until A.size - 1)) {
        // 門松を生成する
        val kadomatsu = Triple(A[index - 1], A[index], A[index + 1])

        // 同じ高さがない事
        if (kadomatsu.first == kadomatsu.second
            || kadomatsu.second == kadomatsu.third
            || kadomatsu.first == kadomatsu.third
        ) {
            continue
        }

        // 真ん中の高さが2番目でない事
        if (kadomatsu.first > kadomatsu.second && kadomatsu.second > kadomatsu.third) {
            continue
        }
        if (kadomatsu.first < kadomatsu.second && kadomatsu.second < kadomatsu.third) {
            continue
        }
        count++
    }

    print("${count}")
}