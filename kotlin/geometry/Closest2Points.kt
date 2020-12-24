package geometry

import numeric.FFT
import optimization.Simplex

object Closest2Points {
    // Find closest pair in O(n*log^2(n))
    fun findClosestPair(points: Array<Point?>): Array<Point?> {
        val result = arrayOfNulls<Point>(2)
        Arrays.sort(points, Comparator.comparingInt { p -> p.x })
        rec(points, 0, points.size - 1, result, Long.MAX_VALUE)
        return result
    }

    fun rec(points: Array<Point?>, l: Int, r: Int, result: Array<Point?>, mindist2: Long): Long {
        var mindist2 = mindist2
        if (l == r) return Long.MAX_VALUE
        val mid = l + r shr 1
        val midx = points[mid]!!.x
        val d1 = rec(points, l, mid, result, mindist2)
        mindist2 = Math.min(mindist2, d1)
        val d2 = rec(points, mid + 1, r, result, mindist2)
        mindist2 = Math.min(mindist2, d2)
        Arrays.sort(points, l, r + 1, Comparator.comparingInt { p -> p.y })
        val t = IntArray(r - l + 1)
        var size = 0
        for (i in l..r) if ((points[i]!!.x - midx).toLong() * (points[i]!!.x - midx) < mindist2) t[size++] = i
        for (i in 0 until size) {
            for (j in i + 1 until size) {
                val a = points[t[i]]
                val b = points[t[j]]
                if ((b!!.y - a!!.y).toLong() * (b.y - a.y) >= mindist2) break
                val dist2 = dist2(a, b)
                if (mindist2 > dist2) {
                    mindist2 = dist2
                    result[0] = a
                    result[1] = b
                }
            }
        }
        return mindist2
    }

    fun dist2(a: Point?, b: Point?): Long {
        val dx = (a!!.x - b!!.x).toLong()
        val dy = (a.y - b.y).toLong()
        return dx * dx + dy * dy
    }

    // random test
    fun main(args: Array<String?>?) {
        val rnd = Random()
        for (step in 0..9999) {
            val n = 100
            val points = arrayOfNulls<Point>(n)
            for (i in 0 until n) {
                points[i] = Point(rnd.nextInt(1000) - 500, rnd.nextInt(1000) - 500)
            }
            val p = findClosestPair(points)
            val res1 = dist2(p[0], p[1])
            val res2 = slowClosestPair(points)
            if (res1 != res2) throw RuntimeException("$res1 $res2")
        }
    }

    fun slowClosestPair(points: Array<Point?>): Long {
        var res = Long.MAX_VALUE
        for (i in points.indices) for (j in i + 1 until points.size) res = Math.min(
            res, dist2(
                points[i], points[j]
            )
        )
        return res
    }

    class Point(val x: Int, val y: Int)
}
