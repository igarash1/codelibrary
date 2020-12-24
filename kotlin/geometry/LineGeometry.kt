package geometry

import numeric.FFT
import optimization.Simplex

object LineGeometry {
    const val EPS = 1e-10
    fun sign(a: Double): Int {
        return if (a < -EPS) -1 else if (a > EPS) 1 else 0
    }

    // Returns -1 for clockwise, 0 for straight line, 1 for counterclockwise order
    fun orientation(a: Point, b: Point, c: Point): Int {
        val AB = b.minus(a)
        val AC = c.minus(a)
        return sign(AB.cross(AC))
    }

    fun cw(a: Point, b: Point, c: Point): Boolean {
        return orientation(a, b, c) < 0
    }

    fun ccw(a: Point, b: Point, c: Point): Boolean {
        return orientation(a, b, c) > 0
    }

    fun isCrossIntersect(a: Point, b: Point, c: Point, d: Point): Boolean {
        return orientation(a, b, c) * orientation(a, b, d) < 0 && orientation(c, d, a) * orientation(c, d, b) < 0
    }

    fun isCrossOrTouchIntersect(a: Point, b: Point, c: Point, d: Point): Boolean {
        return if (Math.max(a.x, b.x) < Math.min(c.x, d.x) - EPS || Math.max(c.x, d.x) < Math.min(
                a.x,
                b.x
            ) - EPS || Math.max(a.y, b.y) < Math.min(c.y, d.y) - EPS || Math.max(
                c.y,
                d.y
            ) < Math.min(a.y, b.y) - EPS
        ) {
            false
        } else orientation(a, b, c) * orientation(
            a,
            b,
            d
        ) <= 0 && orientation(c, d, a) * orientation(c, d, b) <= 0
    }

    fun pointToLineDistance(p: Point, line: Line): Double {
        return Math.abs(line.a * p.x + line.b * p.y + line.c) / fastHypot(line.a, line.b)
    }

    fun fastHypot(x: Double, y: Double): Double {
        return Math.sqrt(x * x + y * y)
    }

    fun angleBetween(a: Point, b: Point): Double {
        return Math.atan2(a.cross(b), a.dot(b))
    }

    fun angle(line: Line): Double {
        return Math.atan2(-line.a, line.b)
    }

    // Usage example
    fun main(args: Array<String?>?) {}
    class Point(var x: Double, var y: Double) : Comparable<Point?> {
        operator fun minus(b: Point): Point {
            return Point(x - b.x, y - b.y)
        }

        fun cross(b: Point): Double {
            return x * b.y - y * b.x
        }

        fun dot(b: Point): Double {
            return x * b.x + y * b.y
        }

        fun rotateCCW(angle: Double): Point {
            return Point(x * Math.cos(angle) - y * Math.sin(angle), x * Math.sin(angle) + y * Math.cos(angle))
        }

        @Override
        operator fun compareTo(o: Point): Int {
            // return Double.compare(Math.atan2(y, x), Math.atan2(o.y, o.x));
            return if (Double.compare(x, o.x) !== 0) Double.compare(x, o.x) else Double.compare(y, o.y)
        }
    }

    class Line {
        var a: Double
        var b: Double
        var c: Double

        constructor(a: Double, b: Double, c: Double) {
            this.a = a
            this.b = b
            this.c = c
        }

        constructor(p1: Point, p2: Point) {
            a = +(p1.y - p2.y)
            b = -(p1.x - p2.x)
            c = p1.x * p2.y - p2.x * p1.y
        }

        fun intersect(line: Line): Point? {
            val d = a * line.b - line.a * b
            if (sign(d) == 0) {
                return null
            }
            val x = -(c * line.b - line.c * b) / d
            val y = -(a * line.c - line.a * c) / d
            return Point(x, y)
        }
    }
}
