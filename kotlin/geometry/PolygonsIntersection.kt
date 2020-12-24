package geometry

import numeric.FFT
import optimization.Simplex

object PolygonsIntersection {
    fun overlap(polygon1: Array<Point>, polygon2: Array<Point>): Double {
        val polygons = arrayOf(polygon1, polygon2)
        val xs: Set<Double> = TreeSet()
        for (point in polygon1) xs.add(point.x)
        for (point in polygon2) xs.add(point.x)
        var i1 = 0
        var j1 = polygon1.size - 1
        while (i1 < polygon1.size) {
            var i2 = 0
            var j2 = polygon2.size - 1
            while (i2 < polygon2.size) {
                val intersection = getSegmentsIntersection(polygon1[i1], polygon1[j1], polygon2[i2], polygon2[j2])
                if (intersection != null) {
                    xs.add(intersection.x)
                }
                j2 = i2++
            }
            j1 = i1++
        }
        val xsa: Array<Double> = xs.toArray(arrayOfNulls<Double>(xs.size()))
        var res = 0.0
        var k = 0
        while (k + 1 < xsa.size) {
            val x = (xsa[k] + xsa[k + 1]) * 0.5
            val sweep0 = Point(x, 0)
            val sweep1 = Point(x, 1)
            val events: List<Event> = ArrayList()
            for (p in 0..1) {
                val polygon = polygons[p]
                var area = 0.0
                run {
                    var i = 0
                    var j = polygon.size - 1
                    while (i < polygon.size) {
                        area += (polygon[j].x - polygon[i].x) * (polygon[j].y + polygon[i].y)
                        j = i++
                    }
                }
                var j = 0
                var i = polygon.size - 1
                while (j < polygon.size) {
                    val intersection = getLinesIntersection(polygon[j], polygon[i], sweep0, sweep1)
                    if (intersection != null) {
                        val y = intersection.y
                        val x0 = polygon[i].x
                        val x1 = polygon[j].x
                        if (x0 < x && x1 > x) {
                            events.add(Event(y, Math.signum(area) as Int * (1 shl p)))
                        } else if (x0 > x && x1 < x) {
                            events.add(Event(y, -Math.signum(area) as Int * (1 shl p)))
                        }
                    }
                    i = j++
                }
            }
            Collections.sort(events)
            var a = 0.0
            var mask = 0
            for (j in 0 until events.size()) {
                if (mask == 3) a += events[j].y - events[j - 1].y
                mask += events[j].maskDelta
            }
            res += a * (xsa[k + 1] - xsa[k])
            k++
        }
        return res
    }

    const val eps = 1e-9
    fun getLinesIntersection(p1: Point, p2: Point, p3: Point, p4: Point): Point? {
        val a1 = p2.y - p1.y
        val b1 = p1.x - p2.x
        val c1 = -(p1.x * p2.y - p2.x * p1.y)
        val a2 = p4.y - p3.y
        val b2 = p3.x - p4.x
        val c2 = -(p3.x * p4.y - p4.x * p3.y)
        val det = a1 * b2 - a2 * b1
        if (Math.abs(det) < eps) return null
        val x = -(c1 * b2 - c2 * b1) / det
        val y = -(a1 * c2 - a2 * c1) / det
        return Point(x, y)
    }

    fun getSegmentsIntersection(p1: Point, p2: Point, p3: Point, p4: Point): Point? {
        return if (!isCrossIntersect(p1, p2, p3, p4)) null else getLinesIntersection(p1, p2, p3, p4)
    }

    fun isCrossIntersect(p1: Point, p2: Point, p3: Point, p4: Point): Boolean {
        val z1 = (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x)
        val z2 = (p2.x - p1.x) * (p4.y - p1.y) - (p2.y - p1.y) * (p4.x - p1.x)
        val z3 = (p4.x - p3.x) * (p1.y - p3.y) - (p4.y - p3.y) * (p1.x - p3.x)
        val z4 = (p4.x - p3.x) * (p2.y - p3.y) - (p4.y - p3.y) * (p2.x - p3.x)
        return (z1 < -eps || z2 < -eps) && (z1 > eps || z2 > eps) && (z3 < -eps || z4 < -eps) && (z3 > eps || z4 > eps)
    }

    class Point(var x: Double, var y: Double)
    internal class Event(var y: Double, var maskDelta: Int) : Comparable<Event?> {
        @Override
        operator fun compareTo(o: Event): Int {
            return if (y != o.y) Double.compare(y, o.y) else Integer.compare(maskDelta, o.maskDelta)
        }
    }
}
