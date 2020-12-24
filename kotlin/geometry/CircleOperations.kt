package geometry

import numeric.FFT
import optimization.Simplex

object CircleOperations {
    const val EPS = 1e-10
    fun fastHypot(x: Double, y: Double): Double {
        return Math.sqrt(x * x + y * y)
    }

    // geometric solution
    fun circleLineIntersection(circle: Circle, line: Line): Array<Point?> {
        val a = line.a
        val b = line.b
        val c = line.c + circle.x * a + circle.y * b
        val r = circle.r
        val aabb = a * a + b * b
        var d = c * c / aabb - r * r
        if (d > EPS) return arrayOfNulls(0)
        val x0 = -a * c / aabb
        val y0 = -b * c / aabb
        if (d > -EPS) return arrayOf(Point(x0 + circle.x, y0 + circle.y))
        d /= -aabb
        val k: Double = Math.sqrt(if (d < 0) 0 else d)
        return arrayOf(
            Point(x0 + k * b + circle.x, y0 - k * a + circle.y),
            Point(x0 - k * b + circle.x, y0 + k * a + circle.y)
        )
    }

    // algebraic solution
    fun circleLineIntersection2(circle: Circle, line: Line): Array<Point?> {
        return if (Math.abs(line.a) >= Math.abs(line.b)) intersection(
            line.a,
            line.b,
            line.c,
            circle.x,
            circle.y,
            circle.r,
            false
        ) else intersection(line.b, line.a, line.c, circle.y, circle.x, circle.r, true)
    }

    fun intersection(a: Double, b: Double, c: Double, CX: Double, CY: Double, R: Double, swap: Boolean): Array<Point?> {
        // ax+by+c=0
        // (by+c+aCX)^2+(ay-aCY)^2=(aR)^2
        val A = a * a + b * b
        val B = 2.0 * b * (c + a * CX) - 2.0 * a * a * CY
        val C = (c + a * CX) * (c + a * CX) + a * a * (CY * CY - R * R)
        var d = B * B - 4 * A * C
        if (d < -EPS) return arrayOfNulls(0)
        d = Math.sqrt(if (d < 0) 0 else d)
        val y1 = (-B + d) / (2 * A)
        val x1 = (-c - b * y1) / a
        val y2 = (-B - d) / (2 * A)
        val x2 = (-c - b * y2) / a
        return if (swap) if (d > EPS) arrayOf(Point(y1, x1), Point(y2, x2)) else arrayOf<Point>(
            Point(
                y1,
                x1
            )
        ) else if (d > EPS) arrayOf<Point>(
            Point(x1, y1), Point(x2, y2)
        ) else arrayOf<Point>(Point(x1, y1))
    }

    fun circleCircleIntersection(c1: Circle, c2: Circle): Array<Point?>? {
        if (fastHypot(c1.x - c2.x, c1.y - c2.y) < EPS) {
            return if (Math.abs(c1.r - c2.r) < EPS) null else arrayOfNulls(0) // infinity intersection points
        }
        val dx = c2.x - c1.x
        val dy = c2.y - c1.y
        val A = -2 * dx
        val B = -2 * dy
        val C = dx * dx + dy * dy + c1.r * c1.r - c2.r * c2.r
        val res = circleLineIntersection(Circle(0, 0, c1.r), Line(A, B, C))
        for (point in res) {
            point!!.x += c1.x
            point!!.y += c1.y
        }
        return res
    }

    fun circleCircleIntersectionArea(
        c1: Circle,
        c2: Circle
    ): Double {
        val r: Double = Math.min(c1.r, c2.r)
        val R: Double = Math.max(c1.r, c2.r)
        val d = fastHypot(c1.x - c2.x, c1.y - c2.y)
        if (d < R - r + EPS) return Math.PI * r * r
        return if (d > R + r - EPS) 0 else r * r * Math.acos((d * d + r * r - R * R) / 2 / d / r)
                + R * R * Math.acos((d * d + R * R - r * r) / 2 / d / R)
                - 0.5 * Math.sqrt((-d + r + R) * (d + r - R) * (d - r + R) * (d + r + R))
    }

    fun tangents(a: Circle, b: Circle): Array<Line> {
        val lines: List<Line> = ArrayList()
        var i = -1
        while (i <= 1) {
            var j = -1
            while (j <= 1) {
                tangents(Point(b.x - a.x, b.y - a.y), a.r * i, b.r * j, lines)
                j += 2
            }
            i += 2
        }
        for (line in lines) line.c -= line.a * a.x + line.b * a.y
        return lines.toArray(arrayOfNulls<Line>(lines.size()))
    }

