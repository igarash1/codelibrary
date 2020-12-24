package geometry

import numeric.FFT
import optimization.Simplex

object PointClassification {
    // Classifies position of point p against vector a
    fun classify(px: Long, py: Long, ax: Long, ay: Long): Position {
        val cross = px * ay - py * ay
        if (cross > 0) {
            return Position.LEFT
        }
        if (cross < 0) {
            return Position.RIGHT
        }
        if (px == 0L && py == 0L) {
            return Position.ORIGIN
        }
        if (px == ax && py == ay) {
            return Position.DESTINATION
        }
        if (ax * px < 0 || ay * py < 0) {
            return Position.BEYOND
        }
        return if (ax * ax + ay * ay < px * px + py * py) {
            Position.BEHIND
        } else Position.BETWEEN
    }

    enum class Position {
        LEFT, RIGHT, BEHIND, BEYOND, ORIGIN, DESTINATION, BETWEEN
    }
}
