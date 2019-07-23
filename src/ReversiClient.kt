//import kotlinx.coroutines.*
//
//// 盤のサイズ
//const val BAN_SIZE: Int = 8
//// 探索する深さ
//const val SEARCH_DEPTH: Int = 6
//// 石を裏返す向き
//val DIRECTIONS: Array<Pair<Int, Int>> =
//	arrayOf(Pair(-1, -1), Pair(-1, 0), Pair(-1, 1), Pair(0, -1), Pair(0, 1), Pair(1, -1), Pair(1, 0), Pair(1, 1))
//// 角の場所
//val KADOS: Array<Pair<Int, Int>> =
//	arrayOf(Pair(0, 0), Pair(0, BAN_SIZE - 1), Pair(BAN_SIZE - 1, 0), Pair(BAN_SIZE - 1, BAN_SIZE - 1))
//
//fun main() {
//	println("main ${Thread.currentThread().id}")
//	// 初期値の盤を生成する
//	var ban = createNewBan()
//	show(ban)
//
//	do {
//		// マスを取得する
//		val position = when (ban.teban) {
//			Stone.BLACK -> getInputPosition(ban)    // 黒：プレイヤー
//			Stone.WHITE -> getScoredPosition(ban)    // 白：マシン
//		}
//		ban = position?.let {
//			ban.putStone(it)
//		} ?: run {
//			println("pass...")
//			ban.pass()
//		}
//		// 盤を表示する
//		show(ban)
//	} while (!ban.isGameEnd())
//	println("game end...")
//}
//
//fun getScoredPosition(ban: Ban) = runBlocking<Pair<Int, Int>?> {
//	ban.getPositions()
//		.map {
//			val score: Deferred<Int> = GlobalScope.async {
//				println("threadId:${Thread.currentThread().id}")
//				val nextBan = ban.putStone(it)
//				getMinMaxScore(nextBan, ban.teban, SEARCH_DEPTH)
//			}
//			Pair(it, score)
//		}
//		.maxBy { it.second.await() }
//		.let { it?.first }
//}
//
//fun getMinMaxScore(currentBan: Ban, teban: Stone, index: Int): Int {
//
//	if (index <= 0 && currentBan.teban == teban) {
//		return currentBan.calcScore();
//	}
//
//	val scores = currentBan.getPositions().map {
//		val nextBan = currentBan.putStone(it)
//		getMinMaxScore(nextBan, teban, index - 1)
//	}
//	// 最大値 or 最小値を取得する
//	val score = if (currentBan.teban == teban) scores.max() else scores.min()
//	return score ?: run {
//		val nextBan = currentBan.pass()
//		getMinMaxScore(nextBan, teban, index - 1)
//	}
//}
//
//// 初期値で盤を生成する
//fun createNewBan(): Ban {
//	var banmen: Array<Array<Stone?>> = Array(BAN_SIZE, { arrayOfNulls<Stone?>(BAN_SIZE) })
//	banmen[BAN_SIZE / 2][BAN_SIZE / 2] = Stone.WHITE
//	banmen[BAN_SIZE / 2 - 1][BAN_SIZE / 2 - 1] = Stone.WHITE
//	banmen[BAN_SIZE / 2 - 1][BAN_SIZE / 2] = Stone.BLACK
//	banmen[BAN_SIZE / 2][BAN_SIZE / 2 - 1] = Stone.BLACK
//	return Ban(banmen, Stone.BLACK)
//}
//
//// 入力する：デバッグ用
//fun getInputPosition(ban: Ban): Pair<Int, Int>? {
//	val positions = ban.getPositions()
//	if (positions.isEmpty()) {
//		return null
//	}
//	while (true) {
//		// 入力を受け付ける
//		val input = readLine()
//		try {
//			// 入力値を数値に変換する
//			val inputNumber = input?.toInt()
//			// 入力値のマスが存在すれば
//			positions.withIndex().find { (it.index + 1) == inputNumber }?.let {
//				// 選択マスとして返却する
//				return it.value
//			}
//		} catch (e: java.lang.NumberFormatException) {
//			;
//		}
//	}
//}
//
//// 表示する：デバッグ用
//fun show(ban: Ban) {
//	println("-------------")
//	when (ban.teban) {
//		Stone.BLACK -> println("next ●")
//		Stone.WHITE -> println("next ○")
//	}
//	val positions = ban.getPositions()
//
//	ban.banmen.withIndex().forEach { array ->
//		array.value.withIndex().forEach { masu ->
//			masu.value.also {
//				when (it) {
//					Stone.BLACK -> print("|●")
//					Stone.WHITE -> print("|○")
//				}
//			} ?: run {
//				positions.withIndex().find { it.value == Pair(array.index, masu.index) }?.let {
//					print("|${it.index + 1}")
//				} ?: run {
//					print("| ")
//				}
//			}
//		}
//		println("|")
//	}
//}
//
//// 石を表現する
//enum class Stone {
//	BLACK {
//		override fun reverse() = WHITE
//	},
//	WHITE {
//		override fun reverse() = BLACK
//	};
//
//	// 裏返す
//	abstract fun reverse(): Stone
//}
//
//// 盤を表現する
//class Ban(var banmen: Array<Array<Stone?>>, var teban: Stone) {
//
//	// 石を置けるマスを取得する
//	fun getPositions(): List<Pair<Int, Int>> {
//		var results: List<Pair<Int, Int>> = mutableListOf<Pair<Int, Int>>()
//		for (y in 0 until BAN_SIZE) {
//			for (x in 0 until BAN_SIZE) {
//				val position: Pair<Int, Int> = Pair(y, x)
//				if (getStone(position) == null) {
//					if (getReverseCount(position) > 0) {
//						results += position
//					}
//				}
//			}
//		}
//		return results
//	}
//
//	// 反転数を取得する
//	fun getReverseCount(position: Pair<Int, Int>): Int {
//		// 1行毎の反転数を取得する
//		fun getReverseLineCount(position: Pair<Int, Int>, direction: Pair<Int, Int>, reverseCount: Int): Int {
//			val newPosition = Pair(position.first + direction.first, position.second + direction.second)
//			return when (getStone(newPosition)) {
//				teban -> reverseCount
//				teban.reverse() -> getReverseLineCount(newPosition, direction, reverseCount + 1)
//				else -> 0
//			}
//		}
//		return DIRECTIONS.map { direction -> getReverseLineCount(position, direction, 0) }.sum()
//	}
//
//	// 位置を指定して石を取得する
//	fun getStone(position: Pair<Int, Int>): Stone? {
//		return when {
//			position.first < 0 -> null
//			position.first >= BAN_SIZE -> null
//			position.second < 0 -> null
//			position.second >= BAN_SIZE -> null
//			else -> banmen[position.first][position.second]
//		}
//	}
//
//	// 指定位置に石を置く
//	fun putStone(position: Pair<Int, Int>): Ban {
//		// 盤面を複製する
//		val copiedBanmen = Array(BAN_SIZE, { y ->
//			Array(BAN_SIZE, { x -> banmen[y][x] })
//		})
//
//		// 1行裏返す
//		fun reverseLine(position: Pair<Int, Int>, direction: Pair<Int, Int>): Boolean {
//			val newPosition = Pair(position.first + direction.first, position.second + direction.second)
//			return when (getStone(newPosition)) {
//				teban -> true
//				teban.reverse() -> {
//					val reverse = reverseLine(newPosition, direction)
//					if (reverse) {
//						copiedBanmen[newPosition.first][newPosition.second] = teban
//					}
//					reverse
//				}
//				else -> false
//			}
//		}
//		// 1行ずつ裏返す
//		DIRECTIONS.map { direction -> reverseLine(position, direction) }
//		// 指定されたマスに石を置く
//		copiedBanmen[position.first][position.second] = teban
//		return Ban(copiedBanmen, teban.reverse())
//	}
//
//	// 手番を渡す
//	fun pass(): Ban {
//		// 盤面を複製する
//		val copiedBanmen = Array(BAN_SIZE, { y ->
//			Array(BAN_SIZE, { x -> banmen[y][x] })
//		})
//		return Ban(copiedBanmen, this.teban.reverse())
//	}
//
//	fun isGameEnd(): Boolean {
//		return getPositions().isEmpty() && pass().getPositions().isEmpty()
//	}
//
//	fun getLinedBanmen(): List<Stone> {
//		return this.banmen.flatMap { array -> array.asList() }.filterNotNull()
//	}
//
//	fun getCount(stone: Stone): Int {
//		return getLinedBanmen().filter { masu -> masu == stone }.count()
//	}
//
//	// 盤面を点数化する
//	fun calcScore(): Int {
//		// ゲームが終了していれば
//		if (isGameEnd()) {
//			// 石の数を取得して
//			val stoneCount = getCount(teban)
//			val reversedStoneCount = getCount(teban.reverse())
//			return when {
//				stoneCount > reversedStoneCount -> 1000    // 勝っていれば 1000点
//				else -> 0    // 負けor引き分けていれば 0点
//			}
//		}
//		// 置ける位置数スコアを算出する
//		val positionScore: Int = getPositions().size
//		// 角の石数スコアを算出する
//		val kadoScore = KADOS.map { getStone(it) }.filterNotNull().filter { it == teban }.size * 100
//		// 自身のスコアを算出する
//		val score = positionScore - kadoScore
//		// 置ける位置数スコアを算出する
//		val aitePositionScore: Int = pass().getPositions().size
//		// 角の石数スコアを算出する
//		val aiteKadoScore = KADOS.map { getStone(it) }.filterNotNull().filter { it == teban.reverse() }.size * 100
//		// 相手のスコアを算出する
//		val aiteScore = aitePositionScore + aiteKadoScore
//		// 合計値を盤面スコアとする
//		return score - aiteScore
//	}
//}