package geometry

import numeric.FFT
import optimization.Simplex

object ConvexHull {
    fun convexHull(points: Array<Point>): Array<Point> {
        Arrays.sort(
            points,
            Comparator.< Point > comparingInt < geometry . ConvexHull . Point ? > { p -> p.x }.thenComparingInt { p -> p.y })
        val n = points.size
        val hull = arrayOfNulls<Point>(n + 1)
        var cnt = 0
        for (i in 0 until 2 * n - 1) {
            val j: Int = if (i < n) i else 2 * n - 2 - i
            while (cnt >= 2 && isNotRightTurn(hull[cnt - 2], hull[cnt - 1], points[j])) --cnt
            hull[cnt++] = points[j]
        }
        return Arrays.copyOf(hull, cnt - 1)
    }

    fun isNotRightTurn(a: Point?, b: Point?, c: Point): Boolean {
        val cross = (a!!.x - b!!.x).toLong() * (c.y - b.y) - (a.y - b.y).toLong() * (c.x - b.x)
        val dot = (a.x - b.x).toLong() * (c.x - b.x) + (a.y - b.y).toLong() * (c.y - b.y)
        return cross < 0 || cross == 0L && dot <= 0
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val points = arrayOf(Point(0, 0), Point(0, 1), Point(1, 0), Point(0, 0))
        val convexHull = convexHull(points)
        System.out.println(Arrays.toString(convexHull))
    }

    class Point(val x: Int, val y: Int) {
        @Override
        override fun toString(): String {
            return ("Point{"
                    + "x=" + x + ", y=" + y + '}')
        }
    }
}
