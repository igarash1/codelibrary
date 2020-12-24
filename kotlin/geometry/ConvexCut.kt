package geometry

import numeric.FFT
import optimization.Simplex

object ConvexCut {
    // cuts right part of convex polygon (returns left part)
    fun convexCut(poly: Array<Point>, p1: Point, p2: Point): Array<Point> {
        val n = poly.size
        val res: List<Point> = ArrayList()
        var i = 0
        var j = n - 1
        while (i < n) {
            val d1 = orientation(p1.x, p1.y, p2.x, p2.y, poly[j].x, poly[j].y)
            val d2 = orientation(p1.x, p1.y, p2.x, p2.y, poly[i].x, poly[i].y)
            if (d1 >= 0) res.add(poly[j])
            if (d1 * d2 < 0) res.add(
                getLinesIntersection(
                    p1.x,
                    p1.y,
                    p2.x,
                    p2.y,
                    poly[j].x,
                    poly[j].y,
                    poly[i].x,
                    poly[i].y
                )
            )
            j = i++
        }
        return res.toArray(arrayOfNulls<Point>(res.size()))
    }

    fun orientation(ax: Double, ay: Double, bx: Double, by: Double, cx: Double, cy: Double): Int {
        var bx = bx
        var by = by
        var cx = cx
        var cy = cy
        bx -= ax
        by -= ay
        cx -= ax
        cy -= ay
        val cross = bx * cy - by * cx
        val EPS = 1e-9
        return if (cross < -EPS) -1 else if (cross > EPS) 1 else 0
    }

    fun getLinesIntersection(
        x1: Double, y1: Double, x2: Double, y2: Double, x3: Double, y3: Double, x4: Double, y4: Double
    ): Point {
        val a1 = y2 - y1
        val b1 = x1 - x2
        val c1 = -(x1 * y2 - x2 * y1)
        val a2 = y4 - y3
        val b2 = x3 - x4
        val c2 = -(x3 * y4 - x4 * y3)
        val det = a1 * b2 - a2 * b1
        val x = -(c1 * b2 - c2 * b1) / det
        val y = -(a1 * c2 - a2 * c1) / det
        return Point(x, y)
    }

    fun main(args: Array<String?>?) {}
    class Point(var x: Double, var y: Double)
}
