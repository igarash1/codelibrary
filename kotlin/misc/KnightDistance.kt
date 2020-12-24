package misc

object KnightDistance {
    fun dist(x1: Int, y1: Int, x2: Int, y2: Int): Int {
        val dx: Int = Math.abs(x2 - x1)
        val dy: Int = Math.abs(y2 - y1)
        var moves: Int = Math.max((dx + 1) / 2, (dy + 1) / 2)
        moves = Math.max(moves, (dx + dy + 2) / 3)
        if (moves % 2 != (dx + dy) % 2) ++moves
        if (dx == 1 && dy == 0) return 3
        if (dy == 1 && dx == 0) return 3
        return if (dx == 2 && dy == 2) 4 else moves
    }

    fun main(args: Array<String?>?) {}
}
