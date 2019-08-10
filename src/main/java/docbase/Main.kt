package docbase

import com.atilika.kuromoji.ipadic.Tokenizer

fun main() {
    println("hello!!!")
    tokenize("")
}

fun tokenize(message: String) {
    val tokenizer = Tokenizer()
    val tokens = tokenizer.tokenize("すもももももももものうち")
    for (token in tokens) {
        println(token.surface + " - " + token.allFeatures)
    }
}
