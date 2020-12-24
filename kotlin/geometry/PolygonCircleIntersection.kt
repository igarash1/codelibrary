package geometry

import java.awt.geom.Point2D

object PolygonCircleIntersection {
    const val eps = 1e-9
    fun polygonCircleIntersection(x: IntArray, y: IntArray, r: Int): Double {
        var area = 0.0
        var i = 0
        var j = x.size - 1
        while (i < x.size) {
            val cur = circleTriangleIntersection(r.toDouble(), x[j], y[j], x[i], y[i])
            area += Math.signum(x[j] * y[i] - x[i] * y[j]) * cur
            j = i++
        }
        return Math.abs(area)
    }

    fun circleTriangleIntersection(r: Double, x1: Int, y1: Int, x2: Int, y2: Int): Double {
        val points: List<Point2D.Double> = ArrayList()
        points.add(Double(x1, y1))
        for (p in circleLineIntersection(
            r,
            (y2 - y1).toDouble(),
            (x1 - x2).toDouble(),
            -(x1 * y2 - x2 * y1).toDouble()
        )) if (isMiddle(p.x, p.y, x1.toDouble(), y1.toDouble(), x2.toDouble(), y2.toDouble())) points.add(p)
        points.add(Double(x2, y2))
        var area = 0.0
        var i = 0
        while (i + 1 < points.size()) {
            val p1: Point2D.Double = points[i]
            val p2: Point2D.Double = points[i + 1]
            if (p1.x * p1.x + p1.y * p1.y > r * r + eps || p2.x * p2.x + p2.y * p2.y > r * r + eps) area += angleBetween(
                p1.x,
                p1.y,
                p2.x,
                p2.y
            ) / 2 * r * r else area += Math.abs(p1.x * p2.y - p2.x * p1.y) / 2
            i++
        }
        return area
    }

    fun angleBetween(ax: Double, ay: Double, bx: Double, by: Double): Double {
        val a: Double = Math.abs(Math.atan2(by, bx) - Math.atan2(ay, ax))
        return Math.min(a, 2 * Math.PI - a)
    }

    fun circleLineIntersection(r: Double, a: Double, b: Double, c: Double): Array<Point2D.Double?> {
        val aabb = a * a + b * b
        var d = c * c / aabb - r * r
        if (d > eps) return arrayOfNulls<Point2D.Double>(0)
        val x0 = -a * c / aabb
        val y0 = -b * c / aabb
        if (d > -eps) return arrayOf(Double(x0, y0))
        d /= -aabb
        val k: Double = Math.sqrt(if (d < 0) 0 else d)
        return arrayOf(
            Double(x0 + k * b, y0 - k * a), Double(x0 - k * b, y0 + k * a)
        )
    }

    fun isMiddle(x: Double, y: Double, x1: Double, y1: Double, x2: Double, y2: Double): Boolean {
        return ((x < x1 + eps || x < x2 + eps) && (x > x1 - eps || x > x2 - eps) && (y < y1 + eps || y < y2 + eps)
                && (y > y1 - eps || y > y2 - eps))
    }
}
