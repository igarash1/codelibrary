package geometry

import java.awt.geom.Line2D

object PointToSegmentDistance {
    fun pointToSegmentDistance(x: Int, y: Int, x1: Int, y1: Int, x2: Int, y2: Int): Double {
        val dx = (x2 - x1).toLong()
        val dy = (y2 - y1).toLong()
        val px = (x - x1).toLong()
        val py = (y - y1).toLong()
        val squaredLength = dx * dx + dy * dy
        val dotProduct = dx * px + dy * py
        if (dotProduct <= 0 || squaredLength == 0L) return fastHypot(px.toDouble(), py.toDouble())
        if (dotProduct >= squaredLength) return fastHypot((px - dx).toDouble(), (py - dy).toDouble())
        val q = dotProduct.toDouble() / squaredLength
        return fastHypot(px - q * dx, py - q * dy)
    }

    fun fastHypot(x: Double, y: Double): Double {
        return Math.sqrt(x * x + y * y)
    }

    // Line2D.ptLineDist
    fun pointToLineDistance(x: Long, y: Long, a: Long, b: Long, c: Long): Double {
        return Math.abs(a * x + b * y + c) / fastHypot(a.toDouble(), b.toDouble())
    }

    // random test
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        for (step in 0..999999) {
            val r = 10
            val x: Int = rnd.nextInt(r) - r / 2
            val y: Int = rnd.nextInt(r) - r / 2
            val x1: Int = rnd.nextInt(r) - r / 2
            val y1: Int = rnd.nextInt(r) - r / 2
            val x2: Int = rnd.nextInt(r) - r / 2
            val y2: Int = rnd.nextInt(r) - r / 2
            val res1 = pointToSegmentDistance(x, y, x1, y1, x2, y2)
            val res2: Double = Line2D.ptSegDist(x1, y1, x2, y2, x, y)
            if (Math.abs(res1 - res2) >= 1e-9) throw RuntimeException()
        }
    }
}
