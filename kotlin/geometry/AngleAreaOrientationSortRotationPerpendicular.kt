package geometry

import numeric.FFT
import optimization.Simplex

class AngleAreaOrientationSortRotationPerpendicular {
    class Point(var x: Int, var y: Int) : Comparable<Point?> {
        @Override
        operator fun compareTo(o: Point): Int {
            val up1 = y > 0 || y == 0 && x >= 0
            val up2 = o.y > 0 || o.y == 0 && o.x >= 0
            if (up1 != up2) return if (up1) -1 else 1
            val cmp: Int = Long.signum(o.x.toLong() * y - o.y.toLong() * x)
            return if (cmp != 0) cmp else Long.compare(
                x.toLong() * x + y.toLong() * y,
                o.x.toLong() * o.x + o.y.toLong() * o.y
            )
            // return Double.compare(Math.atan2(y, x), Math.atan2(o.y, o.x));
        }

        @Override
        override fun toString(): String {
            return "($x,$y)"
        }
    }

    fun rotateCCW(p: Point2D.Double, angle: Double): Point2D.Double {
        return Double(
            p.x * Math.cos(angle) - p.y * Math.sin(angle), p.x * Math.sin(angle) + p.y * Math.cos(angle)
        )
    }

    fun perpendicular(line: Line, x: Long, y: Long): Line {
        return Line(-line.b, line.a, line.b * x - line.a * y)
    }

    class Line(var a: Long, var b: Long, var c: Long)
    companion object {
        // pay attention to case ax==0 && ay==0 or bx==0 && by == 0
        fun angleBetween(ax: Long, ay: Long, bx: Long, by: Long): Double {
            val a: Double = Math.atan2(ax * by - ay * bx, ax * bx + ay * by)
            return if (a < 0) a + 2 * Math.PI else a
        }

        // pay attention to case ax==0 && ay==0 or bx==0 && by == 0
        fun angleBetween2(ax: Long, ay: Long, bx: Long, by: Long): Double {
            val a: Double = Math.atan2(by, bx) - Math.atan2(ay, ax)
            return if (a < 0) a + 2 * Math.PI else a
        }

        fun doubleSignedArea(x: IntArray, y: IntArray): Long {
            val n = x.size
            var area: Long = 0
            var i = 0
            var j = n - 1
            while (i < n) {
                area += (x[i] - x[j]).toLong() * (y[i] + y[j]) // area += (long) x[i] * y[j] - (long) x[j] * y[i];
                j = i++
            }
            return area
        }

        // Returns -1 for clockwise, 0 for straight line, 1 for counterclockwise orientation
        fun orientation(ax: Long, ay: Long, bx: Long, by: Long, cx: Long, cy: Long): Int {
            var bx = bx
            var by = by
            var cx = cx
            var cy = cy
            bx -= ax
            by -= ay
            cx -= ax
            cy -= ay
            return Long.signum(bx * cy - by * cx)
        }

        fun isMiddle(a: Long, m: Long, b: Long): Boolean {
            return Math.min(a, b) <= m && m <= Math.max(a, b)
        }

        fun isMiddle(ax: Long, ay: Long, mx: Long, my: Long, bx: Long, by: Long): Boolean {
            return orientation(ax, ay, mx, my, bx, by) == 0 && isMiddle(ax, mx, bx) && isMiddle(ay, my, by)
        }

        // random test
        fun main(args: Array<String?>?) {
            val rnd = Random(1)
            val range = 100
            for (step in 0..99999) {
                val ax: Long = rnd.nextInt(range) - range / 2
                val ay: Long = rnd.nextInt(range) - range / 2
                val bx: Long = rnd.nextInt(range) - range / 2
                val by: Long = rnd.nextInt(range) - range / 2
                val cx: Long = rnd.nextInt(range) - range / 2
                val cy: Long = rnd.nextInt(range) - range / 2
                val orientation1 = orientation(ax, ay, bx, by, cx, cy)
                val orientation2: Int = -Line2D.relativeCCW(ax, ay, bx, by, cx, cy)
                if (orientation1 == 0) continue
                if (orientation1 != orientation2) throw RuntimeException()
                if (ax == 0L && ay == 0L || bx == 0L && by == 0L) continue
                val res1 = angleBetween(ax, ay, bx, by)
                val res2 = angleBetween2(ax, ay, bx, by)
                if (Math.abs(res1 - res2) >= 1e-9) throw RuntimeException()
            }
            val points = arrayOf(Point(1, 1), Point(1, -1), Point(0, 0))
            Arrays.sort(points)
            System.out.println(Arrays.toString(points))
            System.out.println(Math.atan2(1, 1))
        }
    }
}