    fun tangents(center2: Point, r1: Double, r2: Double, lines: List<Line?>) {
        val r = r2 - r1
        val z = center2.x * center2.x + center2.y * center2.y
        var d = z - r * r
        if (d < -EPS) return
        d = Math.sqrt(if (d < 0) 0 else d)
        lines.add(Line((center2.x * r + center2.y * d) / z, (center2.y * r - center2.x * d) / z, r1))
    }

    // min enclosing circle in O(n) on average
    fun minEnclosingCircle(points: Array<Point>): Circle {
        if (points.size == 0) return Circle(0, 0, 0)
        if (points.size == 1) return Circle(points[0].x, points[0].y, 0)
        Collections.shuffle(Arrays.asList(points))
        var circle = getCircumCircle(points[0], points[1])
        for (i in 2 until points.size) {
            if (!circle.contains(points[i])) {
                circle = getCircumCircle(points[0], points[i])
                for (j in 1 until i) {
                    if (!circle.contains(points[j])) {
                        circle = getCircumCircle(points[j], points[i])
                        for (k in 0 until j) {
                            if (!circle.contains(points[k])) {
                                circle = getCircumCircle(points[i], points[j], points[k])
                            }
                        }
                    }
                }
            }
        }
        return circle
    }

    fun getCircumCircle(a: Point, b: Point): Circle {
        val x = (a.x + b.x) / 2.0
        val y = (a.y + b.y) / 2.0
        val r = fastHypot(a.x - x, a.y - y)
        return Circle(x, y, r)
    }

    fun getCircumCircle(a: Point, b: Point, c: Point): Circle {
        val Bx = b.x - a.x
        val By = b.y - a.y
        val Cx = c.x - a.x
        val Cy = c.y - a.y
        val d = 2 * (Bx * Cy - By * Cx)
        if (Math.abs(d) < EPS) return getCircumCircle(
            Point(Math.min(a.x, Math.min(b.x, c.x)), Math.min(a.y, Math.min(b.y, c.y))),
            Point(Math.max(a.x, Math.max(b.x, c.x)), Math.max(a.y, Math.max(b.y, c.y)))
        )
        val z1 = Bx * Bx + By * By
        val z2 = Cx * Cx + Cy * Cy
        val cx = Cy * z1 - By * z2
        val cy = Bx * z2 - Cx * z1
        val x = cx / d
        val y = cy / d
        val r = fastHypot(x, y)
        return Circle(x + a.x, y + a.y, r)
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        for (step in 0..99999) {
            val range = 10
            val x: Int = rnd.nextInt(range) - range / 2
            val y: Int = rnd.nextInt(range) - range / 2
            val r: Int = rnd.nextInt(range)
            val x1: Int = rnd.nextInt(range) - range / 2
            val y1: Int = rnd.nextInt(range) - range / 2
            val x2: Int = rnd.nextInt(range) - range / 2
            val y2: Int = rnd.nextInt(range) - range / 2
            if (x1 == x2 && y1 == y2) continue
            val p1 = circleLineIntersection(
                Circle(
                    x.toDouble(), y.toDouble(), r.toDouble()
                ), Line(
                    Point(x1.toDouble(), y1.toDouble()), Point(
                        x2.toDouble(), y2.toDouble()
                    )
                )
            )
            val p2 = circleLineIntersection2(
                Circle(
                    x.toDouble(), y.toDouble(), r.toDouble()
                ), Line(
                    Point(x1.toDouble(), y1.toDouble()), Point(
                        x2.toDouble(), y2.toDouble()
                    )
                )
            )
            if (p1.size != p2.size || p1.size == 1 && !eq(p1[0], p2[0]) || p1.size == 2 && !(eq(
                    p1[0], p2[0]
                ) && eq(p1[1], p2[1]) || eq(p1[0], p2[1]) && eq(
                    p1[1], p2[0]
                ))
            ) throw RuntimeException()
        }
    }

    fun eq(p1: Point?, p2: Point?): Boolean {
        return fastHypot(p1!!.x - p2!!.x, p1.y - p2.y) <= 1e-9
    }

    class Point(var x: Double, var y: Double)
    class Circle(var x: Double, var y: Double, var r: Double) {
        operator fun contains(p: Point): Boolean {
            return fastHypot(p.x - x, p.y - y) < r + EPS
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
    }
}
