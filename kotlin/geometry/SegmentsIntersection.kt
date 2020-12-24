package geometry

import java.util.Random

object SegmentsIntersection {
    fun isCrossIntersect(x1: Long, y1: Long, x2: Long, y2: Long, x3: Long, y3: Long, x4: Long, y4: Long): Boolean {
        val z1 = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1)
        val z2 = (x2 - x1) * (y4 - y1) - (y2 - y1) * (x4 - x1)
        val z3 = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)
        val z4 = (x4 - x3) * (y2 - y3) - (y4 - y3) * (x2 - x3)
        return (z1 < 0 || z2 < 0) && (z1 > 0 || z2 > 0) && (z3 < 0 || z4 < 0) && (z3 > 0 || z4 > 0)
    }

    fun isCrossOrTouchIntersect(
        x1: Long, y1: Long, x2: Long, y2: Long, x3: Long, y3: Long, x4: Long, y4: Long
    ): Boolean {
        if (Math.max(x1, x2) < Math.min(x3, x4) || Math.max(x3, x4) < Math.min(x1, x2) || Math.max(
                y1,
                y2
            ) < Math.min(y3, y4) || Math.max(y3, y4) < Math.min(y1, y2)
        ) return false
        val z1 = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1)
        val z2 = (x2 - x1) * (y4 - y1) - (y2 - y1) * (x4 - x1)
        val z3 = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)
        val z4 = (x4 - x3) * (y2 - y3) - (y4 - y3) * (x2 - x3)
        return (z1 <= 0 || z2 <= 0) && (z1 >= 0 || z2 >= 0) && (z3 <= 0 || z4 <= 0) && (z3 >= 0 || z4 >= 0)
    }

    fun getLinesIntersection(
        x1: Long, y1: Long, x2: Long, y2: Long, x3: Long, y3: Long, x4: Long, y4: Long
    ): Point2D.Double? {
        val a1 = y2 - y1
        val b1 = x1 - x2
        val c1 = -(x1 * y2 - x2 * y1)
        val a2 = y4 - y3
        val b2 = x3 - x4
        val c2 = -(x3 * y4 - x4 * y3)
        val det = a1 * b2 - a2 * b1
        if (det == 0L) return null
        val x = -(c1 * b2 - c2 * b1) / det.toDouble()
        val y = -(a1 * c2 - a2 * c1) / det.toDouble()
        return Double(x, y)
    }

    // random tests
    fun main(args: Array<String?>?) {
        val rnd = Random()
        for (step in 0..999999) {
            val r = 10
            val x1: Int = rnd.nextInt(r) - r / 2
            val y1: Int = rnd.nextInt(r) - r / 2
            val x2: Int = rnd.nextInt(r) - r / 2
            val y2: Int = rnd.nextInt(r) - r / 2
            val x3: Int = rnd.nextInt(r) - r / 2
            val y3: Int = rnd.nextInt(r) - r / 2
            val x4: Int = rnd.nextInt(r) - r / 2
            val y4: Int = rnd.nextInt(r) - r / 2
            val crossIntersect = isCrossIntersect(
                x1.toLong(),
                y1.toLong(),
                x2.toLong(),
                y2.toLong(),
                x3.toLong(),
                y3.toLong(),
                x4.toLong(),
                y4.toLong()
            )
            val crossOrTouchIntersect = isCrossOrTouchIntersect(
                x1.toLong(),
                y1.toLong(),
                x2.toLong(),
                y2.toLong(),
                x3.toLong(),
                y3.toLong(),
                x4.toLong(),
                y4.toLong()
            )
            var crossOrTouchIntersect2: Boolean = Line2D.linesIntersect(x1, y1, x2, y2, x3, y3, x4, y4)
            crossOrTouchIntersect2 =
                crossOrTouchIntersect2 and !(x1 == x2 && y1 == y2 && x3 == x4 && y3 == y4 && (x1 != x3 || y1 != y3)) // applying fix
            if (crossOrTouchIntersect != crossOrTouchIntersect2 || crossIntersect && !crossOrTouchIntersect) throw RuntimeException()
        }
        System.out.println(getLinesIntersection(0, 0, 4, 2, 2, -1, 2, 5).equals(Double(2, 1)))
    }
}
